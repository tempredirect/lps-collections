package com.logicalpractice.collections.typed;

import static java.lang.String.format;

import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;
import java.util.Set;

import com.logicalpractice.collections.typed.*;

/**
 * Util functions for wrapping collections in Typed collection interfaces.
 *
 * @author gareth
 */
public class TypedUtils {

   private TypedUtils() {
      // prevent construction
   }

   public static void checkTypesOf(Class<?> cls, Iterable<?> iterable) {
      for (Object obj : iterable) {
         checkType(cls, obj);
      }
   }

   public static void checkType(Class<?> cls, Object obj) {
      if (!cls.isAssignableFrom(obj.getClass())) {
         throw new IllegalArgumentException(format("obj of type : %s found was expecting type : %s"));
      }
   }

   public static <T> Iterable<T> typed(Iterable<T> iterable, Class<T> type){
      return new TypedIterable<T>(iterable, type);
   }
   
   public static <T> List<T> typedList(List<T> list, Class<T> type) {
       return list instanceof RandomAccess ? new RandomAccessTypedList<T>(list,type): new TypedList<T>(list, type);
   }

   public static <T> List<T> enforcingTypedList(List<T> list, Class<T> type) {
      return list instanceof RandomAccess ? new RandomAccessEnforcingTypedList<T>(list,type) : new EnforcingTypedList<T>(list, type);
   }
   
   public static <T> Collection<T> typedCollection(Collection<T> collection, Class<T> type){
      return new TypedCollection<T>(collection, type);
   }
   
   public static <T> Set<T> typedSet(Set<T> set, Class<T> type){
      return new TypedSet<T>(set, type);
   }
}
