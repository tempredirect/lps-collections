/*
 * EnforcingTypedSet.java Created on 16 Dec 2007
 *
 * All rights reserved Logical Practice Systems Limited 2007
 */
package com.logicalpractice.collections.typed;

import java.util.Collection;

/**
 * @author gareth
 */
class EnforcingTypedSet<T> extends EnforcingTypedCollection<T> {

   /**
    * @param delegate
    * @param type
    */
   EnforcingTypedSet(Collection<T> delegate, Class<T> type) {
      super(delegate, type);
   }
}
