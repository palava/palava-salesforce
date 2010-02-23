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

import java.util.List;

import com.sforce.soap.enterprise.sobject.SObject;

import de.cosmocode.palava.core.Service;

/**
 * A {@link Service} which allows batch updates
 * with Salesforce.
 *
 * @author Willi Schoenborn
 */
public interface SalesforceBatchService extends Service {

    /**
     * Creates a set of objects in Salesforce.
     * 
     * @param objects the objects being created
     * @throws NullPointerException if objects is null
     * @throws IllegalArgumentException if objects is empty
     * @throws SalesforceException if creation failed
     */
    void create(List<SObject> objects);
    
    /**
     * Creates an object in Salesforce.
     * 
     * @param object the object being created
     * @throws NullPointerException if objects is null
     * @throws SalesforceException if creation failed
     */
    void create(SObject object);

    /**
     * Updates a set of objects in Salesforce.
     * 
     * @param objects the objects being updated
     * @throws NullPointerException if objects is null
     * @throws IllegalArgumentException if objects is empty
     * @throws SalesforceException if update failed
     */
    void update(List<SObject> objects);

    /**
     * Updates an object in Salesforce.
     * 
     * @param object the object being updated
     * @throws NullPointerException if objects is null
     * @throws SalesforceException if update failed
     */
    void update(SObject object);

    /**
     * Updates/Inserts a set of objects in Salesforce.
     * 
     * @param objects the objects being updated/inserted
     * @throws NullPointerException if objects is null
     * @throws IllegalArgumentException if objects is empty
     * @throws SalesforceException if update/insertion failed
     */
    void upsert(List<SObject> objects);

    /**
     * Updates/Inserts an object in Salesforce.
     * 
     * @param object the object being updated/inserted
     * @throws NullPointerException if objects is null
     * @throws SalesforceException if update/insertion failed
     */
    void upsert(SObject object);

    /**
     * Deletes a set of objects in Salesforce.
     * 
     * @param objects the objects being deleted
     * @throws NullPointerException if objects is null
     * @throws IllegalArgumentException if objects is empty
     * @throws SalesforceException if deletion failed
     */
    void delete(List<SObject> objects);
    
    /**
     * Delets a set of objects in Salesforce.
     * 
     * @param identifiers the object identifiers
     * @throws NullPointerException if identifiers is null
     * @throws IllegalArgumentException if identifiers is empty
     * @throws SalesforceException if deletion failed
     */
    void delete(String[] identifiers);

    /**
     * Deletes an object in Salesforce.
     * 
     * @param object the object being deleted
     * @throws NullPointerException if object is null
     * @throws SalesforceException if deletion failed
     */
    void delete(SObject object);

    /**
     * Deletes an object in Salesforce.
     * 
     * @param identifier the object's identifier being deleted
     * @throws NullPointerException if identifier is null
     * @throws SalesforceException if deletion failed
     */
    void delete(String identifier);
    
}
