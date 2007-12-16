package com.logicalpractice.collections.support;


import net.sf.cglib.proxy.Enhancer;

public class MethodCapture {

   private final static ThreadLocal<CapturingProxy<?>> context = new ThreadLocal<CapturingProxy<?>>();
   
   @SuppressWarnings("unchecked")
   public final static <T> T capture(Class<T> cls){
      context.set(new CapturingProxy<Object>());
      
      return (T) Enhancer.create(cls, context.get());      
   }
   
   public final static CapturingProxy<?> clearAndReturn(){
      CapturingProxy<?> capture = context.get();
      context.set(null);
      return capture ;
   }
}
