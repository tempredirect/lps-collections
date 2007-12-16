/*
 * TypedCollection.java Created on 16 Dec 2007
 *
 * All rights reserved Logical Practice Systems Limited 2007
 */
package com.logicalpractice.collections;

import java.util.Collection;
import java.util.Iterator;

/**
 * Collection wrapper that implements Typed&lt;T&gt;.
 * A typed object retains the runtime type information which isn't available
 * under the java implmentations of generics.
 * 
 * @author gareth
 */
public class TypedCollection<T> implements Collection<T>,Typed<T> {
   private final Collection<T> delegate;
   private final Class<T> type ;
   
   public TypedCollection(Collection<T> delegate, Class<T> type) {
      if( delegate == null || type == null ){
         throw new IllegalArgumentException("both parameters are required to be not null");
      }
      this.delegate = delegate;
      this.type = type ;
   }

   public boolean add(T o) {
      return delegate.add(o);
   }

   public boolean addAll(Collection<? extends T> c) {
      return delegate.addAll(c);
   }

   public void clear() {
      delegate.clear();
   }

   public boolean contains(Object o) {
      return delegate.contains(o);
   }

   public boolean containsAll(Collection<?> c) {
      return delegate.containsAll(c);
   }

   public boolean isEmpty() {
      return delegate.isEmpty();
   }

   public Iterator<T> iterator() {
      return delegate.iterator();
   }

   public boolean remove(Object o) {
      return delegate.remove(o);
   }

   public boolean removeAll(Collection<?> c) {
      return delegate.removeAll(c);
   }

   public boolean retainAll(Collection<?> c) {
      return delegate.retainAll(c);
   }

   public int size() {
      return delegate.size();
   }

   public Object[] toArray() {
      return delegate.toArray();
   }

   @SuppressWarnings("hiding")
   public <T> T[] toArray(T[] a) {
      return delegate.toArray(a);
   }

   /* (non-Javadoc)
    * @see com.logicalpractice.collections.Typed#type()
    */
   public final Class<T> type() {
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
