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

import java.util.List;

import com.google.inject.Provider;
import com.sforce.soap.enterprise.DeleteResult;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.SaveResult;
import com.sforce.soap.enterprise.Soap;
import com.sforce.soap.enterprise.UpsertResult;
import com.sforce.soap.enterprise.sobject.SObject;

/**
 * A {@link SalesforceService} handles soap connections to Salesforce.com.
 *
 * @author Willi Schoenborn
 */
public interface SalesforceService extends Provider<Soap> {

    /**
     * Connects to the Salesforce SOAP API.
     * <p>
     *   <strong>Note</strong>: If you want to reuse
     *   an already connected soap instance, use {@link SalesforceService#get()}.
     * </p>
     * 
     * @return a {@link Soap} used to manage API calls.
     * @throws SalesforceException if connect failed
     */
    Soap connect() throws SalesforceException;
    
    /**
     * Provides a Soap instance which will be cached
     * for future requests. <del>If there is no connection
     * in progress or the cached version went invalid,
     * a new one will replace it.</del>
     * 
     * <p>
     *   <strong>Update</strong>: This method no longer
     *   checks for valid connections.
     * </p>
     * 
     * @return a Soap instance
     */
    Soap get();

    /**
     * Reconnects the cached soap instance which is provided
     * by {@link SalesforceService#get()} and returns it.
     * 
     * @return a Soap instance
     */
    Soap reconnect();

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
     * @throws IllegalArgumentException if query is blank
     */
    QueryResult execute(String query);
    
}
