package datastore;

import java.util.Collection;

/**
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
public interface GenericDAO<T> {

    /**
     * Deletes an object. The given parameter can be the object to be deleted,
     * it means an instance of T, or can be the key for the object to be
     * deleted.
     * 
     * @param object
     *            The object to be deleted or the key for the object to be
     *            deleted.
     */
    public abstract void delete(Object object);

    /**
     * Saves an entity. It doesn't verify if the object already exists, it just
     * saves, overwriting the previous object, if exists.
     * 
     * @param entity
     *            The entity to be save
     */
    public abstract void save(T entity);

    /**
     * @return Gets all T entities.
     */
    public abstract Collection<T> getAll();

    /**
     * Get all elements which given attribute starts with the given prefix;
     * 
     * @param prefix
     *            the attribute's prefix to be queried
     * @param attribute
     *            the entity attribute being queried
     * 
     * 
     * @return a list of all entities which attribute starts with the given
     *         prefix. If no entity be found, it returns an empty list.
     */
    public abstract Collection<T> getAllStartsWith(String attribute, String prefix);

    /**
     * Gets an T entity.
     * 
     * @param key
     *            the entity's key.
     * @return The entity with the given key.
     * 
     * @throws ObjectNotFoundException
     *             if there's no object with the given key
     */
    public abstract T get(Object key);

    /**
     * Checks if exists an entity with the given key.
     * 
     * @param key
     *            the entity's key
     * @return <code>true</code> if exists, <code>false</code> otherwise.
     */
    public abstract boolean exists(Object key);

    /**
     * @return The number of T entities stored
     */
    public abstract int count();

}