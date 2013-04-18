package datastore.jdo;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thread based pool for {@link PersistenceManager}.
 * 
 * It keeps a unique {@link PersistenceManager} per {@link Thread} using a
 * {@link ThreadLocal}.
 * 
 * @see ThreadLocal
 * 
 * @author Danilo Penna Queiroz - dpenna.queiroz@gmail.com
 */
public class PersistenceManagerPool {

    private static final Logger logger = LoggerFactory.getLogger(PersistenceManagerPool.class);

    private static final PersistenceManagerPool instance = new PersistenceManagerPool();

    public static PersistenceManagerPool getInstance() {
        return instance;
    }

    /**
     * Closes the current connection to database.
     * 
     * This method should be used carefully. Use this only when you are doing
     * background tasks. After closes the connection any object retrieved from
     * database become to a inconsistent state, and shouldn't be used (read or
     * modified) anymore.
     */
    public static void forceClose() {
        instance.close();
    }

    // Object stuff
    private ThreadLocal<PersistenceManager> pool = new ThreadLocal<PersistenceManager>();

    private PersistenceManagerPool() {
        // private constructor
    }

    protected boolean isPersistenceManagerOpened() {
        PersistenceManager pm = this.pool.get();
        return (pm != null) && !pm.isClosed();
    }

    protected void close() {
        if (instance.isPersistenceManagerOpened()) {
            logger.debug("Closing PersistenceManager.");
            PersistenceManager pm = this.pool.get();
            pm.close();
            this.pool.remove();
        }
    }

    public PersistenceManager getPersistenceManagerForThread() {
        logger.debug("Getting a PersistenceManager.");
        PersistenceManager pm = this.pool.get();
        if (pm == null || pm.isClosed()) {
            logger.debug("Creating a new PersistenceManager.");
            pm = PMFWrapper.getPersistenceManager();
            this.pool.set(pm);
        }
        return pm;
    }

    /**
     * This class is a wrapper to the {@link PersistenceManagerFactory}.
     */
    private static class PMFWrapper {
        private static final String PMF_DEFAULT_VALUE = "jdo-factory";

        private static PersistenceManagerFactory pmFactory;

        static {
            pmFactory = JDOHelper.getPersistenceManagerFactory(PMF_DEFAULT_VALUE);
        }

        public static PersistenceManager getPersistenceManager() {
            return pmFactory.getPersistenceManager();
        }
    }
}
