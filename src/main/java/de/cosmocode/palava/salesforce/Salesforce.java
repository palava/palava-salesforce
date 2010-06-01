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

import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.sforce.soap.enterprise.DeleteResult;
import com.sforce.soap.enterprise.SaveResult;
import com.sforce.soap.enterprise.UpsertResult;
import com.sforce.soap.enterprise.sobject.ObjectFactory;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sun.xml.bind.api.JAXBRIContext;

import de.cosmocode.commons.Calendars;

/**
 * Static utility class for salesforce related work.
 *
 * @author Willi Schoenborn
 */
public final class Salesforce {
    
    public static final int MAXIMUM_BATCH_SIZE = 200;
    
    public static final ObjectFactory FACTORY = new ObjectFactory();
    
    public static final Function<SObject, String> ID_FUNCTION = new Function<SObject, String>() {
        
        @Override
        public String apply(SObject from) {
            return from.getId();
        }
        
    };
    
    public static final Predicate<SaveResult> SAVE_SUCCESS = new Predicate<SaveResult>() {
        
        @Override
        public boolean apply(SaveResult input) {
            return input.isSuccess();
        }
        
    };
    
    public static final Predicate<SaveResult> SAVE_FAILURE = Predicates.not(SAVE_SUCCESS);
    
    public static final Predicate<UpsertResult> UPSERT_SUCCESS = new Predicate<UpsertResult>() {
        
        @Override
        public boolean apply(UpsertResult input) {
            return input.isSuccess();
        }
        
    };
    
    public static final Predicate<UpsertResult> UPSERT_FAILURE = Predicates.not(UPSERT_SUCCESS);
    
    public static final Predicate<UpsertResult> CREATED_VIA_UPSERT = new Predicate<UpsertResult>() {
        
        @Override
        public boolean apply(UpsertResult input) {
            return input.isCreated();
        }
        
    };
    
    public static final Predicate<DeleteResult> DELETE_SUCCESS = new Predicate<DeleteResult>() {
        
        @Override
        public boolean apply(DeleteResult input) {
            return input.isSuccess();
        }
        
    };
    
    public static final  Predicate<DeleteResult> DELETE_FAILURE = Predicates.not(DELETE_SUCCESS);

    public static final QName SERVICE_NAME = new QName("urn:enterprise.soap.sforce.com", "SforceService");
    
    public static final ImmutableMap<String, ImmutableList<String>> HTTP_HEADERS;
    
    static {
        final ImmutableList<String> gzip = ImmutableList.of("gzip");
        HTTP_HEADERS = ImmutableMap.of(
            "Content-Encoding", gzip,
            "Accept-Encoding", gzip
        );
    }

    public static final JAXBRIContext CONTEXT;
    
    static {
        try {
            final ClassLoader loader = Salesforce.class.getClassLoader();
            final JAXBContext context = JAXBContext.newInstance("com.sforce.soap.enterprise", loader);
            CONTEXT = JAXBRIContext.class.cast(context);
        } catch (JAXBException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
    
    public static final DatatypeFactory DATATYPE_FACTORY;
    
    static {
        try {
            DATATYPE_FACTORY = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private Salesforce() {
        
    }
    
    /**
     * Creates a {@link XMLGregorianCalendar} set to the given
     * {@link Date}.
     * 
     * @param date the target date of the new {@link XMLGregorianCalendar}
     * @return a new {@link XMLGregorianCalendar} or null if date is null
     */
    public static XMLGregorianCalendar toXMLGregorianCalendar(Date date) {
        if (date == null) {
            return null;
        } else {
            return Salesforce.DATATYPE_FACTORY.newXMLGregorianCalendar(Calendars.of(date));
        }
    }
    
}
