package ch.lavanchy.recipes;

import ch.lavanchy.recipes.config.ApplicationBinder;
import ch.lavanchy.recipes.config.ApplicationConfig;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * Created by cla on 22.11.2015.
 */
public class Application {
    public static void main(String[] args) throws Exception {
        final URI baseUri = UriBuilder.fromUri("http://localhost/").port(9998).build();
        //final ResourceConfig config = new ResourceConfig(ApplicationConfig.class);
        ResourceConfig config = new ResourceConfig();
        config.packages("ch.lavanchy.recipes.services");
        config.register(new ApplicationBinder());
        final Server jettyServer = JettyHttpContainerFactory.createServer(baseUri, config);

        try {
            jettyServer.start();
            jettyServer.join();
        } finally {
            jettyServer.destroy();
        }
    }
}
