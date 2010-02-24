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

import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.sforce.soap.enterprise.sobject.SObject;

import de.cosmocode.junit.UnitProvider;

/**
 * Tests {@link SalesforceBatchService} implementations.
 *
 * @author Willi Schoenborn
 */
public abstract class AbstractSalesforceBatchServiceTest implements UnitProvider<SalesforceBatchService> {

    /**
     * Tests {@link SalesforceBatchService#create(List)} with a null list.
     */
    @Test(expected = NullPointerException.class)
    public void createListNull() {
        final List<SObject> list = null;
        unit().create(list);
    }
    
    /**
     * Tests {@link SalesforceBatchService#create(List)} with an empty list.
     */
    @Test(expected = IllegalArgumentException.class)
    public void createListEmpty() {
        unit().create(Collections.<SObject>emptyList());
    }
    
    /**
     * Tests {@link SalesforceBatchService#create(SObject)} with a null object.
     */
    @Test(expected = NullPointerException.class)
    public void createNull() {
        final SObject object = null;
        unit().create(object);
    }

    /**
     * Tests {@link SalesforceBatchService#update(List)} with a null list.
     */
    @Test(expected = NullPointerException.class)
    public void updateListNull() {
        final List<SObject> list = null;
        unit().update(list);
    }
    
    /**
     * Tests {@link SalesforceBatchService#update(List)} with an empty list.
     */
    @Test(expected = IllegalArgumentException.class)
    public void updateListEmpty() {
        unit().update(Collections.<SObject>emptyList());
    }

    /**
     * Tests {@link SalesforceBatchService#update(SObject)} with a null object.
     */
    @Test(expected = NullPointerException.class)
    public void updateNull() {
        final SObject object = null;
        unit().update(object);
    }

    /**
     * Tests {@link SalesforceBatchService#upsert(List)} with a null list.
     */
    @Test(expected = NullPointerException.class)
    public void upsertListNull() {
        final List<SObject> list = null;
        unit().upsert(list);
    }
    
    /**
     * Tests {@link SalesforceBatchService#upsert(List)} with an empty list.
     */
    @Test(expected = IllegalArgumentException.class)
    public void upsertListEmpty() {
        unit().upsert(Collections.<SObject>emptyList());
    }

    /**
     * Tests {@link SalesforceBatchService#upsert(SObject)} with a null object.
     */
    @Test(expected = NullPointerException.class)
    public void upsertNull() {
        final SObject object = null;
        unit().upsert(object);
    }

    /**
     * Tests {@link SalesforceBatchService#delete(List)} with a null list.
     */
    @Test(expected = NullPointerException.class)
    public void deleteListNull() {
        final List<SObject> list = null;
        unit().delete(list);
    }
    
    /**
     * Tests {@link SalesforceBatchService#delete(List)} with an empty list.
     */
    @Test(expected = IllegalArgumentException.class)
    public void deleteListEmpty() {
        unit().delete(Collections.<SObject>emptyList());
    }

    /**
     * Tests {@link SalesforceBatchService#delete(String[]))} with a null array.
     */
    @Test(expected = NullPointerException.class)
    public void deleteArrayNull() {
        final String[] array = null;
        unit().delete(array);
    }

    /**
     * Tests {@link SalesforceBatchService#delete(String[]))} with a null array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void deleteArrayEmpty() {
        unit().delete(new String[0]);
    }
    
    /**
     * Tests {@link SalesforceBatchService#delete(SObject)} with a null list.
     */
    @Test(expected = NullPointerException.class)
    public void deleteObjectNull() {
        final SObject object = null;
        unit().delete(object);
    }

    /**
     * Tests {@link SalesforceBatchService#delete(String)} with a null list.
     */
    @Test(expected = NullPointerException.class)
    public void deleteStringNull() {
        final String string = null;
        unit().delete(string);
    }
    
    // TODO add missing test cases

}
