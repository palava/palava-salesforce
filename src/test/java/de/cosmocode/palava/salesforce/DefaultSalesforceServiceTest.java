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
