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

import com.google.common.base.Function;
import com.sforce.soap.enterprise.sobject.SObject;

import de.cosmocode.palava.model.base.EntityBase;
import de.cosmocode.palava.salesforce.SalesforceException;


/**
 * A service which executes sync tasks. Tasks will be executed
 * in background threads or thread pools.
 * 
 * @author Willi Schoenborn
 */
public interface SyncService {

    /**
     * Executes a sync task.
     * 
     * @param task the task being executed
     * @throws NullPointerException if task is null
     */
    void execute(SyncTask task);
    
    /**
     * Executes the given tasks sequentially.
     * 
     * @param first the first task
     * @param second the second task
     * @param rest the rest
     * @throws NullPointerException if first, second or rest is null
     */
    void execute(SyncTask first, SyncTask second, SyncTask... rest);
    
    /**
     * Synchronize a generic object with salesforce.
     * 
     * @param <S> the generic entity type
     * @param <T> the generic salesforce object type
     * @param from the source entity
     * @param function a copy function which creates a salesforce copy of the source entity
     * @throws NullPointerException if from or function is null
     */
    <S extends EntityBase, T extends SObject> void sync(S from, Function<S, T> function);
    
    /**
     * Synchronizes the complete database.
     * 
     * <p>
     *   Using this method is equivalent to {@link SyncService#complete(boolean)} using
     *   false.
     * </p>
     * 
     * @throws SalesforceException if sync failed
     */
    void complete();
    
    /**
     * Synchronizes the complete database.
     * 
     * @param failFast if true, an exception is thrown as soon
     *        as an error occured, which will suppress following syncs
     * @throws SalesforceException if sync failed
     */
    void complete(boolean failFast);
    
}
