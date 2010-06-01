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

import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.sforce.soap.enterprise.sobject.SObject;

import de.cosmocode.commons.concurrent.Runnables;
import de.cosmocode.palava.model.base.EntityBase;
import de.cosmocode.palava.salesforce.SalesforceService;

/**
 * Abstract base implementation of the {@link SyncService} interface.
 *
 * @author Willi Schoenborn
 */
public abstract class AbstractSyncService implements SyncService {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractSyncService.class);

    /**
     * Retrieves the associated {@link SalesforceService}.
     * 
     * @since 1.0
     * @return the salesforce service
     */
    protected abstract SalesforceService getService();
    
    /**
     * Retrieves the associated {@link ExecutorService}.
     * 
     * @since 1.0
     * @return the executor
     */
    protected abstract ExecutorService getExecutor();
    
    @Override
    public void execute(SyncTask task) {
        Preconditions.checkNotNull(task, "Task");
        LOG.trace("Executing {}", task);
        getExecutor().execute(task);
    }

    @Override
    public void execute(SyncTask first, SyncTask second, SyncTask... rest) {
        final Runnable task = Runnables.chain(first, second, rest);
        LOG.trace("Executing {}", task);
        getExecutor().execute(task);
    }

    @Override
    public <S extends EntityBase, T extends SObject> void sync(final S from, final Function<S, T> function) {
        Preconditions.checkNotNull(from, "From");
        Preconditions.checkNotNull(function, "Function");
        
        execute(new SyncTask() {
            
            @Override
            public void run() {
                getService().upsert(function.apply(from));
            }
            
        });
    }
    
    @Override
    public void complete() {
        complete(false);
    }
    
}
