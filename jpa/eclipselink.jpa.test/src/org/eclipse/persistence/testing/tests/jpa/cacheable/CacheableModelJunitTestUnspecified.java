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
 *     06/16/2009-2.0 Guy Pelletier 
 *       - 277039: JPA 2.0 Cache Usage Settings
 *     07/16/2009-2.0 Guy Pelletier 
 *       - 277039: JPA 2.0 Cache Usage Settings
 *     06/09/2010-2.0.3 Guy Pelletier 
 *       - 313401: shared-cache-mode defaults to NONE when the element value is unrecognized
 ******************************************************************************/  
package org.eclipse.persistence.testing.tests.jpa.cacheable;

import junit.framework.*;

import org.eclipse.persistence.sessions.server.ServerSession;
import org.eclipse.persistence.testing.framework.junit.JUnitTestCase;
import org.eclipse.persistence.testing.models.jpa.cacheable.CacheableTableCreator;

/*
 * The test is testing against "MulitPU-5" persistence unit which has <shared-cache-mode> to be UNSPECIFIED
 */
public class CacheableModelJunitTestUnspecified extends CacheableModelJunitTest {
    
    public CacheableModelJunitTestUnspecified() {
        super();
    }
    
    public CacheableModelJunitTestUnspecified(String name) {
        super(name);
        setPuName("MulitPU-5");
    }
    
    public void setUp() {
        clearCache("MulitPU-5");
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.setName("CacheableModelJunitTestUnspecified");

        if (! JUnitTestCase.isJPA10()) {
            suite.addTest(new CacheableModelJunitTestUnspecified("testSetup"));
            suite.addTest(new CacheableModelJunitTestUnspecified("testCachingOnUNSPECIFIED"));
        }
        return suite;
    }
    
    /**
     * The setup is done as a test, both to record its failure, and to allow execution in the server.
     */
    public void testSetup() {
        new CacheableTableCreator().replaceTables(JUnitTestCase.getServerSession("MulitPU-5"));
        clearCache("MulitPU-5");
    }
    
    /**
     * Convenience method.
     */
    @Override
    public ServerSession getPUServerSession(String puName) {
        return JUnitTestCase.getServerSession("MulitPU-5");
    }
}
