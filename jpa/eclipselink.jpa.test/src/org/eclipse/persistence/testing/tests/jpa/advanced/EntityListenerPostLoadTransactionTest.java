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
 *     Jan 8, 2009-1.1 Chris Delahunt 
 *       - Bug 244802: PostLoad callback getting invoked twice 
 ******************************************************************************/  
package org.eclipse.persistence.testing.tests.jpa.advanced;

import org.eclipse.persistence.testing.models.jpa.advanced.Employee;
import org.eclipse.persistence.testing.models.jpa.advanced.EmployeeListener;

/**
 * Tests the @PostLoad event from an EntityListener is fired only once when in a transaction.
 *
 * @author Chris Delahunt
 */
public class EntityListenerPostLoadTransactionTest extends CallbackEventTest {
    public void test() throws Exception {
        beginTransaction();
        m_beforeEvent = EmployeeListener.POST_LOAD_COUNT;
        
        getEntityManager().find(Employee.class, m_employee.getId());
        
        m_afterEvent = EmployeeListener.POST_LOAD_COUNT;
        this.rollbackTransaction();
    }
}
