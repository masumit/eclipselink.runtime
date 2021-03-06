/*******************************************************************************
 * Copyright (c) 2011, 2013 Oracle and/or its affiliates. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
 * which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     Denise Smith - September 2013
 ******************************************************************************/
package org.eclipse.persistence.testing.jaxb.annotations.xmlelementdecl.xsitype;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.persistence.platform.xml.XMLComparer;
import org.w3c.dom.Node;

@XmlType(name = "ExampleType")
public class ExampleType {

	@XmlElement(name = "Content")
    public Object content;
    
    public boolean equals(Object obj) {
    	if(obj instanceof ExampleType){
    		if(content != null && content instanceof Node){
    			XMLComparer comparer = new XMLComparer();
    			return comparer.isNodeEqual((Node)content,(Node) ((ExampleType)obj).content);
    		}
    		return (content == null && ((ExampleType)obj).content == null) 
    		 || (content != null && content.equals(((ExampleType)obj).content));
    	}
    	return false;
    }
}
 
    
