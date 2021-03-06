package com.logicalpractice.collections;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import org.hamcrest.Matcher;

import com.logicalpractice.collections.support.CapturingProxy;
import com.logicalpractice.collections.support.MethodCapture;
import com.logicalpractice.collections.typed.Typed;

/**
 * Main set of utility functions for Collections.
 * <p>
 * There are two groups of methods, first is the actual utility methods, such as
 * {@link #select(Object, Matcher) select()} and {@link #collect(Object)} and the
 * the support methods such as {@link #where(Class)} and {@link #from(Iterable)}
 * with can only be used in combination with the utility methods.
 * </p>
 * <p>
 * Typicaly each utility method comes with three versions. In order of
 * complexity: one that accepts a {@link Function} object, second that can be used
 * in conjunction with a clause function like {@link #where(Class)}, a third
 * that seemingly doesn't even accept a collection as an argument these need to
 * be used in conjunction with a source method such as {@link #from(Iterable)}
 * </p>
 *
 * @author gareth
 */
public class Selector {

    private static final ThreadLocal<Iterable<?>> localItems = new ThreadLocal<Iterable<?>>();

    private static Field checkedCollectionTypeField;

    static {
        Collection<String> tmp = Collections.checkedCollection(new ArrayList<String>(1),String.class);
        try {
            checkedCollectionTypeField = tmp.getClass().getDeclaredField("type");
            checkedCollectionTypeField.setAccessible(true);
        } catch (SecurityException e){
            //ignore
        } catch (NoSuchFieldException e) {
            //ignore
        }
    }

    /**
     * Select matching items from Iterable items by evalulating the Function object
     * for each element.
     *
     * @param <T>       Type of Iterable elements
     * @param <V>       Type of the value used for the matcher
     * @param items     None null instance of Iterable
     * @param transform None null instance of Function that excepts type T and returns a
     *                  Value of type V
     * @param matcher   Hamcrest matcher that if evalulated to true will result in the
     *                  element being returned in the result
     * @return A list of selected elements, note that the list will be
     *         independant of the source items and that items will not be changed
     *         during this operation.
     */
    public static <T, V> Iterable<T> select(Iterable<T> items, final Function<T, V> transform, final Matcher<V> matcher) {
        return select(items, new Predicate<T>() {
            public boolean apply(T input) {
                return matcher.matches(transform.apply(input));
            }
        });
    }

    /**
     * Select matching items from Iterable items using a where clause.
     * <p>
     * This method is similar in operation to {@link #select(Object, Matcher)}
     * except that it will accept an empty untyped list with out any problems.
     * The only real down side is that is requires an extra parameter, which
     * given you may need to
     * </p>
     * Example:
     * <p/>
     * <pre>
     * select(listOfPeople, where(Person.class).getFirstName(), equalsIgnoreCase(&quot;smith&quot;));
     * </pre>
     *
     * @param <T>         Type of Iterable elements
     * @param <V>         Type of the value used for the matcher
     * @param items       None null instance of Iterable
     * @param whereclause return value from the where(ClassName.class) script like thing,
     *                    the actual value isn't particalarly interesting, as it will just
     *                    be thrown away.
     * @param matcher     Hamcrest matcher that if evalulated to true will result in the
     *                    element being returned in the result
     * @return A list of selected elements, note that the list will be
     *         independant of the source items and that items will not be changed
     *         during this operation.
     */
    public static <T, V> Iterable<T> select(Iterable<T> items, V whereclause, Matcher<V> matcher) {
        List<T> result = new LinkedList<T>();
        CapturingProxy<T, V> proxy = getCurrentCapture(whereclause);

        for (T item : items) {
            try {
                if (matcher.matches(proxy.replay(item))) {
                    result.add(item);
                }
            } catch (Exception e) {
                throw launderException(e);
            }
        }
        return result;
    }

