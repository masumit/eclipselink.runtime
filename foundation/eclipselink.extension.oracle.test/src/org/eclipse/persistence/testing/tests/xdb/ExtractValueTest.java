/*******************************************************************************
 * Copyright (c) 1998, 2013 Oracle and/or its affiliates. All rights reserved.
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0 
 * which accompanies this distribution. 
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at 
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     Oracle - initial API and implementation from Oracle TopLink
 ******************************************************************************/  
package org.eclipse.persistence.testing.tests.xdb;

import org.eclipse.persistence.expressions.*;
import org.eclipse.persistence.platform.database.oracle.Oracle9Platform;
import org.eclipse.persistence.testing.framework.*;

public class ExtractValueTest extends TestCase {
    Employee_XML employee = null;

    public ExtractValueTest() {
        setDescription("Tests the use of the extractVale function");
    }

    public void setup() {
        if (!(getSession().getPlatform() instanceof Oracle9Platform)) {
            throw new TestWarningException("This test is intended for the Oracle 9 platform");
        }
    }

    public void reset() {
    }

    public void test() {
        employee = (Employee_XML)getSession().readObject(Employee_XML.class, new ExpressionBuilder().get("resume").extractValue("/resume/first-name/text()").equal("Bob"));
    }

    public void verify() {
        if ((employee == null) || !(employee.firstName.equals("Bob"))) {
            throw new TestErrorException("Wrong or No Employee returned");
        }
    }
}
