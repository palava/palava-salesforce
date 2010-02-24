package de.cosmocode.palava.salesforce;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.inject.BindingAnnotation;

import de.cosmocode.palava.salesforce.sync.SyncService;

/**
 * Used to identify the threadpool for the {@link SyncService}.
 *
 * @author Willi Schoenborn
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({
    ElementType.PARAMETER,
    ElementType.FIELD
})
@BindingAnnotation
public @interface SalesforceExecutor {

}
