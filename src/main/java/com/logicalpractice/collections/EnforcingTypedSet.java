/*
 * EnforcingTypedSet.java Created on 16 Dec 2007
 *
 * All rights reserved Logical Practice Systems Limited 2007
 */
package com.logicalpractice.collections;

import java.util.Collection;

/**
 * @author gareth
 */
public class EnforcingTypedSet<T> extends EnforcingTypedCollection<T> {

   /**
    * @param delegate
    * @param type
    */
   public EnforcingTypedSet(Collection<T> delegate, Class<T> type) {
      super(delegate, type);
   }
}
