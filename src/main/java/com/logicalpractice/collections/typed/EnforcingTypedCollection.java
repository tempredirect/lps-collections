/*
 * EnforcingTypedCollection.java Created on 16 Dec 2007
 *
 * All rights reserved Logical Practice Systems Limited 2007
 */
package com.logicalpractice.collections.typed;

import static com.logicalpractice.collections.typed.TypedUtils.checkType;
import static com.logicalpractice.collections.typed.TypedUtils.checkTypesOf;

import java.util.Collection;

/**
 * @author gareth
 */
class EnforcingTypedCollection<T> extends TypedCollection<T> {

   /**
    * @param delegate
    * @param type
    */
   public EnforcingTypedCollection(Collection<T> delegate, Class<T> type) {
      super(delegate, type);
   }

   @Override
   public boolean add(T o) {
      checkType(type(), o);
      return super.add(o);
   }

   @Override
   public boolean addAll(Collection<? extends T> c) {
      checkTypesOf(type(), c);
      return super.addAll(c);
   }
}
