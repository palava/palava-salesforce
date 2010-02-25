package de.cosmocode.palava.salesforce.sync;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;

import de.cosmocode.palava.concurrent.ExecutorModule;
import de.cosmocode.palava.salesforce.SalesforceExecutor;

/**
 * Binds the {@link SyncService} to its default implementation.
 *
 * @author Willi Schoenborn
 */
public final class SyncServiceModule implements Module {

    @Override
    public void configure(Binder binder) {
        binder.install(new ExecutorModule(SalesforceExecutor.class, "salesforce"));
        binder.bind(SyncService.class).to(DefaultSyncService.class).in(Singleton.class);
    }

}
