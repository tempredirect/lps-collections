package com.logicalpractice.collections;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import org.junit.Test;

import com.logicalpractice.collections.typed.EnforcingTypedList;
import com.logicalpractice.collections.typed.Typed;

@SuppressWarnings("unchecked")
public class EnforcingTypedListTest {

   List<String> underLyingList = Arrays.asList("Thing", "That", "Peter", "Said", "To", "Dave");
   
   List testObject = new EnforcingTypedList<String>(underLyingList, String.class);
   
   @Test(expected=IllegalArgumentException.class)
   public void testAddIntT() {
      testObject.add(1, new Integer(666));
   }

   @Test(expected=IllegalArgumentException.class)
   public void testAddT() {
      testObject.add(new Double(2.3));
   }

   @Test(expected=IllegalArgumentException.class)
   public void testAddAllCollectionOfQextendsT() {
      testObject.addAll(asList(1,2,3,4));
   }

   @Test(expected=IllegalArgumentException.class)
   public void testAddAllIntCollectionOfQextendsT() {
      testObject.addAll(2,asList(1,2,3,4));
   }

   @Test(expected=IllegalArgumentException.class)
   public void testListIteratorSet() {
      ListIterator li = testObject.listIterator() ;
      li.next();
      li.set(new Integer(1));
   }

   @Test(expected=IllegalArgumentException.class)
   public void testListIteratorAdd() {
      ListIterator li = testObject.listIterator() ;
      li.next();
      li.add(new Integer(1));
   }

   @Test(expected=IllegalArgumentException.class)
   public void testListIteratorIntAndSet() {
      ListIterator li = testObject.listIterator(3) ;
      li.next();
      li.set(new Integer(1));
   }

   @Test(expected=IllegalArgumentException.class)
   public void testListIteratorIntAndAdd() {
      ListIterator li = testObject.listIterator(3) ;
      li.next();
      li.add(new Integer(1));
   }

   @Test(expected=IllegalArgumentException.class)
   public void testSet() {
      testObject.set(0, new Object());
   }

   public void testSubList() {
      List sublist = testObject.subList(0, 2);
      assertThat(sublist, is(EnforcingTypedList.class));
      assertThat(((Typed)sublist).type(), equalTo(String.class));
   }

}
