package ch.lavanchy.recipes.services.filters;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

/**
 * Filter that configure the ThreadContext for the logging and log the request as well as its duration
 *
 * @since 2.0.0
 */
public class LoggingFilter implements Filter {
    private static final Logger LOGGER = LogManager.getLogger(LoggingFilter.class);

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        // Do nothing
    }

    @Override
    public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain)
            throws IOException, ServletException {
        ThreadContext.put("logId", UUID.randomUUID().toString());

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            stopWatch.stop();
            LOGGER.info("Service call url={} {} duration={}",
                    ((HttpServletRequest) servletRequest).getMethod(),
                    extractUrl(servletRequest),
                    stopWatch.getTime());

            ThreadContext.clearAll();
        }
    }

    private String extractUrl(final ServletRequest servletRequest) {
        if (servletRequest instanceof HttpServletRequest) {
            final String query = ((HttpServletRequest) servletRequest).getQueryString();
            return ((HttpServletRequest) servletRequest).getRequestURL() + (query == null ? "" : "?" + query);
        } else {
            return "cannot extract URL from " + servletRequest.getClass().getName();
        }
    }

    @Override
    public void destroy() {
        // Do nothing
    }
}
