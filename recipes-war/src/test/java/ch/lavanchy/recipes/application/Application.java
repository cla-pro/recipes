package ch.lavanchy.recipes.application;

import ch.lavanchy.recipes.config.GuiceContext;
import com.google.inject.servlet.GuiceFilter;
import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Test application that starts a Jetty server
 *
 * @since 1.0.0
 */
public class Application {
    private static final String RECIPES_PERSISTENCE_UNIT_TEST = "recipes-test-pu";

    public static void main(String[] args) throws Exception {
        initDB();

        Server server = new Server(9998);

        FilterHolder holder = new FilterHolder(CrossOriginFilter.class);
        holder.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        holder.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        holder.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST,PUT,OPTIONS,HEAD");
        holder.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setResourceBase("./recipes-frontend/");

        final ServletContextHandler handler = new ServletContextHandler(server, "/");
        handler.addEventListener(new GuiceContext(RECIPES_PERSISTENCE_UNIT_TEST));
        handler.addFilter(holder, "/*", null);
        handler.addFilter(new FilterHolder(GuiceFilter.class), "/*", null);
        handler.addServlet(DefaultServlet.class, "/");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { resourceHandler, handler });

        server.setHandler(handlers);
        server.start();
    }

    public static void initDB() throws ClassNotFoundException, SQLException, LiquibaseException {
        Connection connection = null;
        liquibase.database.core.H2Database databaseConnection = null;
        try {
            Class.forName("org.h2.Driver");

            // http://stackoverflow.com/questions/5763747/h2-in-memory-database-table-not-found
            // DB_CLOSE_DELAY is used to avoid the DB to be deleted when the
            // connection is closed.
            connection = DriverManager.getConnection("jdbc:h2:mem:recipes;DB_CLOSE_DELAY=-1", "admin", "admin");
            connection.setAutoCommit(true);

            final JdbcConnection jdbcConnection = new JdbcConnection(connection);
            databaseConnection = new liquibase.database.core.H2Database();
            databaseConnection.setConnection(jdbcConnection);
            databaseConnection.setAutoCommit(true);

            final Statement statement = connection.createStatement();
            statement.execute("drop all objects");
            statement.close();

            final Liquibase liquibase = new Liquibase("liquibase/database.xml", new ClassLoaderResourceAccessor(), databaseConnection);
            liquibase.validate();
            liquibase.update((Contexts) null);

            databaseConnection.commit();
            connection.commit();
        } finally {
            if (databaseConnection != null) {
                databaseConnection.close();
            }

            if (connection != null) {
                connection.close();
            }
        }
    }
}
