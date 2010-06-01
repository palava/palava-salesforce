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

import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.sforce.soap.enterprise.sobject.SObject;

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
     * Tests {@link SalesforceService#create(List)} with a null list.
     */
    @Test(expected = NullPointerException.class)
    public void createListNull() {
        final List<SObject> list = null;
        unit().create(list);
    }
    
    /**
     * Tests {@link SalesforceService#create(List)} with an empty list.
     */
    @Test(expected = IllegalArgumentException.class)
    public void createListEmpty() {
        unit().create(Collections.<SObject>emptyList());
    }
    
    /**
     * Tests {@link SalesforceService#create(SObject)} with a null object.
     */
    @Test(expected = NullPointerException.class)
    public void createNull() {
        final SObject object = null;
        unit().create(object);
    }

    /**
     * Tests {@link SalesforceService#update(List)} with a null list.
     */
    @Test(expected = NullPointerException.class)
    public void updateListNull() {
        final List<SObject> list = null;
        unit().update(list);
    }
    
    /**
     * Tests {@link SalesforceService#update(List)} with an empty list.
     */
    @Test(expected = IllegalArgumentException.class)
    public void updateListEmpty() {
        unit().update(Collections.<SObject>emptyList());
    }

    /**
     * Tests {@link SalesforceService#update(SObject)} with a null object.
     */
    @Test(expected = NullPointerException.class)
    public void updateNull() {
        final SObject object = null;
        unit().update(object);
    }

    /**
     * Tests {@link SalesforceService#upsert(List)} with a null list.
     */
    @Test(expected = NullPointerException.class)
    public void upsertListNull() {
        final List<SObject> list = null;
        unit().upsert(list);
    }
    
    /**
     * Tests {@link SalesforceService#upsert(List)} with an empty list.
     */
    @Test(expected = IllegalArgumentException.class)
    public void upsertListEmpty() {
        unit().upsert(Collections.<SObject>emptyList());
    }

    /**
     * Tests {@link SalesforceService#upsert(SObject)} with a null object.
     */
    @Test(expected = NullPointerException.class)
    public void upsertNull() {
        final SObject object = null;
        unit().upsert(object);
    }

    /**
     * Tests {@link SalesforceService#delete(List)} with a null list.
     */
    @Test(expected = NullPointerException.class)
    public void deleteListNull() {
        final List<SObject> list = null;
        unit().delete(list);
    }
    
    /**
     * Tests {@link SalesforceService#delete(List)} with an empty list.
     */
    @Test(expected = IllegalArgumentException.class)
    public void deleteListEmpty() {
        unit().delete(Collections.<SObject>emptyList());
    }

    /**
     * Tests {@link SalesforceService#delete(String[]))} with a null array.
     */
    @Test(expected = NullPointerException.class)
    public void deleteArrayNull() {
        final String[] array = null;
        unit().delete(array);
    }

    /**
     * Tests {@link SalesforceService#delete(String[]))} with a null array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void deleteArrayEmpty() {
        unit().delete(new String[0]);
    }
    
    /**
     * Tests {@link SalesforceService#delete(SObject)} with a null list.
     */
    @Test(expected = NullPointerException.class)
    public void deleteObjectNull() {
        final SObject object = null;
        unit().delete(object);
    }

    /**
     * Tests {@link SalesforceService#delete(String)} with a null list.
     */
    @Test(expected = NullPointerException.class)
    public void deleteStringNull() {
        final String string = null;
        unit().delete(string);
    }
    
    /**
     * Tests {@link SalesforceService#execute(String)} with a null query.
     */
    @Test(expected = NullPointerException.class)
    public void executeNull() {
        unit().execute(null);
    }
    
    /**
     * Tests {@link SalesforceService#execute(String)} with an empty query.
     */
    @Test(expected = IllegalArgumentException.class)
    public void executeEmpty() {
        unit().execute("");
    }
    
    /**
     * Tests {@link SalesforceService#execute(String)} with a blank query.
     */
    @Test(expected = IllegalArgumentException.class)
    public void executeBlank() {
        unit().execute("  ");
    }

}
