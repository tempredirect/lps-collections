package com.logicalpractice.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class CollectionUtilsTest {

   @Test
   public void checkType() {
      CollectionUtils.checkType(String.class, "Blah");
   }
   
   @Test(expected=IllegalArgumentException.class)
   public void checkTypeThrowsIllegalArgumentException() throws Exception {
      CollectionUtils.checkType(String.class, new Integer(666));
   }

   @Test(expected=IllegalArgumentException.class)
   public void checkTypeThrowsOnSuperClasses() throws Exception {
      CollectionUtils.checkType(String.class, new Object());
   }
   
   @Test
   public void checkTypesOf() throws Exception {
      List<String> objs = Arrays.asList("Thing", "That", "Stuff");
      
      CollectionUtils.checkTypesOf(String.class, objs);
   }

   @Test(expected=IllegalArgumentException.class)
   public void checkTypesOfThrowsIllegalArgumentException() throws Exception {
      List<Object> objs = new ArrayList<Object>();
      objs.add("thing");
      objs.add(new Integer(100));
      
      CollectionUtils.checkTypesOf(String.class, objs);
   }
}
