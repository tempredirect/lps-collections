/*
 * Address.java Created on 12 Jan 2008
 *
 * All rights reserved Logical Practice Systems Limited 2008
 */
package com.logicalpractice.collections;

/**
 * @author gareth
 */
public class Address {

   private String street ;
   private String town ;
   private String postcode ;
   
   public String getStreet() {
      return street;
   }
   public void setStreet(String street) {
      this.street = street;
   }
   public String getTown() {
      return town;
   }
   public void setTown(String town) {
      this.town = town;
   }
   public String getPostcode() {
      return postcode;
   }
   public void setPostcode(String postcode) {
      this.postcode = postcode;
   }
}
