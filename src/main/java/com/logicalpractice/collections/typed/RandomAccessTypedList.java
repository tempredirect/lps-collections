package com.logicalpractice.collections.typed;

import java.util.List;
import java.util.RandomAccess;

/**
 */
class RandomAccessTypedList<T> extends TypedList<T> implements RandomAccess {
    RandomAccessTypedList(List<T> delegate, Class<T> type) {
        super(delegate, type);
    }
}
