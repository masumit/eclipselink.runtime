/*******************************************************************************
 * Copyright (c) 2011, 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0 
 * which accompanies this distribution. 
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at 
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *      gonural - initial 
 ******************************************************************************/
package org.eclipse.persistence.jpars.test.util;

import javax.persistence.EntityManager;

public class DBUtils {

    /**
     * Instantiates a new dB utils.
     */
    public DBUtils() {

    }

    /**
     * Db delete.
     *
     * @param object the object
     * @param em the em
     */
    public static void dbDelete(Object object, EntityManager em) {
        em.getTransaction().begin();
        Object merged = em.merge(object);
        //em.refresh(merged);
        em.remove(merged);
        em.getTransaction().commit();
    }

    /**
     * Db create.
     *
     * @param object the object
     * @param em the em
     */
    public static void dbCreate(Object object, EntityManager em) {
        em.getTransaction().begin();
        em.persist(object);
        em.getTransaction().commit();
    }

    /**
     * Db read.
     *
     * @param <T> the generic type
     * @param id the id
     * @param resultClass the result class
     * @param em the em
     * @return the t
     */
    public static <T> T dbRead(Object id, Class<T> resultClass, EntityManager em) {
        em.getEntityManagerFactory().getCache().evictAll();
        return em.find(resultClass, id);
    }

    /**
     * Db update.
     *
     * @param object the object
     * @param em the em
     */
    public static void dbUpdate(Object object, EntityManager em) {
        em.getTransaction().begin();
        em.merge(object);
        em.getTransaction().commit();
    }
}
