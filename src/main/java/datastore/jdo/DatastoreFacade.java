package datastore.jdo;

import java.util.Collection;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

/**
 * Facade to JDO DataStore. Provides methods to save, load and query objects in
 * low level.
 * 
 * @author Danilo Penna Queiroz - dpenna.queiroz@gmail.com
 */
public class DatastoreFacade {

    protected PersistenceManagerPool pool = PersistenceManagerPool.getInstance();

    /**
     * Saves the given object.
     * 
     * @param persistentObject
     *            The object to be saved.
     */
    public <T> void saveObject(T persistentObject) {
        PersistenceManager pm = this.pool.getPersistenceManagerForThread();
        pm.makePersistent(persistentObject);
        pm.close();
    }

    /**
     * Save all the given objects
     * 
     * @param persistentObjects
     *            The objects to be saved.
     */
    public <T> void saveAllObjects(Collection<T> persistentObjects) {
        PersistenceManager pm = this.pool.getPersistenceManagerForThread();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            pm.makePersistentAll(persistentObjects);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
    }

    /**
     * Checks if exists a object of the given type, with the given key.
     * 
     * @param klass
     *            The object's class
     * @param key
     *            The object's key
     * @return <code>true</code> if exist an object of the given class with the
     *         given key, <code>false</code> otherwise
     */
    public <T> boolean existsObject(Class<T> klass, Object key) {
        if (key != null) {
            PersistenceManager pm = this.pool.getPersistenceManagerForThread();
            try {
                pm.getObjectById(klass, key);
                return true;
            } catch (JDOObjectNotFoundException ex) {
                // object not found, returning null
                return false;
            }
        }
        return false;
    }

    /**
     * Gets an object of the given key, with the given key. If there's no such
     * object, returns null.
     * 
     * @param klass
     *            The object's class
     * @param key
     *            The object's key
     * @return The object of the given key, with the given key, or
     *         <code>null</code> if there's no such object.
     */
    public <T> T getObjectByKey(Class<T> klass, Object key) {
        if (key != null) {
            PersistenceManager pm = this.pool.getPersistenceManagerForThread();
            try {
                T obj = pm.getObjectById(klass, key);
                return obj;
            } catch (JDOObjectNotFoundException ex) {
                // object not found, returning null
                return null;
            }
        }
        return null;
    }

    /**
     * Gets all objects of the given class
     * 
     * @param klass
     *            The objects' class
     * @return A list with all objects of the given class.
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getObjects(Class<T> klass) {
        PersistenceManager pm = this.pool.getPersistenceManagerForThread();
        Query query = pm.newQuery(klass);
        return (List<T>) query.execute();
    }

    /**
     * Counts how many objects of the given class exists at datastore.
     * 
     * @param klass
     *            The objects' class.
     * @return The number of objects of the given class stored at datastore
     */
    public <T> int countObjects(Class<T> klass) {
        PersistenceManager pm = this.pool.getPersistenceManagerForThread();
        Query query = pm.newQuery(klass);
        query.setResult("count(this)");
        return (Integer) query.execute();
    }

    /**
     * Deletes the given object from datastore
     * 
     * @param persistentObject
     *            the object to be deleted.
     */
    public <T> void deleteObject(T persistentObject) {
        PersistenceManager pm = this.pool.getPersistenceManagerForThread();
        pm.deletePersistent(persistentObject);
    }

    /**
     * Delete all objects of the given class from datastore.
     * 
     * @param klass
     *            The objects' class
     */
    public <T> void deleteObjects(Class<T> klass) {
        PersistenceManager pm = this.pool.getPersistenceManagerForThread();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            Query query = pm.newQuery(klass);
            query.deletePersistentAll();
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Delete all the given objects from datastore
     * 
     * @param persistentObjects
     *            the objects to be deleted
     */
    public <T> void deleteAllObjects(Collection<T> persistentObjects) {
        PersistenceManager pm = this.pool.getPersistenceManagerForThread();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            pm.deletePersistentAll(persistentObjects);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
        }
    }

    /**
     * Creates a {@link Query} for query objects of the given class.
     * 
     * @param klass
     *            The objects' class to be queried
     * @return A query for the given class
     */
    public <T> Query createQueryForClass(Class<T> klass) {
        PersistenceManager pm = this.pool.getPersistenceManagerForThread();
        return pm.newQuery(klass);
    }
}
