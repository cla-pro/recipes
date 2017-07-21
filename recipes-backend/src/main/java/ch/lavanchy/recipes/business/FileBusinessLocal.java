package ch.lavanchy.recipes.business;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Provide the methods to save and retrieve the files
 *
 * @since 1.0.0
 */
public interface FileBusinessLocal {
    void saveFile(final InputStream inputStream, final String filename, final boolean overwrite);

    File readPDFFile(final String filename) throws FileNotFoundException;

    File readOriginalFile(final String filename) throws FileNotFoundException;

    void deleteFile(final String filename);
}
