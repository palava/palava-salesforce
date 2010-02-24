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

import org.junit.Assert;
import org.junit.Test;

import com.sforce.soap.enterprise.Soap;
import com.sforce.soap.enterprise.UnexpectedErrorFault;

import de.cosmocode.junit.UnitProvider;

/**
 * Abstract test suite for {@link SalesforceService}s.
 *
 * @author Willi Schoenborn
 */
public abstract class AbstractSalesforceServiceTest implements UnitProvider<SalesforceService> {

    /**
     * Tests {@link SalesforceService#connect()}.
     */
    @Test
    public void connect() {
        unit().connect();
    }
    
    /**
     * Tests {@link SalesforceService#get()}.
     */
    @Test
    public void get() {
        unit().get();
    }
    
    /**
     * Tests {@link SalesforceService#get()} to make sure
     * the soap instance gets cached.
     */
    @Test
    public void getSame() {
        final SalesforceService unit = unit();
        Assert.assertSame(unit.get(), unit.get());
    }
    
    /**
     * Tests reconnect functionality of {@link SalesforceService#get()}.
     * 
     * @throws UnexpectedErrorFault if logout failed
     */
    @Test
    public void getReconnect() throws UnexpectedErrorFault {
        final SalesforceService unit = unit();
        final Soap soap = unit.get();
        soap.logout();
        Assert.assertNotSame(soap, unit.get());
    }
    
    // TODO add test cases

}
