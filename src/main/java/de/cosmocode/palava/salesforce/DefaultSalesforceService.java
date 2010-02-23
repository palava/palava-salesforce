/**
 * palava - a java-php-bridge
 * Copyright (C) 2007-2010  CosmoCode GmbH
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package de.cosmocode.palava.salesforce;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.sforce.soap.enterprise.GetUserInfoResult;
import com.sforce.soap.enterprise.InvalidIdFault;
import com.sforce.soap.enterprise.LoginFault;
import com.sforce.soap.enterprise.LoginResult;
import com.sforce.soap.enterprise.SessionHeader;
import com.sforce.soap.enterprise.SforceService;
import com.sforce.soap.enterprise.Soap;
import com.sforce.soap.enterprise.UnexpectedErrorFault;
import com.sun.xml.ws.api.message.Header;
import com.sun.xml.ws.api.message.Headers;
import com.sun.xml.ws.developer.WSBindingProvider;

import de.cosmocode.palava.core.lifecycle.Disposable;
import de.cosmocode.palava.core.lifecycle.Initializable;
import de.cosmocode.palava.core.lifecycle.LifecycleException;

/**
 * Default implementations of the {@link SalesforceService} interface.
 * 
 * @author Willi Schoenborn
 */
final class DefaultSalesforceService implements SalesforceService, Initializable, Disposable {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultSalesforceService.class);

    /**
     * The location of the wsdl file.
     */
    private final URL wsdl;
    
    /**
     * The username for the Salesforce API.
     */
    private final String username;
    
    /**
     * The corresponding password.
     */
    private final String password;
    
    /**
     * The security token.
     */
    private final String securityToken;
    
    /**
     * Connection timeout.
     */
    private final long connectionTimeout;
    
    /**
     * The unit of {@link DefaultSalesforceService#connectionTimeout}.
     */
    private final TimeUnit connectionTimeoutUnit;
    
    private Soap soap;
    
    @Inject
    public DefaultSalesforceService(
        @Named(SalesforceConfig.WSDL) URL wsdl,
        @Named(SalesforceConfig.USERNAME) String username,
        @Named(SalesforceConfig.PASSWORD) String password,
        @Named(SalesforceConfig.SECURITY_TOKEN) String securityToken,
        @Named(SalesforceConfig.CONNECTION_TIMEOUT) long connectionTimeout,
        @Named(SalesforceConfig.CONNECTION_TIMEOUT_UNIT) TimeUnit connectionTimeoutUnit) {
        
        this.wsdl = Preconditions.checkNotNull(wsdl, "Wsdl");
        this.username = Preconditions.checkNotNull(username, "Username");
        this.password = Preconditions.checkNotNull(password, "Password");
        this.securityToken = Preconditions.checkNotNull(securityToken, "SecurityToken");
        this.connectionTimeout = Preconditions.checkNotNull(connectionTimeout, "ConnectionTimeout");
        this.connectionTimeoutUnit = Preconditions.checkNotNull(connectionTimeoutUnit, "ConnectionTimeoutUnit");
    }

    @Override
    public synchronized void initialize() throws LifecycleException {
        try {
            soap = connect();
        } catch (SalesforceException e) {
            throw new LifecycleException(e);
        }
    }
    
    @Override
    public Soap connect() throws SalesforceException {
        LOG.info("Connecting to Salesforce");
        final SforceService service = new SforceService(wsdl, Salesforce.SERVICE_NAME);
        final Soap endpoint = service.getSoap();
        
        final WSBindingProvider provider = WSBindingProvider.class.cast(endpoint);
        final Object address = provider.getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
        LOG.debug("Connecting to {}", address);
        
        LOG.debug("Setting connection timeout to {} {}", 
            connectionTimeout, connectionTimeoutUnit.name().toLowerCase());
        final int timeout = (int) connectionTimeoutUnit.toMillis(connectionTimeout);
        provider.getRequestContext().put("com.sun.xml.ws.request.timeout", timeout);
        
        LOG.debug("Enabling Gzip compression");
        provider.getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, Maps.newHashMap(Salesforce.HTTP_HEADERS));
        
        try {
            LOG.debug("Attempt to login using {}/***", username);
            final LoginResult result = endpoint.login(username, password + securityToken);
            
            final String serverUrl = result.getServerUrl();
            LOG.debug("Setting endpoint to {}", serverUrl);
            provider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, serverUrl);
            
            final SessionHeader sessionHeader = new SessionHeader();
            LOG.debug("Creating new SessionHeader with Id: {}", result.getSessionId());
            sessionHeader.setSessionId(result.getSessionId());
            
            final Header header = Headers.create(Salesforce.CONTEXT, sessionHeader);
            LOG.debug("Setting Header {} in provider {}", header, provider);
            provider.setOutboundHeaders(header);
            
            if (LOG.isDebugEnabled()) {
                LOG.debug("Logged in as user with ID {}", result.getUserId());
                
                final GetUserInfoResult info = result.getUserInfo();
                LOG.debug("Username: {} ({})", info.getUserFullName(), info.getUserName());
                LOG.debug("Email: {}", info.getUserEmail());
                LOG.debug("Organization: {} [{}]", info.getOrganizationName(), info.getOrganizationId());
                LOG.debug("Language: {} / Locale: {}", info.getUserLanguage(), info.getUserLocale());
            }
        } catch (InvalidIdFault e) {
            throw new SalesforceException("Unable to log into Salesforce", e);
        } catch (LoginFault e) {
            throw new SalesforceException("Unable to log into Salesforce", e);
        } catch (UnexpectedErrorFault e) {
            throw new SalesforceException("Unable to log into Salesforce", e);
        }
        return endpoint;
    }
    
    @Override
    public synchronized Soap get() {
        try {
            soap.getServerTimestamp();
            return soap;
        } catch (UnexpectedErrorFault e) {
            LOG.warn("Soap connection went invalid.", e);
            soap = connect();
            return get();
        }
    }
    
    @Override
    public synchronized void dispose() throws LifecycleException {
        try {
            LOG.info("Logging out from Salesforce");
            soap.logout();
        } catch (UnexpectedErrorFault e) {
            LOG.error("Logout from Salesforce failed", e);
        }
    }

}
