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

import org.easymock.EasyMock;
import org.junit.Test;

import com.google.common.base.Function;
import com.sforce.soap.enterprise.sobject.SObject;

import de.cosmocode.junit.UnitProvider;
import de.cosmocode.palava.model.base.EntityBase;

/**
 * Abstract test suite for {@link SyncService}s.
 *
 * @author Willi Schoenborn
 */
public abstract class AbstractSyncServiceTest implements UnitProvider<SyncService> {

    /**
     * SyncService SyncService#execute(SaSyncServiceth a null task.
     */
    @Test(expected = NullPointerException.class)
    public void executeNull() {
        unit().execute(null);
    }

    /**
     * Tests {@link SyncService#execute(SaSyncServiceesforceSyncTask, SyncTask...)} with a null first task.
     */
    @Test(expected = NullPointerException.class)
    public void executeTasksFirstNull() {
        final SyncTask second = EasyMock.createMock("second", SyncTask.class);
        final SyncTask[] rest = new SyncTask[0];
        unit().execute(null, second, rest);
    }

    /**
     * Tests {@link SyncService#execute(SyncTask, SyncTask, SyncTask...)}
     * with a null second task.
     */
    @Test(expected = NullPointerException.class)
    public void executeTasksSecondNull() {
        final SyncTask first = EasyMock.createMock("first", SyncTask.class);
        final SyncTask[] rest = new SyncTask[0];
        unit().execute(first, null, rest);
    }
    
    /**
     * Tests {@link SyncService#execute(SyncTask, SyncTask, SyncTask...)} with a null rest.
     */
    @Test(expected = NullPointerException.class)
    public void executeTasksRestNull() {
        final SyncTask first = EasyMock.createMock("first", SyncTask.class);
        final SyncTask second = EasyMock.createMock("second", SyncTask.class);
        final SyncTask[] rest = null;
        unit().execute(first, second, rest);
    }

    /**
     * Tests {@link SyncService#execute(SyncTask, SyncTask, SyncTask...)} with a null first and second task.
     */
    @Test(expected = NullPointerException.class)
    public void executeTasksFirstSecondNull() {
        final SyncTask[] rest = new SyncTask[0];
        unit().execute(null, null, rest);
    }
    
    /**
     * Tests {@link SyncService#execute(SyncTask, SyncTask, SyncTask...)} with a null first task and null rest.
     */
    @Test(expected = NullPointerException.class)
    public void executeTasksFirstRestNull() {
        final SyncTask second = EasyMock.createMock("second", SyncTask.class);
        final SyncTask[] rest = null;
        unit().execute(null, second, rest);
    }
    
    /**
     * Tests {@link SyncService#execute(SyncTask, SyncTask, SyncTask...)} with a null second task and null rest.
     */
    @Test(expected = NullPointerException.class)
    public void executeTasksSecondRestNull() {
        final SyncTask first = EasyMock.createMock("first", SyncTask.class);
        final SyncTask[] rest = null;
        unit().execute(first, null, rest);
    }
    
    /**
     * Tests {@link SyncService#sync(EntityBase, Function)} with a null object.
     */
    @Test(expected = NullPointerException.class)
    public void syncObjectNull() {
        @SuppressWarnings("unchecked")
        final Function<EntityBase, SObject> function = EasyMock.createMock("function", Function.class);
        EasyMock.replay(function);
        unit().sync(null, function);
    }

    /**
     * Tests {@link SyncService#sync(EntityBase, Function)} with a null function.
     */
    @Test(expected = NullPointerException.class)
    public void syncFunctionNull() {
        final EntityBase object = EasyMock.createMock("object", EntityBase.class);
        EasyMock.replay(object);
        unit().sync(object, null);
    }

    /**
     * Tests {@link SyncService#sync(EntityBase, Function)} with a null object and a null function.
     */
    @Test(expected = NullPointerException.class)
    public void syncObjectFunctionNull() {
        unit().sync(null, null);
    }

}
