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

/**
 * Static constant holder class for salesforce config key names.
 *
 * @author Willi Schoenborn
 */
public final class SalesforceServiceConfig {

    public static final String PREFIX = "salesforce.";
    
    public static final String WSDL = PREFIX + "wsdl";
    
    public static final String USERNAME = PREFIX + "username";
    
    public static final String PASSWORD = PREFIX + "password";
    
    public static final String SECURITY_TOKEN = PREFIX + "securityToken";
    
    public static final String CONNECTION_TIMEOUT = PREFIX + "connectionTimeout";
    
    public static final String CONNECTION_TIMEOUT_UNIT = PREFIX + "connectionTimeoutUnit";
    
    public static final String MAX_RETRIES = PREFIX + "maxRetries";
    
    public static final String FAIL_ON_BOOT = PREFIX + "failOnBoot";
    
    private SalesforceServiceConfig() {
        
    }

}
