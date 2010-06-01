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

package de.cosmocode.palava.salesforce.sync;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.sforce.soap.enterprise.sobject.Contact;

import de.cosmocode.commons.Strings;
import de.cosmocode.palava.model.business.AddressBase;
import de.cosmocode.palava.model.business.ContactBase;
import de.cosmocode.palava.salesforce.Salesforce;

/**
 * Default {@link ContactBase} to {@link Contact} copy function.
 * 
 * <p>The following table shows the field mapping:
 * <table>
 *   <tr>
 *     <th>Model</th>
 *     <th>Salesforce</th>
 *   </tr>
 *   <tr>
 *     <td>{@link ContactBase#getTitle()}</td>
 *     <td>{@link Contact#getSalutation()}</td>
 *   </tr>
 *   <tr>
 *     <td>{@link ContactBase#getForename()}</td>
 *     <td>{@link Contact#getFirstName()}</td>
 *   </tr>
 *   <tr>
 *     <td>{@link ContactBase#getSurname()}</td>
 *     <td>{@link Contact#getLastName()}</td>
 *   </tr>
 *   <tr>
 *     <td>{@link AddressBase#getPhone()}</td>
 *     <td>{@link Contact#getPhone()}</td>
 *   </tr>
 *   <tr>
 *     <td>{@link AddressBase#getMobilePhone()}</td>
 *     <td>{@link Contact#getMobilePhone()}</td>
 *   </tr>
 *   <tr>
 *     <td>{@link AddressBase#getFax()}</td>
 *     <td>{@link Contact#getFax()}</td>
 *   </tr>
 *   <tr>
 *     <td>{@link AddressBase#getEmail()}</td>
 *     <td>{@link Contact#getEmail()}</td>
 *   </tr>
 *   <tr>
 *     <td>
 *       {@link AddressBase#getStreet()},
 *       {@link AddressBase#getStreetNumber()},
 *       {@link AddressBase#getAdditional()}
 *     </td>
 *     <td>{@link Contact#getMailingStreet()}</td>
 *   </tr>
 *   <tr>
 *     <td>{@link AddressBase#getPostalCode()}</td>
 *     <td>{@link Contact#getMailingPostalCode()}</td>
 *   </tr>
 *   <tr>
 *     <td>{@link AddressBase#getCityName()}</td>
 *     <td>{@link Contact#getMailingCity()}</td>
 *   </tr>
 *   <tr>
 *     <td>{@link AddressBase#getState()}</td>
 *     <td>{@link Contact#getMailingState()}</td>
 *   </tr>
 *   <tr>
 *     <td>{@link AddressBase#getCountryCode()}</td>
 *     <td>{@link Contact#getMailingCountry()}</td>
 *   </tr>
 * </table>
 * <p>
 *   Note: Values which exeeds the length limit will be trimmed to fit into their corresponding salesforce fields.
 *   This may e.g. result in the postalcode being trimmed to 40 chars. Values which do not satisfy their
 *   corresponding validation rules will be blanked (set to null).
 * </p>
 *
 * @author Willi Schoenborn
 */
public final class DefaultContactCopyFunction implements Function<ContactBase, Contact> {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultContactCopyFunction.class);

    private static final EmailValidator EMAIL_VALIDATOR = EmailValidator.getInstance();
    
    private static final Function<ContactBase, Contact> INSTANCE = new DefaultContactCopyFunction();

    private DefaultContactCopyFunction() {
        
    }
    
    @Override
    public Contact apply(ContactBase from) {
        final Contact to = Salesforce.FACTORY.createContact();
        LOG.debug("Copying {} into {}", from, to);
        
        final String title = StringUtils.substring(from.getTitle(), 0, 40);
        to.setSalutation(Salesforce.FACTORY.createContactSalutation(title));
        
        final String forename = StringUtils.substring(from.getForename(), 0, 40);
        to.setFirstName(Salesforce.FACTORY.createContactFirstName(forename));
        
        String surname = from.getSurname();
        surname = Strings.defaultIfBlank(surname, "Empty Surname");
        surname = StringUtils.substring(surname, 0, 80);
        to.setLastName(Salesforce.FACTORY.createContactLastName(surname));
        
        final AddressBase address = from.getAddress();

        final String phone = StringUtils.substring(address.getPhone(), 0, 40);
        to.setPhone(Salesforce.FACTORY.createContactPhone(phone));
        
        final String mobile = StringUtils.substring(address.getMobilePhone(), 0, 40);
        to.setPhone(Salesforce.FACTORY.createContactMobilePhone(mobile));
        
        final String fax = StringUtils.substring(address.getFax(), 0, 40);
        to.setFax(Salesforce.FACTORY.createContactFax(fax));
        
        String email = address.getEmail();
        email = StringUtils.substring(email, 0, 80);
        email = EMAIL_VALIDATOR.isValid(email) ? email : null;
        to.setEmail(Salesforce.FACTORY.createContactEmail(email));

        final String street = Strings.defaultIfBlank(address.getStreet(), "");
        final String streetNumber = Strings.defaultIfBlank(address.getStreetNumber(), "");
        final String additional = Strings.defaultIfBlank(address.getAdditional(), "");
        final String formatted = String.format("%s %s\n%s", street, streetNumber, additional);
        final String trimmed = StringUtils.substring(formatted, 0, 255);
        to.setMailingStreet(Salesforce.FACTORY.createContactMailingStreet(trimmed));

        final String postalCode = StringUtils.substring(address.getPostalCode(), 0, 20);
        to.setMailingPostalCode(Salesforce.FACTORY.createContactMailingPostalCode(postalCode));
        
        final String city = StringUtils.substring(address.getCityName(), 0, 40);
        to.setMailingCity(Salesforce.FACTORY.createContactMailingCity(city));
        
        final String state = StringUtils.substring(address.getState(), 0, 20);
        to.setMailingState(Salesforce.FACTORY.createContactMailingState(state));
        
        final String country = StringUtils.substring(address.getCountryCode(), 0, 40);
        to.setMailingCountry(Salesforce.FACTORY.createContactMailingCountry(country));
        
        return to;
    }
    
    public static Function<ContactBase, Contact> getInstance() {
        return INSTANCE;
    }
    
}
