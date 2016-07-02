package ch.lavanchy.recipes.services;

import com.google.inject.persist.Transactional;

import javax.servlet.*;
import java.io.IOException;

/**
 * Filter that starts and commit (or rollback) the transaction.
 * 
 * @since 1.0.0
 */
public class TransactionFilter implements Filter {

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        // Do nothing
    }

    @Transactional
    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Do nothing
    }
}
