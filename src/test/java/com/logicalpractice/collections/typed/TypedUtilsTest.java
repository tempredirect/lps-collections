package com.logicalpractice.collections.typed;

import java.util.*;

import com.logicalpractice.collections.typed.TypedUtils;
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

public class TypedUtilsTest {

   @Test
   public void checkType() {
      TypedUtils.checkType(String.class, "Blah");
   }
   
   @Test(expected=IllegalArgumentException.class)
   public void checkTypeThrowsIllegalArgumentException() throws Exception {
      TypedUtils.checkType(String.class, 666);
   }

   @Test(expected=IllegalArgumentException.class)
   public void checkTypeThrowsOnSuperClasses() throws Exception {
      TypedUtils.checkType(String.class, new Object());
   }
   
   @Test
   public void checkTypesOf() throws Exception {
      List<String> objs = Arrays.asList("Thing", "That", "Stuff");
      
      TypedUtils.checkTypesOf(String.class, objs);
   }

   @Test(expected=IllegalArgumentException.class)
   public void checkTypesOfThrowsIllegalArgumentException() throws Exception {
      List<Object> objs = new ArrayList<Object>();
      objs.add("thing");
      objs.add(100);
      
      TypedUtils.checkTypesOf(String.class, objs);
   }

    @Test
    public void typedListReturnsNoneRandomAccess(){
        List<String> result = TypedUtils.typedList(new LinkedList<String>(), String.class);
        assertThat(result, not(instanceOf(RandomAccess.class)));
    }
    @Test
    public void typedListReturnsRandomAccess(){
        List<String> result = TypedUtils.typedList(new ArrayList<String>(), String.class);
        assertThat(result, instanceOf(RandomAccess.class));
    }
    @Test
    public void enforcingTypedListReturnsNoneRandomAccess(){
        List<String> result = TypedUtils.enforcingTypedList(new LinkedList<String>(), String.class);
        assertThat(result, not(instanceOf(RandomAccess.class)));
    }
    @Test
    public void enforcingTypeListReturnsRandomAccess(){
        List<String> result = TypedUtils.enforcingTypedList(new ArrayList<String>(), String.class);
        assertThat(result, instanceOf(RandomAccess.class));
    }
}