    /**
     * Select matching items from Iterable items by evalulating the current chain
     * of CapturingProxy's.
     * <p>
     * This method is designed to be used in conjunction with the source method
     * {@link #from(Iterable)}. <br/> For example:
     * </p>
     * <p/>
     * <pre>
     *    import static com.logicalpractice.collections.Selector.* ;
     *    import static org.hamcrest.Matchers.* ;
     * <p/>
     *    List&lt;Person&gt; output = select(from(listOfPeople).getLastName(), equalTo(&quot;Smith&quot;));
     * </pre>
     * <p/>
     * <p>
     * There are a number of limitations with this seemly magic method. If the
     * runtime type of the source list (listOfPeople in the above example) then
     * an IllegalArgumentException is raised (by from()).
     * </p>
     * <p>
     * There are two ways of providing runtime type information to the
     * Selector, first is to supply a none empty list, the first
     * element will be used as the prototype for CapturingProxy. An Alternative
     * is to use an implementation of {@link Typed}, Typed implementations of
     * the collection classes can be obtained with via
     * {@link com.logicalpractice.collections.typed.TypedUtils#typedCollection(java.util.Collection, Class)}},
     * {@link com.logicalpractice.collections.typed.TypedUtils#typedList(java.util.List,Class)} and
     * {@link com.logicalpractice.collections.typed.TypedUtils#typedSet(java.util.Set,Class)}
     * </p>
     * <p/>
     * <pre>
     *    import static com.logicalpractice.collections.typed.TypedUtils#typed ;
     *    import static com.logicalpractice.collections.Selector.* ;
     *    import static org.hamcrest.Matchers.* ;
     * <p/>
     *    List&lt;Person&gt; output = select(from(typed(listOfPeople,Person.class)).getLastName(), equalTo(&quot;Smith&quot;));
     * </pre>
     *
     * @param <T>     Type of Iterable elements
     * @param <V>     Type of the value used for the matcher
     * @param value   Value returned by {from(Iterable)} it's value will be ignored
     * @param matcher Hamcrest matcher that if evalulated to true will result in the
     *                element being returned in the result
     * @return A list of selected elements, note that the list will be
     *         independant of the source items and that items will not be changed
     *         during this operation.
     */
    @SuppressWarnings("unchecked")
    public static <T, V> Iterable<T> select(V value, Matcher<V> matcher) {
        try {
            return (List<T>) select(localItems.get(), value, matcher);
        } finally {
            localItems.remove();
        }
    }

    public static <T> Iterable<T> select(Iterable<T> items, final Matcher<T> matcher) {
        return select(items, is(matcher));
    }


    /**
     * Return a predicate that wraps the given matcher.
     * @param matcher required matcher
     * @param <T> type of the matcher and the resulting predicate
     * @return none null instance Predicate
     */
    public static <T> Predicate<T> is(final Matcher<T> matcher) {
        Preconditions.checkNotNull(matcher,"matcher is required");
        return new Predicate<T>() {
            public boolean apply(T input) {
                return matcher.matches(input);
            }
        };
    }

    public static <T> Iterable<T> select(final Iterable<T> items, final Predicate<T> predicate) {
        return new Iterable<T>() {
            public Iterator<T> iterator() {
                final Iterator<T> it = items.iterator();
                return Iterators.filter(it, predicate);
            }
        };
    }

    /**
     * Maps one collection to another new collection using the Function to
     * transform each value in the source collection into a value in
     * the destination.
     * <pre>
     *    List<String> names = collect(people, new Function<String>(){{
     *          each(Person.class).getFullName();
     *      }}
     * </pre>
     *
     * @param <T>      Type of the source list
     * @param <V>      type of the destination list
     * @param items    Non-null Source collection
     * @param function
     * @return a list of transformed values, the order of the elements will be in
     *         iteration order of the source list.
     */
    public static <T, V> Iterable<V> collect(Iterable<T> items, Function<T, V> function) {
        List<V> result = new LinkedList<V>();

        for (T item : items) {
            try {
                result.add(function.apply(item));
            } catch (Exception e) {
                throw launderException(e);
            }
        }
        return result;
    }

