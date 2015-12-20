package ch.lavanchy.recipes.business;

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
    void saveFile(final InputStream inputStream, final String filename);

    /**
     * Return the file identified by the filename
     *
     * @param filename The name of the file to read
     * @return The file as an input stream
     */
    InputStream readFile(final String filename);
}
