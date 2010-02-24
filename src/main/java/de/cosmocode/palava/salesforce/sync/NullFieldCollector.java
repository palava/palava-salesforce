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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.sforce.soap.enterprise.sobject.SObject;

/**
 * A utility class used to collect nullable fields from {@link SObject}s.
 *
 * @author Willi Schoenborn
 */
public final class NullFieldCollector {

    private static final Logger LOG = LoggerFactory.getLogger(NullFieldCollector.class);

    private NullFieldCollector() {
        
    }
    
    /**
     * Adds all null fields returned by {@link NullFieldCollector#collect(SObject)}
     * to the given object by using {@link List#addAll(Collection)} on 
     * the list returned by {@link SObject#getFieldsToNull()}.
     * 
     * @param object the object to use
     * @throws NullPointerException if object is null
     */
    public static void addNullFields(SObject object) {
        final Set<String> nullFields = collect(object);
        object.getFieldsToNull().addAll(nullFields);
    }
    
    /**
     * Collects all nullable fields from the given object
     * by walking recursively via reflection through
     * the object graph. 
     * 
     * <p>
     *   The field name is either the name as specified
     *   by {@link XmlElement#name()} or {@link Field#getName()}.
     * </p>
     * 
     * @param object the object containing fields
     * @return a Set of all field names
     */
    public static Set<String> collect(SObject object) {
        Preconditions.checkNotNull(object, "Object");
        final Set<String> nullFields = Sets.newHashSet();
        NullFieldCollector.collect(nullFields, object);
        return nullFields;
    }
    
    private static void collect(Set<String> nullFields, Object object) {
        LOG.trace("Collecting null fields on {}", object);
        final Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) continue;
            if ("fieldsToNull".equals(field.getName())) continue;
            try {
                final boolean accessible = field.isAccessible();
                field.setAccessible(true);
                final Object value = field.get(object);
                field.setAccessible(accessible);
                if (value == null) {
                    continue;
                } else if (value instanceof JAXBElement<?>) {
                    final JAXBElement<?> jaxb = JAXBElement.class.cast(value);
                    if (jaxb.getValue() == null) {
                        nullFields.add(nameOf(field));
                    }
                }
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
        }
        LOG.trace("Null fields on {}: {}", object, nullFields);
    }
    
    private static String nameOf(Field field) {
        final XmlElementRef ref = field.getAnnotation(XmlElementRef.class);
        if (ref == null || StringUtils.isBlank(ref.name())) {
            return field.getName();
        } else {
            return ref.name();
        }
    }
    
}
