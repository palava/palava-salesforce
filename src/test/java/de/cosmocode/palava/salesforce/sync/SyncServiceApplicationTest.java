package de.cosmocode.palava.salesforce.sync;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.cosmocode.palava.core.Framework;
import de.cosmocode.palava.core.Palava;

public final class SyncServiceApplicationTest {

    private static final Logger LOG = LoggerFactory.getLogger(SyncServiceApplicationTest.class);

    @Test
    public void test() {
        final Framework framework = Palava.createFramework(SyncConfiguration.getProperties());
        framework.start();
        framework.stop();
    }
    
}
