package ch.lavanchy.recipes.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * Root service to check the availability
 *
 * @since 1.0.0
 */
@Path("/")
public class RootService {
    @GET
    public String get() {
        return "Recipes";
    }
}
