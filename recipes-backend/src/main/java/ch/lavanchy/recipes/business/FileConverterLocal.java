package ch.lavanchy.recipes.business;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Convert files of different types (ODT, DOCX, ...) to PDF
 *
 * @since 1.0.0
 */
public interface FileConverterLocal {
    /**
     * Get the sourceFile as PDF. Does nothing if the file is already a PDF.
     *
     * @param sourceFile The source file to be converted
     * @return The converted file
     * @throws FileNotFoundException Thrown if the source file does not exists
     */
    File getFileAsPDF(final File sourceFile) throws FileNotFoundException;
}
