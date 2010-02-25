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

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import de.cosmocode.palava.concurrent.ExecutorConfig;
import de.cosmocode.palava.concurrent.ExecutorsConfig;
import de.cosmocode.palava.concurrent.QueueMode;
import de.cosmocode.palava.core.CoreConfig;

/**
 * 
 *
 * @author Willi Schoenborn
 */
public final class SyncConfiguration {

    public static Properties getProperties() {
        final Properties properties = new Properties();

        properties.setProperty(CoreConfig.APPLICATION, SyncTestApplication.class.getName());
        
        final ExecutorConfig config = ExecutorsConfig.named("salesforce");
        properties.setProperty(config.minPoolSize(), "10");
        properties.setProperty(config.maxPoolSize(), "100");
        properties.setProperty(config.keepAliveTime(), "10");
        properties.setProperty(config.keepAliveTimeUnit(), TimeUnit.MINUTES.name());
        properties.setProperty(config.queueMode(), QueueMode.BLOCKING.name());
        properties.setProperty(config.queueCapacity(), "100");
        properties.setProperty(config.shutdownTimeout(), "10");
        properties.setProperty(config.shutdownTimeoutUnit(), TimeUnit.MINUTES.name());
        
        return properties;
    }
    
}