    /**
     * Collects a List of values from an Iterable. The values and type of the result are
     * controlled by the fromclause.<br/>
     * <pre>
     * </pre>
     *
     * @param <T>
     * @param <V>
     * @param items
     * @param fromclause should be used as from(YourClass.class).getProperty() the actual value
     *                   past in is ignored.
     * @return a list of transformed values, the order of the elements will be in
     *         iteration order of the source list.
     */
    public static <T, V> Iterable<V> collect(Iterable<T> items, V fromclause) {
        List<V> result = new LinkedList<V>();
        CapturingProxy<T, V> proxy = getCurrentCapture(fromclause);

        for (T item : items) {
            try {
                result.add(proxy.replay(item));
            } catch (Exception e) {
                throw launderException(e);
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <V> Iterable<V> collect(V value) {
        try {
            return collect(localItems.get(), value);
        } finally {
            localItems.remove();
        }
    }

    public static <T, V> void remove(Iterable<T> items, Function<T, V> function, Matcher<V> matcher) {
        for (Iterator<T> it = items.iterator(); it.hasNext();) {
            T item = it.next();
            try {
                if (matcher.matches(function.apply(item))) {
                    it.remove();
                }
            } catch (Exception e) {
                throw launderException(e);
            }
        }
    }

    public static <T, V> void remove(Iterable<T> items, V whereclause, Matcher<V> matcher) {
        CapturingProxy<T, V> proxy = getCurrentCapture(whereclause);

        for (Iterator<T> it = items.iterator(); it.hasNext();) {
            T item = it.next();
            try {
                if (matcher.matches(proxy.replay(item))) {
                    it.remove();
                }
            } catch (Exception e) {
                throw launderException(e);
            }
        }
    }

    public static <V> void remove(V value, Matcher<V> matcher) {
        try {
            remove(localItems.get(), value, matcher);
        } finally {
            localItems.remove();
        }
    }


    /**
     * Provides the where clause for {@link #select(Iterable, Object, Matcher)}.
     * Returns an instance of the type provided, this proxy will attempt to
     * record the subsequent method invokations in order for them to be replayed
     * later by the {@link #select(Iterable, Object, Matcher)}.
     * <p>
     * {@link com.logicalpractice.collections.support.MethodCapture#capture(java.lang.Class)} provides the proxy and it in turn uses
     * CGLIB to implement it. There are some rescitions to the use of
     * MethodCapture please refer it's documentation for more information.
     * </p>
     *
     * @param <T> type of the instance
     * @param cls none null class
     * @return Capturing instance of cls
     * @throws IllegalArgumentException if cls is null
     */
    @SuppressWarnings("cast")
    public static <T> T where(Class<T> cls) {
        if (cls == null) {
            throw new IllegalArgumentException("Null cls parameter for where(Class). cls is required");
        }
        return (T) MethodCapture.capture(cls);
    }

    /**
     * by() is just an alais for {@link #where(Class)}, but is included
     * to provide a more logical syntax.
     *
     * @param <T>
     * @param cls non-null class
     * @return Capturing instance of cls
     * @see #where(Class)
     */
    public static <T> T by(Class<T> cls) {
        return where(cls);
    }

    /**
     * from source method, to be used in conjunction with
     * {@link #select(Object, Matcher)}.
     * <p>
     * It is important to understand that this method uses a ThreadLocal to pass
     * that actual collection to the select method. If the caller fails to call
     * {@link #select(Object, Matcher)} then the reference will be left hanging,
     * and will prevent the collection from being garbage collected.
     * </p>
     * <p>
     * This method must obtain the runtime type of the contents of the Iterable.
     * There are two ways of providing runtime type information to the
     * Selector, first is to supply a none empty list, the first
     * element will be used as the prototype for CapturingProxy. An Alternative
     * is to use an implementation of {@link Typed}, Typed implementations of
     * the collection classes can be obtained with via
     * </p>
     * <p>
     * {@link com.logicalpractice.collections.support.MethodCapture#capture(Class)} provides the proxy and it in turn uses
     * CGLIB to implement it. There are some rescitions to the use of
     * MethodCapture please refer it's documentation for more information.
     * </p>
     *
     * @param <T>
     * @param items
     * @return
     */
    @SuppressWarnings("cast")
    public final static <T> T from(Iterable<T> items) {
        Class<T> type = typeOfIterable(items);
        localItems.set(items);
        return (T) MethodCapture.capture(type);
    }

    @SuppressWarnings("unchecked")
    private static <F, V> CapturingProxy<F, V> getCurrentCapture(V value) {
        return (CapturingProxy<F, V>) MethodCapture.clearAndReturn();
    }


    // visible for testing only
    @SuppressWarnings("unchecked")
    static <T> Class<T> typeOfIterable(Iterable<T> items) {
        if (items instanceof Typed) {
            return ((Typed) items).type();
        }

        if( checkedCollectionsReflectionEnabled()
                && items.getClass().getName().startsWith("java.util.Collections$Checked")){
            try {
                return (Class<T>)checkedCollectionTypeField.get(items);
            } catch (IllegalAccessException e) {
                // ignore
            }
        }

        Iterator<T> it = items.iterator();

        if (!it.hasNext()) {
            throw new IllegalArgumentException("Unable to use from() clause on empty untyped collections");
        }

        return (Class<T>) it.next().getClass();
    }

    /**
     * @return true if the {@code Selector} is enable to snif the type of
     *              collections sourced from {@code Collections.checked*}
     */
    public static boolean checkedCollectionsReflectionEnabled() {
        return checkedCollectionTypeField != null;
    }

    private static RuntimeException launderException(Exception e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        } else if (e instanceof InvocationTargetException) {
            if (e.getCause() instanceof RuntimeException) {
                return (RuntimeException) e.getCause();
            }
        }
        return new RuntimeException(e);
    }

}
