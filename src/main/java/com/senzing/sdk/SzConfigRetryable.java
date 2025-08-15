package com.senzing.sdk;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to annotate which Senzing SDK methods are dependent upon the
 * active configuration being current.  
 * 
 * <p>
 * Methods annotated with this annotation are those methods that
 * may experience a failure if the {@linkplain SzEnvironment#getActiveConfigId() 
 * active configuration} is <b>not</b> the most recent configuration 
 * for the repository.  Such failures can occur when retrieved data 
 * references a configuration element (e.g.: data source, feature type or
 * match type) that is not found in the {@linkplain 
 * SzEnvironment#getActiveConfigId() active configuration}.
 * </p>
 * 
 * <p>
 * There are various ways this can happen, but typically it means that data
 * has been loaded through another {@link SzEnvironment} using a newer
 * configuration (usually in another system process) and then that data is
 * retrieved by an {@link SzEnvironment} that was initialized prior to that 
 * configuration being {@linkplain SzConfigManager#registerConfig(String, String)
 * registered in the repository} and being set as the {@linkplain
 * SzConfigManager#setDefaultConfigId(long) default configuration}.
 * </p>
 *
 * </p>
 * Such failures can usually be resolved by retrying after checking if the 
 * {@linkplain SzEnvironment#getActiveConfigId() active configuration ID} differs
 * from the current {@link SzConfigManager#getDefaultConfigId() default 
 * configuration ID}, and if so {@linkplain SzEnvironment#reinitialize(long) 
 * reinitializing} using the current {@link SzConfigManager#getDefaultConfigId()
 * default configuration ID}.
 * </p> 
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.METHOD)
public @interface SzConfigRetryable {
}
