/**
 * Copyright 2010 CosmoCode GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
    
    public static final String EXTERNAL_IDENTIFIER = PREFIX + "externalIdentifier";
    
    public static final String MAX_RETRIES = PREFIX + "maxRetries";
    
    public static final String FAIL_ON_BOOT = PREFIX + "failOnBoot";
    
    private SalesforceServiceConfig() {
        
    }

}
