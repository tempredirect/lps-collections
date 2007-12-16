package com.logicalpractice.collections.typed;

/**
 * Used by the Selector and friends to work out the runtime type of a collection.
 * @author gareth
 * @param <T>
 */
public interface Typed<T> {
   Class<T> type();
}
