package de.cosmocode.palava.salesforce.sync;

import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;

import de.cosmocode.palava.concurrent.Runnables;
import de.cosmocode.palava.model.base.EntityBase;
import de.cosmocode.palava.salesforce.SalesforceExecutor;

/**
 * Default implementation of the {@link SyncService} interface.
 *
 * @author Willi Schoenborn
 */
final class DefaultSalesforceSyncService implements SyncService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultSalesforceSyncService.class);

    private final ExecutorService service;
    
    @Inject
    public DefaultSalesforceSyncService(@SalesforceExecutor ExecutorService service) {
        this.service = Preconditions.checkNotNull(service, "Service");
    }
    
    @Override
    public <T extends EntityBase> void execute(SalesforceSyncTask<T> task) {
        Preconditions.checkNotNull(task, "Task");
        LOG.trace("Executing {}", task);
        service.execute(task);
    }

    @Override
    public void execute(SalesforceSyncTask<?> first, SalesforceSyncTask<?> second, SalesforceSyncTask<?>... rest) {
        final Runnable task = Runnables.chain(first, second, rest);
        LOG.trace("Executing {}", task);
        service.execute(task);
    }

}
