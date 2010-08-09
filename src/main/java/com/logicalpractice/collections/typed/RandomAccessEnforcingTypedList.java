package com.logicalpractice.collections.typed;

import java.util.List;
import java.util.RandomAccess;

/**
 */
class RandomAccessEnforcingTypedList<T> extends EnforcingTypedList<T> implements RandomAccess {
    RandomAccessEnforcingTypedList(List<T> delegate, Class<T> type) {
        super(delegate, type);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
