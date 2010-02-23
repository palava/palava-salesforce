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

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sforce.soap.enterprise.DeleteResult;
import com.sforce.soap.enterprise.Error;
import com.sforce.soap.enterprise.InvalidFieldFault;
import com.sforce.soap.enterprise.InvalidIdFault;
import com.sforce.soap.enterprise.InvalidSObjectFault;
import com.sforce.soap.enterprise.SaveResult;
import com.sforce.soap.enterprise.Soap;
import com.sforce.soap.enterprise.UnexpectedErrorFault;
import com.sforce.soap.enterprise.UpsertResult;
import com.sforce.soap.enterprise.sobject.SObject;

/**
 * Default implementation of the {@link SalesforceBatchService} interface
 * using the Saleforce.com API.
 *
 * @author Willi Schoenborn
 */
public final class SoapBatchService implements SalesforceBatchService {

    private static final Logger LOG = LoggerFactory.getLogger(SoapBatchService.class);

    private Provider<Soap> provider;
    
    @Inject
    public SoapBatchService(Provider<Soap> provider) {
        this.provider = Preconditions.checkNotNull(provider, "Provider");
    }
    
    @Override
    public void create(List<SObject> objects) {
        Preconditions.checkNotNull(objects, "Objects");
        if (objects.isEmpty()) throw new IllegalArgumentException("Objects must not be empty");
        final List<SaveResult> results;
        
        try {
            results = provider.get().create(objects);
        } catch (InvalidIdFault e) {
            throw new SalesforceException(e);
        } catch (InvalidSObjectFault e) {
            throw new SalesforceException(e);
        } catch (UnexpectedErrorFault e) {
            throw new SalesforceException(e);
        }
        
        if (Iterables.all(results, Salesforce.SAVE_SUCCESS)) {
            final String name = objects.get(0).getClass().getSimpleName();
            LOG.info("Successfully created {} {}(s)", results.size(), name);
        } else {
            final Iterable<SaveResult> failures = Iterables.filter(results, Salesforce.SAVE_FAILURE);
            final List<Error> errors = Lists.newArrayList();
            for (SaveResult failure : failures) {
                errors.addAll(failure.getErrors());
            }
            throw new SalesforceException(errors);
        }
    }

    @Override
    public void create(SObject object) {
        create(ImmutableList.of(object));
    }

    @Override
    public void update(List<SObject> objects) {
        Preconditions.checkNotNull(objects, "Objects");
        if (objects.isEmpty()) throw new IllegalArgumentException("Objects must not be empty");
        final List<SaveResult> results;
        
        try {
            results = provider.get().update(objects);
        } catch (InvalidIdFault e) {
            throw new SalesforceException(e);
        } catch (InvalidSObjectFault e) {
            throw new SalesforceException(e);
        } catch (UnexpectedErrorFault e) {
            throw new SalesforceException(e);
        }
        
        if (Iterables.all(results, Salesforce.SAVE_SUCCESS)) {
            final String name = objects.get(0).getClass().getSimpleName();
            LOG.info("Successfully updated {} {}(s)", results.size(), name);
        } else {
            final Iterable<SaveResult> failures = Iterables.filter(results, Salesforce.SAVE_FAILURE);
            final List<Error> errors = Lists.newArrayList();
            for (SaveResult failure : failures) {
                errors.addAll(failure.getErrors());
            }
            throw new SalesforceException(errors);
        }
    }

    @Override
    public void update(SObject object) {
        update(ImmutableList.of(object));

    }

    @Override
    public void upsert(List<SObject> objects) {
        Preconditions.checkNotNull(objects, "Objects");
        if (objects.isEmpty()) throw new IllegalArgumentException("Objects must not be empty");
        final List<UpsertResult> results;
        
        try {
            results = provider.get().upsert(Salesforce.EXTERNAL_IDENTIFIER, objects);
        } catch (InvalidFieldFault e) {
            throw new SalesforceException(e);
        } catch (InvalidIdFault e) {
            throw new SalesforceException(e);
        } catch (InvalidSObjectFault e) {
            throw new SalesforceException(e);
        } catch (UnexpectedErrorFault e) {
            throw new SalesforceException(e);
        }

        if (Iterables.all(results, Salesforce.UPSERT_SUCCESS)) {
            final String name = objects.get(0).getClass().getSimpleName();
            final int created = Iterables.size(Iterables.filter(results, Salesforce.CREATED_VIA_UPSERT));
            LOG.info("Successfully updated {} and created {} {}(s)", new Object[] {
                results.size() - created,
                created,
                name
            });
        } else {
            final Iterable<UpsertResult> failures = Iterables.filter(results, Salesforce.UPSERT_FAILURE);
            final List<Error> errors = Lists.newArrayList();
            for (UpsertResult failure : failures) {
                errors.addAll(failure.getErrors());
            }
            throw new SalesforceException(errors);
        }
    }

    @Override
    public void upsert(SObject object) {
        upsert(ImmutableList.of(object));
    }

    @Override
    public void delete(List<SObject> objects) {
        Preconditions.checkNotNull(objects, "Objects");
        if (objects.isEmpty()) throw new IllegalArgumentException("Objects must not be empty");
        final List<String> identifiers = Lists.newArrayList(Iterables.transform(objects, Salesforce.ID_FUNCTION));
        delete(identifiers.toArray(new String[identifiers.size()]));
    }
    
    @Override
    public void delete(String[] identifiers) {
        final List<DeleteResult> results;
        
        try {
            results = provider.get().delete(Arrays.asList(identifiers));
        } catch (UnexpectedErrorFault e) {
            throw new SalesforceException(e);
        }

        if (Iterables.all(results, Salesforce.DELETE_SUCCESS)) {
            LOG.info("Successfully deleted {} objects", results.size());
        } else {
            final Iterable<DeleteResult> failures = Iterables.filter(results, Salesforce.DELETE_FAILURE);
            final List<Error> errors = Lists.newArrayList();
            for (DeleteResult failure : failures) {
                errors.addAll(failure.getErrors());
            }
            throw new SalesforceException(errors);
        }
        
    }

    @Override
    public void delete(SObject object) {
        delete(ImmutableList.of(object));
    }
    
    @Override
    public void delete(String identifier) {
        delete(new String[] {identifier});
    }

}
