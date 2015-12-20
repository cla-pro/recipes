package ch.lavanchy.recipes.business;

import ch.lavanchy.recipes.utils.PropertyProviderLocal;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * Implementation of {@link RecipesBusinessLocal}
 *
 * @since 1.0.0
 */
public class FileBusinessBean implements FileBusinessLocal {
    @Inject
    private PropertyProviderLocal propertyProvider;

    @Override
    public void saveFile(InputStream inputStream, String filename) {
        final String location = propertyProvider.getStringPropertyByName("recipes.files.location");

        final File folder = new File(location);
        try {
            if (!folder.exists()) {
                folder.mkdirs();
            }
            final File file = new File(folder, filename);
            Files.copy(inputStream, file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public InputStream readFile(String filename) {
        final String location = propertyProvider.getStringPropertyByName("recipes.files.location");

        final File folder = new File(location);
        try {
            if (!folder.exists()) {
                return null;
            }

            final File file = new File(folder, filename);
            if (!file.exists()) {
                return null;
            }

            return new FileInputStream(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
