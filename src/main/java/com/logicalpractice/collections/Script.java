package com.logicalpractice.collections;

import net.sf.cglib.proxy.Enhancer;

public class Script<V> {

	private CapturingProxy<V> elementProxy ;
	
	public V evalate(Object object) throws Exception {
		return elementProxy.replay(object) ;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T each(Class<T> cls){
		elementProxy = new CapturingProxy<V>();
		return (T) Enhancer.create(cls, elementProxy);
	}
}
