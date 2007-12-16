package com.logicalpractice.collections;

import static com.logicalpractice.collections.CollectionUtils.typedList;
import static com.logicalpractice.collections.Selector.from;
import static com.logicalpractice.collections.Selector.select;
import static com.logicalpractice.collections.Selector.where;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;

import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class SelectorTest {

   List<Person> testData = Arrays.asList(new Person("Billy", "Smith"),
         new Person("Billy", "Jones"), new Person("James", "Smith"));

   @Test
   public void selectAllSmiths() throws Exception {

      List<Person> output = select(testData, new Script<String>() {
         {
            each(Person.class).getLastName();
         }
      }, equalTo("Smith"));

      assertThat(output.size(), equalTo(2));
   }
   
   @Test
   public void selectAllSmiths2() throws Exception {
      List<Person> output = select(testData, where(Person.class).getLastName(), equalTo("Smith"));
      assertThat(output.size(), equalTo(2));
   }

   @Test
   public void selectAllSmiths3() throws Exception {
      List<Person> output = select(from(testData).getLastName(), equalTo("Smith"));
      assertThat(output.size(), equalTo(2));
   }

   @Test
   public void selectAllBillys() throws Exception {
      List<Person> output = select(from(testData).getFirstName(), equalToIgnoringCase("billy"));
      assertThat(output.size(), equalTo(2));
   }

   @Test(expected=IllegalArgumentException.class)
   public void fromThrowsIllegalArgumentException() throws Exception {
      List<String> testData = Collections.emptyList();
      from(testData);
   }
   
   @Test
   public void selectAllowsTypedZeroLengthInput() throws Exception {
      List<Person> testData = Collections.emptyList();
      testData = typedList(testData, Person.class);
      List<Person> results = select(from(testData).getFirstName(), equalTo("Thing"));
      
      assertThat(results.isEmpty(), equalTo(true));
   }
   
}
