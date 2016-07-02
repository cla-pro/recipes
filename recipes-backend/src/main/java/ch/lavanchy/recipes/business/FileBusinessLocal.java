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
    /**
     * Save the given file (as Stream) into the filesystem
     *
     * @param inputStream The content to save
     * @param filename The filename for the new file
     */
    void saveFile(final InputStream inputStream, final String filename, final boolean overwrite);

    /**
     * Return the PDF file identified by the filename
     *
     * @param filename The name of the file to read
     * @return The file
     * @throws FileNotFoundException if the file cannot be found
     */
    File readPDFFile(final String filename) throws FileNotFoundException;

    /**
     * Return the file identified by the filename
     *
     * @param filename The name of the file to read
     * @return The file
     * @throws FileNotFoundException if the file cannot be found
     */
    File readOriginalFile(final String filename) throws FileNotFoundException;
}
