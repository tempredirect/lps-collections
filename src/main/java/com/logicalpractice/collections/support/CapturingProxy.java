package com.logicalpractice.collections.support;

import static java.lang.Thread.currentThread;
import static java.lang.reflect.Modifier.isFinal;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class CapturingProxy<T> implements MethodInterceptor {

   private static class CapturedMethodInvocation {
      private final Method method;
      private final Object[] args;

      CapturedMethodInvocation(Method method, Object[] args) {
         this.method = method;
         this.args = args;
      }

      public Object invoke(Object obj) throws Exception {
         return method.invoke(obj, args);
      }

      @Override
      public String toString() {
         return new StringBuilder()
               .append("CapturedMethodInvocation: method:")
                  .append(method)
                  .append(" args:")
                  .append(Arrays.asList(args))
                  .toString();
      }
   }

   // fields ****************************************************************

   private List<CapturedMethodInvocation> invocations = new LinkedList<CapturedMethodInvocation>();

   private final Thread createdOn ;
   
   // constructor ***********************************************************
   
   public CapturingProxy() {
      this.createdOn = currentThread();
   }
   // methods ***************************************************************

   public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
         throws Throwable {
      if( currentThread().equals(createdOn)){
         invocations.add(new CapturedMethodInvocation(method, args));
      }
      Class<?> returnType = method.getReturnType();
      if (!isFinal(returnType.getModifiers())) {
         return Enhancer.create(method.getReturnType(), this);
      }
      return null;
   }

   @SuppressWarnings("unchecked")
   public T replay(Object target) throws Exception {
      Object currentTarget = target;
      for (CapturedMethodInvocation invocation : invocations) {
         currentTarget = invocation.invoke(currentTarget);
      }
      return (T) currentTarget;
   }
}
