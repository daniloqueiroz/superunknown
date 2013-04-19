package datastore.jdo;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import server.Configuration;

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
        if (this.isPersistenceManagerOpened()) {
            logger.debug("Closing PersistenceManager.");
            PersistenceManager pm = this.pool.get();
            pm.close();
            this.pool.remove();
        }
    }

    protected PersistenceManager getPersistenceManagerForThread() {
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

        private static PersistenceManagerFactory pmFactory;

        static {
            Configuration config = new Configuration();
            if (config.getDebugMode()) {
                pmFactory = JDOHelper.getPersistenceManagerFactory(config.getPMFNameDebug());
            } else {
                pmFactory = JDOHelper.getPersistenceManagerFactory(config.getPMFNameProduction());
            }
        }

        public static PersistenceManager getPersistenceManager() {
            return pmFactory.getPersistenceManager();
        }
    }
}
