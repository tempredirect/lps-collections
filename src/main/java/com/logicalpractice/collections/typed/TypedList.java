package com.logicalpractice.collections.typed;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/**
 * List wrapper that holds runtime type information.
 * 
 * @author gareth
 *
 * @param <T> Type of collection
 */
public class TypedList<T> extends TypedCollection<T> implements List<T> {

   // static's **************************************************************
   
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

   // fields ****************************************************************
   
   private final List<T> delegate;
   
   // constructors **********************************************************
   
   public TypedList(List<T> delegate, Class<T> type) {
      super(delegate,type);
      this.delegate = delegate;
   }

   // implmentation of List *************************************************
   
   public void add(int index, T element) {
      delegate.add(index, element);
   }

   public boolean addAll(int index, Collection<? extends T> c) {
      return delegate.addAll(index, c);
   }

   public T get(int index) {
      return delegate.get(index);
   }

   public int indexOf(Object o) {
      return delegate.indexOf(o);
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

   public T set(int index, T element) {
      return delegate.set(index, element);
   }

   public List<T> subList(int fromIndex, int toIndex) {
      return new TypedList<T>(delegate.subList(fromIndex, toIndex), type());
   }
}
