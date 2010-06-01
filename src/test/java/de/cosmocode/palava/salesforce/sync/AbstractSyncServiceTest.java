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
