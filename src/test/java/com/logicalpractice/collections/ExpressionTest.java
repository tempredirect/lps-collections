package com.logicalpractice.collections;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.* ;
import org.junit.Test;

public class ExpressionTest {

   @Test
   public void script1() throws Exception {
      
      Person billy = new Person("Billy", "Smith");
      
      Expression<Person,String> testObject = new Expression<Person,String>(){{
         each(Person.class).getFirstName();
      }};
      
      String result = testObject.apply(billy);
      
      assertThat(result, equalTo("Billy"));
   }
}
