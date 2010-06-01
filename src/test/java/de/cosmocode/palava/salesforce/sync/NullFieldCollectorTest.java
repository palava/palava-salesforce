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
