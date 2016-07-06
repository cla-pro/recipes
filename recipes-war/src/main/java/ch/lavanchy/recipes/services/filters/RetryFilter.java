package ch.lavanchy.recipes.services.filters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import java.io.IOException;

/**
 * Try to play the request a second time in case of a connection timeout
 */
public class RetryFilter implements Filter {
    private static final Logger LOGGER = LogManager.getLogger(RetryFilter.class);

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
       // Do nothing
    }

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain)
            throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (final Exception e) {
            LOGGER.error("Exception during the execution of the request", e);
            // retry
        }
    }

    @Override
    public void destroy() {
        // Do nothing
    }
}
