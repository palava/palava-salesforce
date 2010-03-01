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

import junit.framework.Assert;

import org.apache.commons.validator.EmailValidator;
import org.junit.Test;

import com.google.common.base.Function;
import com.sforce.soap.enterprise.sobject.Contact;

import de.cosmocode.junit.UnitProvider;
import de.cosmocode.palava.model.business.ContactBase;

/**
 * Abstract test for contact copy functions.
 *
 * @author Willi Schoenborn
 */
public abstract class AbstractContactCopyFunctionTest implements UnitProvider<Function<ContactBase, Contact>> {

    /**
     * Tests {@link EmailValidator} with an email containing an umlaut.
     */
    @Test
    public void umlautInEmail() {
        Assert.assertFalse(EmailValidator.getInstance().isValid("info@butterblümchen.com"));
    }
    
    /**
     * Tests {@link EmailValidator} with an email containing "..".
     */
    @Test
    public void illegalTld() {
        Assert.assertFalse(EmailValidator.getInstance().isValid("info@t-online..de"));
    }
    
    @Test
    public void illegalSign() {
        Assert.assertFalse(EmailValidator.getInstance().isValid("mette@mettemøller.no"));
    }
    
}
