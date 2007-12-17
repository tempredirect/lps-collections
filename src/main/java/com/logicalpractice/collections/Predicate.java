/*
 * Predicate.java Created on 17 Dec 2007
 *
 * All rights reserved Logical Practice Systems Limited 2007
 */
package com.logicalpractice.collections;

/**
 * @author gareth
 */
public interface Predicate<E> {

   boolean evaluate(E o);
}
