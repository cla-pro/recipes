package ch.lavanchy.recipes.business;

import ch.lavanchy.recipes.converter.FileConverter;
import ch.lavanchy.recipes.utils.PropertyProviderLocal;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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

    @Inject
    private FileConverter fileConverter;

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

            if (fileConverter.isConversionRequired(file)) {
                fileConverter.getFileAsPDF(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public InputStream readFile(String filename) throws FileNotFoundException {
        final String location = propertyProvider.getStringPropertyByName("recipes.files.location");
        final File file = getFileToRead(filename, location);
        final File pdfFile = fileConverter.getFileAsPDF(file);

        return new FileInputStream(pdfFile);
    }

    private File getFileToRead(String filename, String location) throws FileNotFoundException {
        final File folder = new File(location);
        if (!folder.exists()) {
            throw new FileNotFoundException();
        }

        final File file = new File(folder, filename);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        return file;
    }
}
