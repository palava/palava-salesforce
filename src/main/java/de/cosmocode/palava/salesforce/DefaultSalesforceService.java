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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.sforce.soap.enterprise.DeleteResult;
import com.sforce.soap.enterprise.Error;
import com.sforce.soap.enterprise.GetUserInfoResult;
import com.sforce.soap.enterprise.InvalidFieldFault;
import com.sforce.soap.enterprise.InvalidIdFault;
import com.sforce.soap.enterprise.InvalidQueryLocatorFault;
import com.sforce.soap.enterprise.InvalidSObjectFault;
import com.sforce.soap.enterprise.LoginFault;
import com.sforce.soap.enterprise.LoginResult;
import com.sforce.soap.enterprise.MalformedQueryFault;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.SaveResult;
import com.sforce.soap.enterprise.SessionHeader;
import com.sforce.soap.enterprise.SforceService;
import com.sforce.soap.enterprise.Soap;
import com.sforce.soap.enterprise.UnexpectedErrorFault;
import com.sforce.soap.enterprise.UpsertResult;
import com.sforce.soap.enterprise.sobject.SObject;
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
    
    private int maxRetries = 1;
    
    private boolean failOnBoot = true;
    
    private Soap soap;
    
    @Inject
    public DefaultSalesforceService(
        @Named(SalesforceServiceConfig.WSDL) URL wsdl,
        @Named(SalesforceServiceConfig.USERNAME) String username,
        @Named(SalesforceServiceConfig.PASSWORD) String password,
        @Named(SalesforceServiceConfig.SECURITY_TOKEN) String securityToken,
        @Named(SalesforceServiceConfig.CONNECTION_TIMEOUT) long connectionTimeout,
        @Named(SalesforceServiceConfig.CONNECTION_TIMEOUT_UNIT) TimeUnit connectionTimeoutUnit) {
        
        this.wsdl = Preconditions.checkNotNull(wsdl, "Wsdl");
        this.username = Preconditions.checkNotNull(username, "Username");
        this.password = Preconditions.checkNotNull(password, "Password");
        this.securityToken = Preconditions.checkNotNull(securityToken, "SecurityToken");
        this.connectionTimeout = Preconditions.checkNotNull(connectionTimeout, "ConnectionTimeout");
        this.connectionTimeoutUnit = Preconditions.checkNotNull(connectionTimeoutUnit, "ConnectionTimeoutUnit");
    }
    
    @Inject(optional = true)
    void setMaxRetries(@Named(SalesforceServiceConfig.MAX_RETRIES) int maxRetries) {
        this.maxRetries = maxRetries;
    }
    
    @Inject(optional = true)
    void setFailOnBoot(@Named(SalesforceServiceConfig.FAIL_ON_BOOT) boolean failOnBoot) {
        this.failOnBoot = failOnBoot;
    }

    @Override
    public void initialize() throws LifecycleException {
        try {
            soap = connect();
        } catch (SalesforceException e) {
            if (failOnBoot) {
                throw new LifecycleException(e);
            } else {
                LOG.warn("Unable to connect to salesforce", e);
            }
        }
    }
    
    @Override
    public Soap connect() throws SalesforceException {
        LOG.info("Connecting to Salesforce using {}", wsdl.toExternalForm());
        final SforceService service = new SforceService(wsdl, Salesforce.SERVICE_NAME);
        final Soap endpoint = service.getSoap();
        
        assert endpoint instanceof WSBindingProvider : 
            String.format("%s should be an instance of %s", endpoint, WSBindingProvider.class);
        
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
        return soap;
    }
    
    @Override
    public synchronized Soap reconnect() {
        soap = connect();
        return soap;
    }
    
    @Override
    public List<SaveResult> create(List<SObject> objects) {
        return create(objects, 0);
    }
    
    private List<SaveResult> create(List<SObject> objects, int retries) {
        Preconditions.checkNotNull(objects, "Objects");
        if (objects.isEmpty()) throw new IllegalArgumentException("Objects must not be empty");
        final List<SaveResult> results;
        
        try {
            results = get().create(objects);
        } catch (InvalidFieldFault e) {
            throw new SalesforceException(e);
        } catch (InvalidIdFault e) {
            throw new SalesforceException(e);
        } catch (InvalidSObjectFault e) {
            throw new SalesforceException(e);
        } catch (UnexpectedErrorFault e) {
            if (retries < maxRetries) {
                reconnect();
                return create(objects, retries + 1);
            } else {
                throw new SalesforceException(e);
            }
        }
        
        if (Iterables.all(results, Salesforce.SAVE_SUCCESS)) {
            final String name = objects.get(0).getClass().getSimpleName();
            LOG.info("Successfully created {} {}(s)", results.size(), name);
            return results;
        } else {
            final Iterable<SaveResult> failures = Iterables.filter(results, Salesforce.SAVE_FAILURE);
            final List<Error> errors = Lists.newArrayList();
            for (SaveResult failure : failures) {
                errors.addAll(failure.getErrors());
            }
            throw new SalesforceException(errors);
        }
    }

    @Override
    public SaveResult create(SObject object) {
        return create(ImmutableList.of(object)).get(0);
    }

    @Override
    public List<SaveResult> update(List<SObject> objects) {
        return update(objects, 0);
    }
    
    private List<SaveResult> update(List<SObject> objects, int retries) {
        Preconditions.checkNotNull(objects, "Objects");
        if (objects.isEmpty()) throw new IllegalArgumentException("Objects must not be empty");
        final List<SaveResult> results;
        
        try {
            results = get().update(objects);
        } catch (InvalidFieldFault e) {
            throw new SalesforceException(e);
        } catch (InvalidIdFault e) {
            throw new SalesforceException(e);
        } catch (InvalidSObjectFault e) {
            throw new SalesforceException(e);
        } catch (UnexpectedErrorFault e) {
            if (retries < maxRetries) {
                reconnect();
                return update(objects, retries + 1);
            } else {
                throw new SalesforceException(e);
            }
        }
        
        if (Iterables.all(results, Salesforce.SAVE_SUCCESS)) {
            final String name = objects.get(0).getClass().getSimpleName();
            LOG.info("Successfully updated {} {}(s)", results.size(), name);
            return results;
        } else {
            final Iterable<SaveResult> failures = Iterables.filter(results, Salesforce.SAVE_FAILURE);
            final List<Error> errors = Lists.newArrayList();
            for (SaveResult failure : failures) {
                errors.addAll(failure.getErrors());
            }
            throw new SalesforceException(errors);
        }
    }

    @Override
    public SaveResult update(SObject object) {
        return update(ImmutableList.of(object)).get(0);

    }

    @Override
    public List<UpsertResult> upsert(List<SObject> objects) {
        return upsert(objects, 0);
    }
    
    private List<UpsertResult> upsert(List<SObject> objects, int retries) {
        Preconditions.checkNotNull(objects, "Objects");
        if (objects.isEmpty()) throw new IllegalArgumentException("Objects must not be empty");
        final List<UpsertResult> results;
        
        try {
            results = get().upsert(Salesforce.EXTERNAL_IDENTIFIER, objects);
        } catch (InvalidFieldFault e) {
            throw new SalesforceException(e);
        } catch (InvalidIdFault e) {
            throw new SalesforceException(e);
        } catch (InvalidSObjectFault e) {
            throw new SalesforceException(e);
        } catch (UnexpectedErrorFault e) {
            if (retries < maxRetries) {
                reconnect();
                return upsert(objects, retries + 1);
            } else {
                throw new SalesforceException(e);
            }
        }

        if (Iterables.all(results, Salesforce.UPSERT_SUCCESS)) {
            final String name = objects.get(0).getClass().getSimpleName();
            final int created = Iterables.size(Iterables.filter(results, Salesforce.CREATED_VIA_UPSERT));
            LOG.info("Successfully updated {} and created {} {}(s)", new Object[] {
                results.size() - created,
                created,
                name
            });
            return results;
        } else {
            final Iterable<UpsertResult> failures = Iterables.filter(results, Salesforce.UPSERT_FAILURE);
            final List<Error> errors = Lists.newArrayList();
            for (UpsertResult failure : failures) {
                errors.addAll(failure.getErrors());
            }
            throw new SalesforceException(errors);
        }
    }

    @Override
    public UpsertResult upsert(SObject object) {
        return upsert(ImmutableList.of(object)).get(0);
    }

    @Override
    public List<DeleteResult> delete(List<SObject> objects) {
        Preconditions.checkNotNull(objects, "Objects");
        if (objects.isEmpty()) throw new IllegalArgumentException("Objects must not be empty");
        final List<String> identifiers = Lists.newArrayList(Iterables.transform(objects, Salesforce.ID_FUNCTION));
        return delete(identifiers.toArray(new String[identifiers.size()]));
    }
    
    @Override
    public List<DeleteResult> delete(String[] identifiers) {
        return delete(identifiers, 0);
    }
    
    private List<DeleteResult> delete(String[] identifiers, int retries) {
        Preconditions.checkNotNull(identifiers, "Identifiers");
        Preconditions.checkArgument(identifiers.length > 0, "Identifiers must not be empty");
        
        final List<DeleteResult> results;
        
        try {
            results = get().delete(Arrays.asList(identifiers));
        } catch (UnexpectedErrorFault e) {
            if (retries < maxRetries) {
                reconnect();
                return delete(identifiers, retries + 1);
            } else {
                throw new SalesforceException(e);
            }
        }

        if (Iterables.all(results, Salesforce.DELETE_SUCCESS)) {
            LOG.info("Successfully deleted {} objects", results.size());
            return results;
        } else {
            final Iterable<DeleteResult> failures = Iterables.filter(results, Salesforce.DELETE_FAILURE);
            final List<Error> errors = Lists.newArrayList();
            for (DeleteResult failure : failures) {
                errors.addAll(failure.getErrors());
            }
            throw new SalesforceException(errors);
        }
    }

    @Override
    public DeleteResult delete(SObject object) {
        return delete(ImmutableList.of(object)).get(0);
    }
    
    @Override
    public DeleteResult delete(String identifier) {
        Preconditions.checkNotNull(identifier, "Identifier");
        return delete(new String[] {identifier}).get(0);
    }
    
    @Override
    public QueryResult execute(String query) {
        return execute(query, 0);
    }
    
    private QueryResult execute(String query, int retries) {
        Preconditions.checkNotNull(query, "Query");
        Preconditions.checkArgument(StringUtils.isNotBlank(query), "Query must not be blank");
        
        try {
            LOG.debug("Executing query '{}' against Salesforce", query);
            return get().query(query);
        } catch (InvalidFieldFault e) {
            throw new SalesforceException(e);
        } catch (InvalidIdFault e) {
            throw new SalesforceException(e);
        } catch (InvalidQueryLocatorFault e) {
            throw new SalesforceException(e);
        } catch (InvalidSObjectFault e) {
            throw new SalesforceException(e);
        } catch (MalformedQueryFault e) {
            throw new SalesforceException(e);
        } catch (UnexpectedErrorFault e) {
            if (retries < maxRetries) {
                reconnect();
                return execute(query, retries + 1);
            } else {
                throw new SalesforceException(e);
            }
        }
    }
    
    @Override
    public void dispose() throws LifecycleException {
        try {
            LOG.info("Logging out from Salesforce");
            soap.logout();
        } catch (UnexpectedErrorFault e) {
            LOG.error("Logout from Salesforce failed", e);
        }
    }

}
