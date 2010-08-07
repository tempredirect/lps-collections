package com.logicalpractice.collections;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.* ;
import org.junit.Test;

public class ScriptTest {

   @Test
   public void script1() throws Exception {
      
      Person billy = new Person("Billy", "Smith");
      
      Script<Person,String> testObject = new Script<Person,String>(){{
         each(Person.class).getFirstName();
      }};
      
      String result = testObject.evaluate(billy);
      
      assertThat(result, equalTo("Billy"));
   }
}
