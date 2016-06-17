package ch.lavanchy.recipes.business;

import ch.lavanchy.recipes.factories.FileConverter;
import ch.lavanchy.recipes.utils.PropertyProviderLocal;

import javax.inject.Inject;
import java.io.File;
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
    public void saveFile(final InputStream inputStream, final String filename, final boolean overwrite) {
        final String location = propertyProvider.getStringPropertyByName("recipes.files.location");

        final File folder = new File(location);
        try {
            if (!folder.exists()) {
                folder.mkdirs();
            }
            final File file = new File(folder, filename);
            if (file.exists()) {
                if (overwrite) {
                    if (file.delete()) {
                        System.out.println("Previous file deleted");
                    } else {
                        throw new RuntimeException(String.format("Unable to delete file %s", filename));
                    }
                } else {
                    throw new RuntimeException(String.format("File %s already exists", filename));
                }
            }
            Files.copy(inputStream, file.toPath());

            if (fileConverter.isConversionRequired(file)) {
                fileConverter.getFileAsPDF(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public File readFile(String filename) throws FileNotFoundException {
        final String location = propertyProvider.getStringPropertyByName("recipes.files.location");
        final File file = getFileToRead(filename, location);
        final File pdfFile = fileConverter.getFileAsPDF(file);
        System.out.println(String.format("%d bytes to read for file=%s", pdfFile.length(), pdfFile.getAbsolutePath()));

        return pdfFile;
    }

    private File getFileToRead(String filename, String location) throws FileNotFoundException {
        final File folder = new File(location);
        if (!folder.exists()) {
            throw new FileNotFoundException(String.format("Folder with name \"%s\" does not exists", location));
        }

        final File file = new File(folder, filename);
        if (!file.exists()) {
            throw new FileNotFoundException(String.format("File with name \"%s\" does not exists", file.getAbsolutePath()));
        }

        System.out.println(String.format("Reading file at %s", file.getAbsolutePath()));
        return file;
    }
}
