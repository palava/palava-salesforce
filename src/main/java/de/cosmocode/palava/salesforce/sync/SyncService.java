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

import de.cosmocode.palava.model.base.EntityBase;

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
     * @param <T> generic entity type
     * @param task the task being executed
     * @throws NullPointerException if task is null
     */
    <T extends EntityBase> void execute(SalesforceSyncTask<T> task);
    
    /**
     * Executes the given tasks sequentially.
     * 
     * @param first the first task
     * @param second the second task
     * @param rest the rest
     * @throws NullPointerException if first, second or rest is null
     */
    void execute(SalesforceSyncTask<?> first, SalesforceSyncTask<?> second, SalesforceSyncTask<?>... rest);
    
}
