/*******************************************************************************
 * Copyright (c) 1998, 2012 Oracle and/or its affiliates. All rights reserved.
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0 
 * which accompanies this distribution. 
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at 
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     Denise Smith  February, 2013
 ******************************************************************************/
package org.eclipse.persistence.testing.jaxb.map;

public class ComplexValue {

	public String thing1;
    public String thing2 ;
    
    public ComplexValue(){
		
	}
	public ComplexValue(String thing1, String thing2){
		this.thing1 = thing1;
		this.thing2 = thing2;
	}
	
	 public boolean equals(Object obj){
	    	if(!(obj instanceof ComplexValue)) {
	    		return false;
	    	}
	    	ComplexValue compare = (ComplexValue)obj;
	    	return thing1.equals(compare.thing1) && thing2.equals(compare.thing2);
	  }
}
