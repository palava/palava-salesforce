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
import org.apache.commons.validator.UrlValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.sforce.soap.enterprise.sobject.Account;

import de.cosmocode.commons.Strings;
import de.cosmocode.palava.model.business.AccountBase;
import de.cosmocode.palava.model.business.AddressBase;
import de.cosmocode.palava.salesforce.Salesforce;

/**
 * Default {@link AccountBase} to {@link Account} copy function.
 * 
 * <p>The following table shows the field mapping:
 * <table>
 *   <tr>
 *     <th>Model</th>
 *     <th>Salesforce</th>
 *   </tr>
 *   <tr>
 *     <td>{@link AccountBase#getName()}</td>
 *     <td>{@link Account#getName()}</td>
 *   </tr>
 *   <tr>
 *     <td>{@link AccountBase#getId()}</td>
 *     <td>{@link Account#getAccountNumber()}</td>
 *   </tr>
 *   <tr>
 *     <td>{@link AddressBase#getPhone()}</td>
 *     <td>{@link Account#getPhone()}</td>
 *   </tr>
 *   <tr>
 *     <td>{@link AddressBase#getFax()}</td>
 *     <td>{@link Account#getFax()}</td>
 *   </tr>
 *   <tr>
 *     <td>{@link AddressBase#getWebsite()}</td>
 *     <td>{@link Account#getWebsite()}</td>
 *   </tr>
 *   <tr>
 *     <td>
 *       {@link AddressBase#getStreet()},
 *       {@link AddressBase#getStreetNumber()},
 *       {@link AddressBase#getAdditional()}
 *     </td>
 *     <td>{@link Account#getBillingStreet()}</td>
 *   </tr>
 *   <tr>
 *     <td>{@link AddressBase#getPostalCode()}</td>
 *     <td>{@link Account#getBillingPostalCode()}</td>
 *   </tr>
 *   <tr>
 *     <td>{@link AddressBase#getCityName()}</td>
 *     <td>{@link Account#getBillingCity()}</td>
 *   </tr>
 *   <tr>
 *     <td>{@link AddressBase#getState()}</td>
 *     <td>{@link Account#getBillingState()}</td>
 *   </tr>
 *   <tr>
 *     <td>{@link AddressBase#getCountryCode()}</td>
 *     <td>{@link Account#getBillingCountry()}</td>
 *   </tr>
 * </table>
 * 
 * <p>
 *   Note: Invalid values will be trimmed to fit into their corresponding salesforce fields.
 *   This may e.g. result in the postalcode being trimmed to 40 chars.
 * </p>
 *
 * @author Willi Schoenborn
 */
public final class DefaultAccountCopyFunction implements Function<AccountBase, Account> {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultAccountCopyFunction.class);

    private static final UrlValidator URL_VALIDATOR = new UrlValidator();
    
    private static final Function<AccountBase, Account> INSTANCE = new DefaultAccountCopyFunction();
    
    private DefaultAccountCopyFunction() {
        
    }
    
    @Override
    public Account apply(AccountBase from) {
        final Account to = Salesforce.FACTORY.createAccount();
        LOG.debug("Copying {} into {}", from, to);

        String name = from.getName();
        name = Strings.defaultIfBlank(name, "Empty Name");
        name = StringUtils.substring(name, 0, 255);
        to.setName(Salesforce.FACTORY.createAccountName(name));

        final String number = StringUtils.substring(Long.toString(from.getId()), 0, 40);
        to.setAccountNumber(Salesforce.FACTORY.createAccountAccountNumber(number));
        
        final AddressBase address = from.getAddress();

        final String phone = StringUtils.substring(address.getPhone(), 0, 40);
        to.setPhone(Salesforce.FACTORY.createAccountPhone(phone));
        
        final String fax = StringUtils.substring(address.getFax(), 0, 40);
        to.setFax(Salesforce.FACTORY.createAccountFax(fax));
        
        String website = address.getWebsite();
        website = StringUtils.substring(website, 0, 255);
        website = URL_VALIDATOR.isValid(website) ? website : null;
        to.setWebsite(Salesforce.FACTORY.createAccountWebsite(website));
        
        final String street = Strings.defaultIfBlank(address.getStreet(), "");
        final String streetNumber = Strings.defaultIfBlank(address.getStreetNumber(), "");
        final String additional = Strings.defaultIfBlank(address.getAdditional(), "");
        final String formatted = String.format("%s %s\n%s", street, streetNumber, additional);
        final String trimmed = StringUtils.substring(formatted, 0, 255);
        to.setBillingStreet(Salesforce.FACTORY.createAccountBillingStreet(trimmed));
        
        final String postalCode = StringUtils.substring(address.getPostalCode(), 0, 20);
        to.setBillingPostalCode(Salesforce.FACTORY.createAccountBillingPostalCode(postalCode));
        
        final String city = StringUtils.substring(address.getCityName(), 0, 40);
        to.setBillingCity(Salesforce.FACTORY.createAccountBillingCity(city));
        
        final String state = StringUtils.substring(address.getState(), 0, 20);
        to.setBillingState(Salesforce.FACTORY.createAccountBillingState(state));
        
        final String country = StringUtils.substring(address.getCountryCode(), 0, 40);
        to.setBillingCountry(Salesforce.FACTORY.createAccountBillingCountry(country));
        
        return to;
    }
    
    public static Function<AccountBase, Account> getInstance() {
        return INSTANCE;
    }
    
}
