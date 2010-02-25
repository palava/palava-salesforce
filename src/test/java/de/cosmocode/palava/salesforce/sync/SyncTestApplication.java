package de.cosmocode.palava.salesforce.sync;

import java.util.concurrent.Executors;

import org.easymock.EasyMock;

import com.google.inject.Binder;
import com.google.inject.Module;

import de.cosmocode.palava.concurrent.ThreadProvider;
import de.cosmocode.palava.core.RegistryModule;

public final class SyncTestApplication implements Module {

    @Override
    public void configure(Binder binder) {
        binder.install(new SyncServiceModule());
        binder.install(new RegistryModule());
        final ThreadProvider provider = EasyMock.createMock("provider", ThreadProvider.class);
        EasyMock.expect(provider.newThreadFactory()).andReturn(Executors.defaultThreadFactory());
        EasyMock.replay(provider);
        binder.bind(ThreadProvider.class).toInstance(provider);
    }

}
