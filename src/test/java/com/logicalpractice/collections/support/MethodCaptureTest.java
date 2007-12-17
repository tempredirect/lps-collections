/*
 * MethodCaptureTest.java Created on 17 Dec 2007
 *
 * All rights reserved Logical Practice Systems Limited 2007
 */
package com.logicalpractice.collections.support;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Test;

/**
 * @author gareth
 */
public class MethodCaptureTest {

   static class NormalClass {
      String thing(){
         return "code thing";
      }
   }
   static abstract class AbstractClass { 
      abstract void implementMe() ;
   }
   
   static final class FinalClass {
      void stupidMethod(){
         System.out.println("you called me?");
      }
   }
   
   static class NoDefaultConstructorClass{
      NoDefaultConstructorClass(String value1){
         assert value1 != null ;
      }
   }

   /**
    * Test method for {@link com.logicalpractice.collections.support.MethodCapture#capture(java.lang.Class)}.
    */
   @Test
   public void captureAbstractClass() {
      AbstractClass implmentation = MethodCapture.capture(AbstractClass.class);
      assertThat(implmentation, not(nullValue()));
   }

   @Test(expected=IllegalArgumentException.class)
   public void captureFinalClass() throws Exception {
      MethodCapture.capture(FinalClass.class);
   }
   
   @Test(expected=IllegalArgumentException.class)
   public void capturePrimitive() throws Exception {
      MethodCapture.capture(int.class);
   }
   
   @Test
   public void captureNormalClass() throws Exception {
      NormalClass impl = MethodCapture.capture(NormalClass.class);
      assertThat(impl, not(nullValue()));

      // the impl version of thing should never return the value 
      // returned by a real
      assertThat(impl.thing(), not(equalTo(new NormalClass().thing())));
   }

   @Test(expected=IllegalArgumentException.class)
   public void captureNoDefaultConstructor() throws Exception {
      MethodCapture.capture(NoDefaultConstructorClass.class);
   }
   
   @After
   public void clearAndReturn() {
      // just to make sure we clear up after our selves.
      MethodCapture.clearAndReturn();
   }

}
