package com.logicalpractice.collections;

import static java.lang.String.format;

import java.util.List;

public class CollectionUtils {
   private CollectionUtils(){
      // prevent construction
   }
   
   public static final void checkTypesOf(Class<?> cls, Iterable<?> iterable){
      for (Object obj : iterable) {
         checkType(cls, obj);
      }
   }
   
   public static final void checkType(Class<?> cls, Object obj){
      if( !cls.isAssignableFrom(obj.getClass())){
         throw new IllegalArgumentException(format("obj of type : %s found was expecting type : %s"));
      }
   }
   
   public static final<T> List<T> typedList(List<T> list, Class<T> type){
      return new TypedList<T>(list, type);
   }
   
   public static final<T> List<T> enforcingTypedList(List<T> list, Class<T> type){
      return new TypedList<T>(list, type);
   }
}
