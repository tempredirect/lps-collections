package com.logicalpractice.collections;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * List wrapper that holds runtime type information.
 * 
 * @author gareth
 *
 * @param <T>
 */
public class TypedList<T> implements List<T>, Typed<T> {

   public static class TypedListIterator<C> implements Typed<C>, ListIterator<C> {
      private final ListIterator<C> iteratorDeleate ;
      private final Class<C> iteratorType ;
      
      protected TypedListIterator(ListIterator<C> iterator, Class<C> type){
         this.iteratorDeleate = iterator ;
         this.iteratorType = type ;
      }
      
      public final Class<C> type(){
         return iteratorType;
      }

      public void add(C o) {
         iteratorDeleate.add(o);
      }

      public boolean hasNext() {
         return iteratorDeleate.hasNext();
      }

      public boolean hasPrevious() {
         return iteratorDeleate.hasPrevious();
      }

      public C next() {
         return iteratorDeleate.next();
      }

      public int nextIndex() {
         return iteratorDeleate.nextIndex();
      }

      public C previous() {
         return iteratorDeleate.previous();
      }

      public int previousIndex() {
         return iteratorDeleate.previousIndex();
      }

      public void remove() {
         iteratorDeleate.remove();
      }

      public void set(C o) {
         iteratorDeleate.set(o);
      }
   }

   private final List<T> delegate;
   private final Class<T> type ;
   
   public TypedList(List<T> delegate, Class<T> type) {
      if( delegate == null || type == null ){
         throw new IllegalArgumentException("both parameters are required to be not null");
      }
      this.delegate = delegate;
      this.type = type ;
   }

   public void add(int index, T element) {
      delegate.add(index, element);
   }

   public boolean add(T o) {
      return delegate.add(o);
   }

   public boolean addAll(Collection<? extends T> c) {
      return delegate.addAll(c);
   }

   public boolean addAll(int index, Collection<? extends T> c) {
      return delegate.addAll(index, c);
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

   public T get(int index) {
      return delegate.get(index);
   }

   public int indexOf(Object o) {
      return delegate.indexOf(o);
   }

   public boolean isEmpty() {
      return delegate.isEmpty();
   }

   public Iterator<T> iterator() {
      return delegate.iterator();
   }

   public int lastIndexOf(Object o) {
      return delegate.lastIndexOf(o);
   }

   public ListIterator<T> listIterator() {
      return new TypedListIterator<T>(delegate.listIterator(), type());
   }

   public ListIterator<T> listIterator(int index) {
      return new TypedListIterator<T>(delegate.listIterator(index), type());
   }

   public T remove(int index) {
      return delegate.remove(index);
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

   public T set(int index, T element) {
      return delegate.set(index, element);
   }

   public int size() {
      return delegate.size();
   }

   public List<T> subList(int fromIndex, int toIndex) {
      return new TypedList<T>(delegate.subList(fromIndex, toIndex), type());
   }

   public Object[] toArray() {
      return delegate.toArray();
   }

   public <V> V[] toArray(V[] a) {
      return delegate.toArray(a);
   }
   
   public final Class<T> type(){
      return type;
   }

   /**
    * Equals is forward directory to the
    * underlying list. Therefore two TypedList's with different type information will still be
    * the same if the equals method of the underlying list returns true.
    */
   public boolean equals(Object o) {
      return delegate.equals(o);
   }

   /**
    * Delegated directly to the underlying list.
    */
   public int hashCode() {
      return delegate.hashCode();
   }

}
