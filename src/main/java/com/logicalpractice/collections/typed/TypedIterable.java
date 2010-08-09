/*
 * TypedIterable.java Created on 17 Dec 2007
 *
 * All rights reserved Logical Practice Systems Limited 2007
 */
package com.logicalpractice.collections.typed;

import java.util.Iterator;

/**
 * @author gareth
 */
class TypedIterable<T> implements Iterable<T>, Typed<T> {

   private final Iterable<T> delegate ;
   private final Class<T> type ;

   /**
    * @param delegate
    * @param type
    */
   TypedIterable(Iterable<T> delegate, Class<T> type) {
      if( delegate == null || type == null ){
         throw new IllegalArgumentException("both parameters are required to be not null");
      }
      
      this.delegate = delegate;
      this.type = type;
   }

   /* (non-Javadoc)
    * @see java.lang.Iterable#iterator()
    */
   public Iterator<T> iterator() {
      return delegate.iterator();
   }

   /* (non-Javadoc)
    * @see com.logicalpractice.collections.typed.Typed#type()
    */
   public Class<T> type() {
      return type;
   }
   
   /**
    * forwards directly to the underlying collection. 
    * Therefore two TypedList's with different type information will still be
    * the same if the equals method of the underlying list returns true.
    */
   @Override
   public boolean equals(Object o) {
      return delegate.equals(o);
   }

   /**
    * Delegated directly to the underlying list.
    */
   @Override
   public int hashCode() {
      return delegate.hashCode();
   }

}
