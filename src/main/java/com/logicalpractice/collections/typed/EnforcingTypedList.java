package com.logicalpractice.collections.typed;

import static com.logicalpractice.collections.typed.TypedUtils.checkType;
import static com.logicalpractice.collections.typed.TypedUtils.checkTypesOf;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

public class EnforcingTypedList<T> extends TypedList<T> {

   public static class EnforcingTypedListIterator<C> extends TypedList.TypedListIterator<C> {

      protected EnforcingTypedListIterator(ListIterator<C> iterator, Class<C> type) {
         super(iterator, type);
      }

      @Override
      public void add(C o) {
         checkType(type(), o);
         super.add(o);
      }

      @Override
      public void set(C o) {
         checkType(type(), o);
         super.set(o);
      }
   }

   public EnforcingTypedList(List<T> delegate, Class<T> type) {
      super(delegate, type);
   }

   @Override
   public void add(int index, T element) {
      checkType(type(), element);
      super.add(index, element);
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

   @Override
   public boolean addAll(int index, Collection<? extends T> c) {
      checkTypesOf(type(), c);
      return super.addAll(index, c);
   }

   @Override
   public ListIterator<T> listIterator() {
      return new EnforcingTypedListIterator<T>(super.listIterator(), type());
   }

   @Override
   public ListIterator<T> listIterator(int index) {
      return new EnforcingTypedListIterator<T>(super.listIterator(index), type());
   }

   @Override
   public T set(int index, T element) {
      checkType(type(), element);
      return super.set(index, element);
   }

   @Override
   public List<T> subList(int fromIndex, int toIndex) {
      return new EnforcingTypedList<T>(super.subList(fromIndex, toIndex), type());
   }
}
