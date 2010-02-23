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

import com.google.inject.Provider;
import com.sforce.soap.enterprise.Soap;

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
     * for future requests. If there is no connection
     * in progress or the cached version went invalid,
     * a new one will replace it.
     * 
     * @return a Soap instance
     */
    Soap get();
    
}
