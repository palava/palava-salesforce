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
