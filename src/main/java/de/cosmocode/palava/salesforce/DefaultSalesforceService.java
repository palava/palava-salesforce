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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.sforce.soap.enterprise.Soap;

/**
 * Default implementations of the {@link SalesforceService} interface.
 *
 * @author Willi Schoenborn
 */
final class DefaultSalesforceService implements SalesforceService {

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
    public Soap connect() throws SalesforceException {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Soap get() {
        throw new UnsupportedOperationException();
    }

}
