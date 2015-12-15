package ch.lavanchy.recipes.application;

import ch.lavanchy.recipes.config.GuiceContext;
import com.google.inject.servlet.GuiceFilter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlets.CrossOriginFilter;

/**
 * Test application that starts a Jetty server
 *
 * @since 1.0.0
 */
public class Application {
    public static void main(String[] args) throws Exception {
        Server server = new Server(9998);

        FilterHolder holder = new FilterHolder(CrossOriginFilter.class);
        holder.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        holder.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        holder.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST,OPTIONS,HEAD");
        holder.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");

        final ServletContextHandler handler = new ServletContextHandler(server, "/");
        handler.addEventListener(new GuiceContext());
        handler.addFilter(holder, "/*", null);
        handler.addFilter(new FilterHolder(GuiceFilter.class), "/*", null);
        handler.addServlet(DefaultServlet.class, "/");

        server.setHandler(handler);
        server.start();
    }
//
//    public static void main2(String[] args) throws Exception {
//        final Server server = new Server(9998);
//
//        ResourceHandler resourceHandler = new ResourceHandler();
//        resourceHandler.setDirectoriesListed(true);
//        resourceHandler.setWelcomeFiles(new String[] { "index.html" });
//        resourceHandler.setResourceBase("./src/main/webapp");
//
//        FilterHolder holder = new FilterHolder(CrossOriginFilter.class);
//        holder.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
//        holder.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
//        holder.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST,HEAD");
//        holder.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");
//
//        String servletName = "ch.lavanchy.recipes.config.ApplicationConfig";
//        ServletHandler servletHandler = new ServletHandler();
//
//        ServletHolder servletHolder = new ServletHolder();
//        servletHolder.setName(servletName);
//        servletHandler.addServlet(servletHolder);
//
//        ServletMapping servletMapping = new ServletMapping();
//        servletMapping.setServletName(servletName);
//        servletMapping.setPathSpec("/*");
//        servletHandler.addServletMapping(servletMapping);
//
//        ServletContextHandler context = new ServletContextHandler(server, "/", ServletContextHandler.NO_SESSIONS);
//        context.setContextPath("/");
//        context.addFilter(holder, "/backend/*", EnumSet.of(DispatcherType.REQUEST));
//
//        HandlerList handlers = new HandlerList();
//        handlers.setHandlers(new Handler[] { resourceHandler, servletHandler, context, new DefaultHandler() });
//        server.setHandler(handlers);
//
//        try {
//            server.start();
//            server.join();
//        } finally {
//            server.destroy();
//        }
//    }
//
//    public static void main2(String[] args) throws Exception {
//        FilterHolder filterHolder = new FilterHolder(CrossOriginFilter.class);
//        filterHolder.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
//        filterHolder.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
//        filterHolder.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST,OPTIONS,HEAD");
//        filterHolder.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "*");
//        // filterHolder.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");
//
//        final URI baseUri = UriBuilder.fromUri("http://localhost/").port(9998).build();
//        final ApplicationConfig config = new ApplicationConfig();
//        final Server jettyServer = JettyHttpContainerFactory.createServer(baseUri, config);
//
//        ServletContextHandler context = new ServletContextHandler(jettyServer, "/", ServletContextHandler.NO_SESSIONS);
//        context.setContextPath("/");
//        context.addFilter(filterHolder, "/*", EnumSet.of(DispatcherType.REQUEST));
//
//        try {
//            jettyServer.start();
//            jettyServer.join();
//        } finally {
//            jettyServer.destroy();
//        }
//    }
}
