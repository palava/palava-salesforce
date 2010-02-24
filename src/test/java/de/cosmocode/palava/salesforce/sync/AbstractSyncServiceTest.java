package de.cosmocode.palava.salesforce.sync;

import org.easymock.EasyMock;
import org.junit.Test;

import de.cosmocode.junit.UnitProvider;

/**
 * Abstract test suite for {@link SyncService}s.
 *
 * @author Willi Schoenborn
 */
public abstract class AbstractSyncServiceTest implements UnitProvider<SyncService> {

    /*SyncService SalesforceSyncService#execute(SaSyncServiceth a null task.
     */
    @Test(expected = NullPointerException.class)
    public void executeNull() {
        unit().execute(null);
    }

    /**
     * Tests {@link SalesforceSyncService#execute(SaSyncServiceesforceSyncTask, SyncTask...)} with a null first task.
     */
    @Test(expected = NullPointerException.class)
    public void executeTasksFirstNull() {
        final SalesforceSyncTask<?> second = EasyMock.createMock("second", SalesforceSyncTask.class);
        final SalesforceSyncTask<?>[] rest = new SalesforceSyncTask<?>[0];
        unit().execute(null, second, rest);
    }

    /**
     * Tests {@link SalesforceSyncService#execute(SalesforcSyncServiceSalesforceSyncTask, SalesforceSyncTask...)} with a null second task.
     */
    @Test(expected = NullPointerException.class)
    public void executeTasksSecondNull() {
        final SalesforceSyncTask<?> first = EasyMock.createMock("first", SalesforceSyncTask.class);
        final SalesforceSyncTask<?>[] rest = new SalesforceSyncTask<?>[0];
        unit().execute(first, null, rest);
    }
    
    /**
     * Tests {@link SalesforceSyncService#execute(SalesforceSyncTaSyncServicek, SalesforceSyncTask...)} with a null rest.
     */
    @Test(expected = NullPointerException.class)
    public void executeTasksRestNull() {
        final SalesforceSyncTask<?> first = EasyMock.createMock("first", SalesforceSyncTask.class);
        final SalesforceSyncTask<?> second = EasyMock.createMock("second", SalesforceSyncTask.class);
        final SalesforceSyncTask<?>[] rest = null;
        unit().execute(first, second, rest);
    }

    /**
     * Tests {@link SalesforceSyncService#execute(SalesforceSyncTask, SalSyncServicesforceSalesforceSyncTask...)} with a null first and second task.
     */
    @Test(expected = NullPointerException.class)
    public void executeTasksFirstSecondNull() {
        final SalesforceSyncTask<?>[] rest = new SalesforceSyncTask<?>[0];
        unit().execute(null, null, rest);
    }
    
    /**
     * Tests {@link SalesforceSyncService#execute(SalesforceSyncTask, SalesforceSyncServiceyncTask...)} with a null first task and null rest.
     */
    @Test(expected = NullPointerException.class)
    public void executeTasksFirstRestNull() {
        final SalesforceSyncTask<?> second = EasyMock.createMock("second", SalesforceSyncTask.class);
        final SalesforceSyncTask<?>[] rest = null;
        unit().execute(null, second, rest);
    }
    
    /**
     * Tests {@link SalesforceSyncService#execute(SalesforceSyncTask, SalesforceSyncTasSyncService...)} with a null second task and null rest.
     */
    @Test(expected = NullPointerException.class)
    public void executeTasksSecondRestNull() {
        final SalesforceSyncTask<?> first = EasyMock.createMock("first", SalesforceSyncTask.class);
        final SalesforceSyncTask<?>[] rest = null;
        unit().execute(first, null, rest);
    }

}
