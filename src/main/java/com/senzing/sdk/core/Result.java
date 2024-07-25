/**********************************************************************************
 © Copyright Senzing, Inc. 2021
 The source code for this program is not published or otherwise divested
 of its trade secrets, irrespective of what has been deposited with the U.S.
 Copyright Office.
**********************************************************************************/

package com.senzing.sdk.core;

/**
 * Provides a simple wrapper for handling "out parameters" of any type.
 * 
 * @param <T> The parametized type of the result.
 */
class Result<T> {
    /**
     * The underlying value.
     */
    private T value;

    /**
     * Default constructor.  This will construct with a <code>null</code> value.
     */
    Result() {
        this(null);
    }

    /**
     * Constructs with the specified value.
     * 
     * @param value The value with which to construct.
     */
    Result(T value) {
        this.value = value;
    }

    /**
     * Sets the underlying value to the specified value.
     * 
     * @param value The value to be set.
     */
    public void setValue(T value)
    {
        this.value = value;
    }
    
    /**
     * Gets the underlying value.
     * 
     * @return The underlying value.
     */
    public T getValue()
    {
        return this.value;
    }
}
