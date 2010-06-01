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

import java.util.concurrent.TimeUnit;

import org.junit.After;

/**
 * Tests {@link DefaultSalesforceService}.
 *
 * @author Willi Schoenborn
 */
public final class DefaultSalesforceServiceTest extends AbstractSalesforceServiceTest {

    private DefaultSalesforceService unit;
    
    @Override
    public SalesforceService unit() {
        unit = new DefaultSalesforceService(
            Class.class.getResource("/enterprise.wsdl"),
            "sarnowski@cosmocode.de",
            "5ucco2Mela?",
            "X7jbpKwXL6oSLeg6sS5tWYAE",
            30, TimeUnit.MINUTES
        );
        
        unit.initialize();
        return unit;
    }
    
    /**
     * Dispose the current unit after each test.
     */
    @After
    public void after() {
        if (unit == null) {
            return;
        } else {
            unit.dispose();
            unit = null;
        }
    }

}
