package com.logicalpractice.collections;

import net.sf.cglib.proxy.Enhancer;

/**
 * Abstract script class for processing elements of a collection.
 * <p>
 * Designed to be used in a similar manner to the <a href="http://www.jmock.org/">JMock</a> 
 * <a href="http://www.jmock.org/javadoc/2.4.0/org/jmock/Expectations.html">Expectations class</a>. 
 * The puesdo script is written in the initializer block and must call, T each(Class<T>) then record 
 * method calls on that object. The evaluate method will then replay the calls on the given object. 
 * </p>  
 * For example:
 * <pre>
 *    List<Person> output = Selector.select(collectionOfPeople, new Script<String>() {{
 *          each(Person.class).getLastName();
 *       }}, equalTo("Smith"));
 * </pre>
 * @author gareth
 * @param V the Type of the evaluation
 */
public abstract class Script<V> {

	private CapturingProxy<V> elementProxy ;
	
	public V evaluate(Object object) throws Exception {
		return elementProxy.replay(object) ;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T each(Class<T> cls){
		elementProxy = new CapturingProxy<V>();
		return (T) Enhancer.create(cls, elementProxy);
	}
}
