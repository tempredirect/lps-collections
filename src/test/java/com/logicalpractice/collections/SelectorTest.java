package com.logicalpractice.collections;

import static com.google.common.collect.Iterables.size;
import static com.google.common.collect.Lists.newArrayList;
import static com.logicalpractice.collections.Selector.by;
import static com.logicalpractice.collections.Selector.collect;
import static com.logicalpractice.collections.Selector.from;
import static com.logicalpractice.collections.Selector.select;
import static com.logicalpractice.collections.Selector.where;
import static com.logicalpractice.collections.typed.TypedUtils.typedList;
import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.junit.After;
import org.junit.Test;

import com.logicalpractice.collections.support.MethodCapture;

public class SelectorTest {

    List<Person> testData = Arrays.asList(new Person("Billy", "Smith", 8), new Person("Billy",
            "Jones", 32), new Person("James", "Smith", 70));

    @Test
    public void selectAllSmiths() throws Exception {

        List<Person> output = newArrayList(select(testData, new Expression<Person, String>() {
            {
                each(Person.class).getLastName();
            }
        }, equalTo("Smith")));

        assertThat(output.size(), equalTo(2));
    }

    @Test
    public void selectAllSmiths2() throws Exception {
        List<Person> output = newArrayList(select(testData, where(Person.class).getLastName(), equalTo("Smith")));
        assertThat(output.size(), equalTo(2));
    }

    @Test
    public void selectAllSmiths3() throws Exception {
        Iterable<Person> output = select(from(testData).getLastName(), equalTo("Smith"));
        assertThat(size(output), equalTo(2));
    }

    @Test
    public void selectAllBillys() throws Exception {
        Iterable<Person> output = select(from(testData).getFirstName(), equalToIgnoringCase("billy"));
        assertThat(size(output), equalTo(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromThrowsIllegalArgumentException() throws Exception {
        List<String> values = Collections.emptyList();
        from(values);
    }

    @Test
    public void selectAllowsTypedZeroLengthInput() throws Exception {
        List<Person> values = Collections.emptyList();
        values = typedList(values, Person.class);
        Iterable<Person> results = select(from(values).getFirstName(), equalTo("Thing"));

        assertThat(size(results), equalTo(0));
    }

    @Test
    public void selectUsingPredicate() {
        final AtomicInteger evaluateCount = new AtomicInteger(0);

        Iterable<Person> result = select(testData, new Predicate<Person>() {
            public boolean apply(Person p) {
                evaluateCount.incrementAndGet();
                return p.getLastName().matches("[Ss]mith");
            }
        });
        assertThat(size(result), equalTo(2));
        assertThat(evaluateCount.intValue(), equalTo(3));
    }

    @Test
    public void simpleSelect() throws Exception {
        Integer[] values = {1, 3, 7, 9, 11, 19};

        Iterable<Integer> result = select(asList(values), lessThan(10));

        assertThat(result, not(nullValue()));
        assertThat(result, hasItems(1, 3, 7, 9));
        assertThat(result, not(hasItems(11, 19)));
    }

    @Test
    public void scriptCollectOperation() {

        // collects all the first names
        Iterable<String> result = collect(testData, new Expression<Person, String>() {{
            each(Person.class).getFirstName();
        }});

        assertThat(result, not(nullValue()));
        assertThat(result, hasItems("Billy", "James"));
        assertThat(size(result), equalTo(3));
    }

    @Test
    public void scriptCollectOperation2() {

        // collects all the first names
        Iterable<String> result = collect(testData, new Expression<Person, String>() {
            {
                each(Person.class).getLastName();
            }
        });

        assertThat(result, not(nullValue()));
        assertThat(result, hasItems("Smith", "Jones"));
        assertThat(size(result), equalTo(3));
    }

    @Test
    public void byCollectOperation() {

        Iterable<String> result = collect(testData, by(Person.class).getLastName());

        assertThat(result, not(nullValue()));
        assertThat(result, hasItems("Smith", "Jones"));
        assertThat(size(result), equalTo(3));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void checkPrimitiveMethod() throws Exception {

        Iterable<Person> workingAgeAdults = select(from(testData).getAge(), allOf(greaterThan(18),
                lessThan(65)));

        assertThat(size(workingAgeAdults), greaterThan(0));
    }

    @Test(expected = NullPointerException.class)
    public void verifyNullPointerThrown() throws Exception {
        select(testData, where(Person.class).getFirstName()
                .substring(0, 1), equalTo("s"));
    }

    @Test
    public void collectFromOperation() {
        Iterable<String> result = collect(from(testData).getLastName());

        assertThat(result, not(nullValue()));
        assertThat(result, hasItems("Smith", "Jones"));
        assertThat(size(result), equalTo(3));
    }

    @Test
    public void ensureSelectIsLazy(){
        final AtomicInteger count = new AtomicInteger(0);
        Iterable<Person> result = select(testData, new Predicate<Person>() {
            public boolean apply(Person input) {
                count.incrementAndGet();
                return true;
            }
        });

        Iterator<Person> it = result.iterator();

        assertThat(count.get(),equalTo(0));

        it.next();

        assertThat(count.get(), equalTo(1));

        it.next();

        assertThat(count.get(), equalTo(2));
    }

    @After
    public void clearAndReturn() {
        MethodCapture.clearAndReturn();
    }
}
