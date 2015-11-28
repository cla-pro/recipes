package ch.lavanchy.recipes;

import ch.lavanchy.recipes.config.ApplicationConfig;
import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;

/**
 * Test application that starts a Jetty server
 *
 * @since 1.0.0
 */
public class Application {
    public static void main(String[] args) throws Exception {
        final URI baseUri = UriBuilder.fromUri("http://localhost/").port(9998).build();
        final ResourceConfig config = new ResourceConfig(new ApplicationConfig());
        final Server jettyServer = JettyHttpContainerFactory.createServer(baseUri, config);

        try {
            jettyServer.start();
            jettyServer.join();
        } finally {
            jettyServer.destroy();
        }
    }
}
