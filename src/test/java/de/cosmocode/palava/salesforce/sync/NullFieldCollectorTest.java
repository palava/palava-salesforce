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

package de.cosmocode.palava.salesforce.sync;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.sforce.soap.enterprise.sobject.Account;
import com.sforce.soap.enterprise.sobject.SObject;

import de.cosmocode.junit.Asserts;
import de.cosmocode.palava.salesforce.Salesforce;

/**
 * Tests {@link NullFieldCollector}.
 *
 * @author Willi Schoenborn
 */
public final class NullFieldCollectorTest {
    
    /**
     * Tests {@link NullFieldCollector#collect(SObject)} with null.
     */
    @Test(expected = NullPointerException.class)
    public void collectNull() {
        NullFieldCollector.collect(null);
    }
    
    /**
     * Tests {@link NullFieldCollector#collect(SObject)} wth an object
     * with nullable fields but without null values.
     */
    @Test
    public void noNullFields() {
        final Account account = Salesforce.FACTORY.createAccount();
        
        account.setName(Salesforce.FACTORY.createAccountName("CosmoCode GmbH"));
        
        NullFieldCollector.addNullFields(account);
        Asserts.assertTrue("Nullfields should be empty", account.getFieldsToNull().isEmpty());
    }
    
    /**
     * Tests {@link NullFieldCollector} with an object
     * with nullable fields and null values.
     */
    @Test
    public void nullFields() {
        final Account account = Salesforce.FACTORY.createAccount();

        account.setName(Salesforce.FACTORY.createAccountName(null));
        account.setBillingStreet(Salesforce.FACTORY.createAccountBillingStreet(null));
        account.setBillingState(Salesforce.FACTORY.createAccountBillingState(null));
        
        NullFieldCollector.addNullFields(account);
        Asserts.assertFalse("Nullfields should not be empty", account.getFieldsToNull().isEmpty());
        Asserts.assertEquals(3, account.getFieldsToNull().size());
    }
    
    /**
     * Tests {@link NullFieldCollector#addNullFields(SObject)}.
     */
    @Test
    public void addNullFields() {
        final Account account = Salesforce.FACTORY.createAccount();

        account.setName(Salesforce.FACTORY.createAccountName(null));
        account.setBillingStreet(Salesforce.FACTORY.createAccountBillingStreet(null));
        account.setBillingState(Salesforce.FACTORY.createAccountBillingState(null));
        
        NullFieldCollector.addNullFields(account);
        Asserts.assertFalse("Nullfields should not be empty", account.getFieldsToNull().isEmpty());
        Assert.assertTrue("getFieldsToNull() should contain all 3 fields now", account.getFieldsToNull().containsAll(
            ImmutableSet.of("Name", "BillingStreet", "BillingState")
        ));
    }
    
}
