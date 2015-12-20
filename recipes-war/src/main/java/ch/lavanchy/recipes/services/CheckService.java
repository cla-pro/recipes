package ch.lavanchy.recipes.services;

import ch.lavanchy.recipes.utils.PropertyProviderLocal;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.io.File;

/**
 * Service that returns the system information
 *
 * @since 1.0.0
 */
@Path("/check")
public class CheckService {
    @Inject
    private PropertyProviderLocal propertyProvider;

    @GET
    public String check() {
        final String userStmt = generateUserStmt();
        final String fileLocationStmt = generateFileLocationStmt();

        return String.format("<html><body>%s %s</body>", userStmt, fileLocationStmt);
    }

    private String generateFileLocationStmt() {
        final String filesLocation = propertyProvider.getStringPropertyByName("recipes.files.location");
        final File folder = new File(filesLocation);

        return String.format("Files location: %s<br /><ul><li>Exists: %s</li><li>isDirectory: %s</li><li>canWrite: %s</li></ul><br />",
                filesLocation, folder.exists(), folder.isDirectory(), folder.canWrite());
    }

    private String generateUserStmt() {
        return "User: " + System.getProperty("user.name") + "<br />";
    }
}
