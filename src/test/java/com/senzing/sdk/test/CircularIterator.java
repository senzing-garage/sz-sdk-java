package com.senzing.sdk.test;

import java.util.Iterator;
import java.util.Collection;

/**
 * Utility class for circular iteration.
 */
class CircularIterator<T> implements Iterator<T> {
    private Collection<T> collection = null;
    private Iterator<T> iterator = null;

    CircularIterator(Collection<T> collection) {
        this.collection = collection;
        this.iterator = this.collection.iterator();
    }

    public boolean hasNext() {
        return (this.collection.size() > 0);
    }

    public T next() {
        if (!this.iterator.hasNext()) {
            this.iterator = this.collection.iterator();
        }
        return this.iterator.next();
    }

    public void remove() {
        throw new UnsupportedOperationException(
                "Cannot remove from a circular iterator.");
    }
}
