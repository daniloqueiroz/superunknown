package datastore.jdo;

import java.io.IOException;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * A filter that takes care of rollback active {@link Transaction} (if exists)
 * and close the {@link PersistenceManager} for the current {@link Thread}.
 * 
 * @author Danilo Queiroz - dpenna.queiroz@gmail.com
 */
public class PersistenceManagerTransactionFilter implements Filter {

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     * javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } finally {
            PersistenceManagerPool pool = PersistenceManagerPool.getInstance();
            if (pool.isPersistenceManagerOpened()) {
                this.sanitizeTransaction(pool.getPersistenceManager());
                pool.closePersistenceManager();
            }
        }
    }

    /**
     * Check if there's an active Transaction and rollback if there's.
     */
    private void sanitizeTransaction(PersistenceManager persistenceManager) {
        Transaction tx = persistenceManager.currentTransaction();
        if (tx.isActive()) {
            tx.rollback();
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
