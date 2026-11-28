package com.souzamonteiro.nfe.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class HibernateUtil {
    
    private static final String PERSISTENCE_UNIT = "nfePU";
    private static EntityManagerFactory emf;
    
    public static EntityManager getEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    private static synchronized EntityManagerFactory getEntityManagerFactory() {
        if (emf == null || !emf.isOpen()) {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        }
        return emf;
    }
    
    public static synchronized void closeEntityManagerFactory() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}