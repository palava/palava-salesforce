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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Iterables;
import com.sforce.soap.enterprise.Error;

/**
 * Indicating an error during salesforce communication,
 * this may include connection errors (io based),
 * constraint violations, security (authentication, authorization)
 * issues etc.
 *
 * @author Willi Schoenborn
 */
public class SalesforceException extends RuntimeException {

    private static final long serialVersionUID = -4260289657112109735L;
    
    private static final Joiner LINE_JOINER = Joiner.on("\n");
    
    private static final Function<Error, String> TO_STRING = new Function<Error, String>() {
        
        @Override
        public String apply(Error error) {
            return String.format("%s %s", error.getStatusCode(), error.getMessage());
        }
        
    };
    
    private static final Comparator<Error> COMPARATOR = new Comparator<Error>() {
        
        @Override
        public int compare(Error left, Error right) {
            return (left.getFields().equals(right.getFields()) ? 0 : 1) +
                left.getMessage().compareTo(right.getMessage()) +
                left.getStatusCode().compareTo(right.getStatusCode());
        }
        
    };
    
    private final Set<Error> errors;
    
    /**
     * Creates a new SalesforceException using the specified message.
     * 
     * @throws NullPointerException if message is null
     */
    public SalesforceException(String message) {
        super(Preconditions.checkNotNull(message, "Message"));
        this.errors = Collections.emptySet();
    }

    /**
     * Creates a new SalesforceException using the specified cause.
     * 
     * @throws NullPointerException if throwable is null
     */
    public SalesforceException(Throwable throwable) {
        super(Preconditions.checkNotNull(throwable, "Cause"));
        this.errors = Collections.emptySet();
    }

    /**
     * Creates a new SalesforceException using the specified message and cause.
     * 
     * @throws NullPointerException if message or cause is null
     */
    public SalesforceException(String message, Throwable throwable) {
        super(
            Preconditions.checkNotNull(message, "Message"), 
            Preconditions.checkNotNull(throwable, "Cause")
        );
        this.errors = Collections.emptySet();
    }

    /**
     * Creates a new SalesforceException using the specified errors.
     * 
     * @throws NullPointerException if errors is null
     */
    public SalesforceException(List<Error> errors) {
        Preconditions.checkNotNull(errors, "Errors");
        this.errors = ImmutableSortedSet.copyOf(COMPARATOR, errors);
    }

    @Override
    public String toString() {
        if (errors.isEmpty()) {
            return super.toString();
        } else if (errors.size() == 1) {
            return String.format("%s: %s",
                super.toString(),
                TO_STRING.apply(errors.iterator().next())
            );
        } else {
            return String.format("%s\n%s",
                super.toString(),
                LINE_JOINER.join(Iterables.transform(errors, TO_STRING))
            );
        }
    }

}
