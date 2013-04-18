package datastore.jdo;

import java.util.Collection;

import javax.jdo.Query;

import datastore.GenericDAO;

/**
 * A GenericDAO implementation using JDO.
 * 
 * @author Danilo Penna Queiroz - dpenna.queiroz@gmail.com
 */
public abstract class JDOGenericDAO<T> implements GenericDAO<T> {

    protected DatastoreFacade datastoreFacade = new DatastoreFacade();
    private Class<T> klass;

    public JDOGenericDAO(Class<T> klass) {
        this.klass = klass;
    }

    /**
     * @param datastoreFacade
     *            the datastoreFacade to set
     */
    public void setDatastoreFacade(DatastoreFacade datastoreFacade) {
        this.datastoreFacade = datastoreFacade;
    }

    /**
     * Creates a new Query on the database for this DAO's class. The returned
     * query can be customized by the application to find the desired entries in
     * the database.
     * 
     * @return The created Query
     * @see javax.jdo.Query
     * @since 1.0
     */
    public Query createQuery() {
        return this.datastoreFacade.createQueryForClass(this.klass);
    }

    /*
     * (non-Javadoc)
     * 
     * @see util.datastore.jdo.GenericDAO#delete(java.lang.Object)
     */
    @Override
    public void delete(Object object) {
        if (object.getClass().equals(this.klass)) {
            this.datastoreFacade.deleteObject(object);
        } else {
            if (this.exists(object)) {
                this.delete(this.get(object));
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see util.datastore.jdo.GenericDAO#save(T)
     */
    @Override
    public void save(T entity) {
        this.datastoreFacade.saveObject(entity);
    }

    /*
     * (non-Javadoc)
     * 
     * @see util.datastore.jdo.GenericDAO#getAll()
     */
    @Override
    public Collection<T> getAll() {
        return this.datastoreFacade.getObjects(this.klass);
    }

    /*
     * (non-Javadoc)
     * 
     * @see util.datastore.jdo.GenericDAO#getAllStartsWith(java.lang.String,
     * java.lang.String)
     */
    @Override
    @SuppressWarnings("unchecked")
    public Collection<T> getAllStartsWith(String attribute, String prefix) {
        Query query = this.createQuery();
        query.setFilter(attribute + ".startsWith(:1)");
        prefix = (prefix != null ? prefix : "").trim();
        return (Collection<T>) query.execute(prefix);
    }

    /*
     * (non-Javadoc)
     * 
     * @see util.datastore.jdo.GenericDAO#get(java.lang.Object)
     */
    @Override
    public T get(Object key) {
        return this.datastoreFacade.getObjectByKey(this.klass, key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see util.datastore.jdo.GenericDAO#exists(java.lang.Object)
     */
    @Override
    public boolean exists(Object key) {
        return this.datastoreFacade.existsObject(this.klass, key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see util.datastore.jdo.GenericDAO#count()
     */
    @Override
    public int count() {
        return this.datastoreFacade.countObjects(this.klass);
    }

}
