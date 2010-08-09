package com.logicalpractice.collections;

import com.google.common.base.Function;
import com.logicalpractice.collections.support.CapturingProxy;

import net.sf.cglib.proxy.Enhancer;

/**
 * Abstract script class for processing elements of a collection.
 * <p>
 * Designed to be used in a similar manner to the <a href="http://www.jmock.org/">JMock</a> 
 * <a href="http://www.jmock.org/javadoc/2.4.0/org/jmock/Expectations.html">Expectations class</a>. 
 * The puesdo script is written in the initializer block and must call, T each(Class<T>) then record 
 * method calls on that object. The apply method will then replay the calls on the given object.
 * </p>  
 * For example:
 * <pre>
 *    List<Person> output = Selector.select(collectionOfPeople, new Expression<String>() {{
 *          each(Person.class).getLastName();
 *       }}, equalTo("Smith"));
 * </pre>
 * @author gareth
 * @param <F> From type
 * @param <T> To Type
 */
public abstract class Expression<F,T> implements Function<F,T> {

	private CapturingProxy<F,T> elementProxy ;
	
    public T apply(F object)  {
        try {
      return elementProxy.replay(object) ;
    } catch (Exception e) {
      throw new ExpressionException(e);
    }
  }
	
	@SuppressWarnings("unchecked")
	public F each(Class<F> cls){
		elementProxy = new CapturingProxy<F,T>();
		return (F) Enhancer.create(cls, elementProxy);
	}

  private static class ExpressionException extends RuntimeException {
    public ExpressionException(Exception e) {
      super(e);
    }
  }
}
