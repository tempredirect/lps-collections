/*
 * TypedCollection.java Created on 16 Dec 2007
 *
 * All rights reserved Logical Practice Systems Limited 2007
 */
package com.logicalpractice.collections.typed;

import java.util.Collection;

/**
 * Collection wrapper that implements Typed&lt;T&gt;.
 * A typed object retains the runtime type information which isn't available
 * under the java implmentations of generics.
 * 
 * @author gareth
 */
class TypedCollection<T> extends TypedIterable<T> implements Collection<T> {
   private final Collection<T> delegate;
   
   TypedCollection(Collection<T> delegate, Class<T> type) {
      super(delegate,type);
      this.delegate = delegate;
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
}
