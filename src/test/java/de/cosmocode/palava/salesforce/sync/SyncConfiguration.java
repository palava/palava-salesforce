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
