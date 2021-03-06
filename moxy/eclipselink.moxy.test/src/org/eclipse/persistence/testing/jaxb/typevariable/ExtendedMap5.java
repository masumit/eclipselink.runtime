/*******************************************************************************
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
 * which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     Blaise Doughan - 2.5.1 - initial implementation
 ******************************************************************************/
package org.eclipse.persistence.testing.jaxb.typevariable;

import java.util.LinkedHashMap;
import java.util.Map;

public class ExtendedMap5<KEY extends Foo, VALUE extends Bar> extends LinkedHashMap<KEY, VALUE> {

    @Override
    public boolean equals(Object obj) {
        if(null == obj || obj.getClass() != this.getClass()) {
            return false;
        }
        Map<KEY, VALUE >test = (Map<KEY, VALUE>) obj;
        if(size() != test.size()) {
            return false;
        }
        for(Map.Entry<KEY, VALUE> entry : entrySet()) {
            if(!entry.getValue().equals(test.get(entry.getKey()))) {
                return false;
            }
        }
        return true;
    }

}
