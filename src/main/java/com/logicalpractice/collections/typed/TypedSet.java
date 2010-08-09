/*
 * TypedSet.java Created on 16 Dec 2007
 *
 * All rights reserved Logical Practice Systems Limited 2007
 */
package com.logicalpractice.collections.typed;

import java.util.Set;

/**
 * Set wrapper class that provides runtime type information.
 * @author gareth
 */
class TypedSet<T> extends TypedCollection<T> implements Set<T> {

   /**
    * @param delegate
    * @param type
    */
   TypedSet(Set<T> delegate, Class<T> type) {
      super(delegate, type);
   }
}
