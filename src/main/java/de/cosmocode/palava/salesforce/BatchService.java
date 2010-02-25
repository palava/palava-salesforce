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

import com.sforce.soap.enterprise.DeleteResult;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.SaveResult;
import com.sforce.soap.enterprise.UpsertResult;
import com.sforce.soap.enterprise.sobject.SObject;

import de.cosmocode.palava.core.Service;

/**
 * A {@link Service} which allows batch updates
 * with Salesforce.
 *
 * @author Willi Schoenborn
 */
public interface BatchService extends Service {

    /**
     * Creates a set of objects in Salesforce.
     * 
     * @param objects the objects being created
     * @return a list of {@link SaveResult}s
     * @throws NullPointerException if objects is null
     * @throws IllegalArgumentException if objects is empty
     * @throws SalesforceException if creation failed
     */
    List<SaveResult> create(List<SObject> objects);
    
    /**
     * Creates an object in Salesforce.
     * 
     * @param object the object being created
     * @return a {@link SaveResult}
     * @throws NullPointerException if objects is null
     * @throws SalesforceException if creation failed
     */
    SaveResult create(SObject object);

    /**
     * Updates a set of objects in Salesforce.
     * 
     * @param objects the objects being updated
     * @return a list of {@link SaveResult}s
     * @throws NullPointerException if objects is null
     * @throws IllegalArgumentException if objects is empty
     * @throws SalesforceException if update failed
     */
    List<SaveResult> update(List<SObject> objects);

    /**
     * Updates an object in Salesforce.
     * 
     * @param object the object being updated
     * @return a {@link SaveResult}
     * @throws NullPointerException if objects is null
     * @throws SalesforceException if update failed
     */
    SaveResult update(SObject object);

    /**
     * Updates/Inserts a set of objects in Salesforce.
     * 
     * @param objects the objects being updated/inserted
     * @return a list of {@link UpsertResult}s
     * @throws NullPointerException if objects is null
     * @throws IllegalArgumentException if objects is empty
     * @throws SalesforceException if update/insertion failed
     */
    List<UpsertResult> upsert(List<SObject> objects);

    /**
     * Updates/Inserts an object in Salesforce.
     * 
     * @param object the object being updated/inserted
     * @return an {@link UpsertResult}
     * @throws NullPointerException if objects is null
     * @throws SalesforceException if update/insertion failed
     */
    UpsertResult upsert(SObject object);

    /**
     * Deletes a set of objects in Salesforce.
     * 
     * @param objects the objects being deleted
     * @return a list of {@link DeleteResult}s
     * @throws NullPointerException if objects is null
     * @throws IllegalArgumentException if objects is empty
     * @throws SalesforceException if deletion failed
     */
    List<DeleteResult> delete(List<SObject> objects);
    
    /**
     * Delets a set of objects in Salesforce.
     * 
     * @param identifiers the object identifiers
     * @return a list of {@link DeleteResult}s
     * @throws NullPointerException if identifiers is null
     * @throws IllegalArgumentException if identifiers is empty
     * @throws SalesforceException if deletion failed
     */
    List<DeleteResult> delete(String[] identifiers);

    /**
     * Deletes an object in Salesforce.
     * 
     * @param object the object being deleted
     * @return a {@link DeleteResult}
     * @throws NullPointerException if object is null
     * @throws SalesforceException if deletion failed
     */
    DeleteResult delete(SObject object);

    /**
     * Deletes an object in Salesforce.
     * 
     * @param identifier the object's identifier being deleted
     * @return a {@link DeleteResult}
     * @throws NullPointerException if identifier is null
     * @throws SalesforceException if deletion failed
     */
    DeleteResult delete(String identifier);
    

    /**
     * Executes a query against Salesforce and returns
     * the result. All Soap/Salesforce-specific exceptions
     * will be wrapped inside a {@link SalesforceException}.
     * 
     * @param query the query string
     * @return the {@link QueryResult} for the given query
     * @throws SalesforceException if an error occurs
     * @throws NullPointerException if query is null
     */
    QueryResult execute(String query);
    
}
