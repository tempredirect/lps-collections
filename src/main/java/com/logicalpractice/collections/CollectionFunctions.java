package com.logicalpractice.collections;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;

public class CollectionFunctions {

   private static final ThreadLocal<Iterable<?>> localItems = new ThreadLocal<Iterable<?>>();

   public static <T, V> List<T> select(Iterable<T> items, Script<V> script, Matcher<V> matcher) {
      List<T> result = new LinkedList<T>();

      for (T item : items) {
         try {
            if (matcher.matches(script.evaluate(item))) {
               result.add(item);
            }
         } catch (RuntimeException e) {
            throw e;
         } catch (InvocationTargetException e) {
            if (e.getCause() instanceof RuntimeException) {
               throw (RuntimeException) e.getCause();
            }
         } catch (Exception e) {
            throw new RuntimeException(e);
         }
      }
      return result;
   }

   public static <T, V> List<T> select(Iterable<T> items, V value, Matcher<V> matcher) {
      List<T> result = new LinkedList<T>();
      CapturingProxy<V> proxy = getCurrentCapture(value);

      for (T item : items) {
         try {
            if (matcher.matches(proxy.replay(item))) {
               result.add(item);
            }
         } catch (RuntimeException e) {
            throw e;
         } catch (InvocationTargetException e) {
            if (e.getCause() instanceof RuntimeException) {
               throw (RuntimeException) e.getCause();
            }
         } catch (Exception e) {
            throw new RuntimeException(e);
         }
      }
      return result;
   }

   @SuppressWarnings("unchecked")
   public static <T, V> List<T> select(V value, Matcher<V> matcher) {
      try {
         return (List<T>) select(localItems.get(), value, matcher);
      } finally {
         localItems.remove();
      }
   }

   @SuppressWarnings("cast")
   public final static <T> T where(Class<T> cls) {
      return (T) MethodCapture.capture(cls);
   }

   @SuppressWarnings("cast")
   public final static <T> T from(Iterable<T> items) {
      Class<T> type = typeOfIterable(items);
      localItems.set(items);
      return (T) MethodCapture.capture(type);
   }

   @SuppressWarnings("unchecked")
   private static final <V> CapturingProxy<V> getCurrentCapture(V value) {
      return (CapturingProxy<V>) MethodCapture.clearAndReturn();
   }

   @SuppressWarnings("unchecked")
   private static final <T> Class<T> typeOfIterable(Iterable<T> items) {
      if (items instanceof Typed) {
         return ((Typed) items).type();
      }
      
      Iterator<T> it = items.iterator();

      if (!it.hasNext()) {
         throw new IllegalArgumentException("Unable to use from() clause on empty collections");
      }

      return (Class<T>) it.next().getClass();
   }
}
