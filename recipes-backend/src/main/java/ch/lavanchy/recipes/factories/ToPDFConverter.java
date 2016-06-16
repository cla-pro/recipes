package ch.lavanchy.recipes.factories;

import java.io.File;

/**
 * @since 2.0.0
 */
public interface ToPDFConverter {
    /**
     * Convert the source to PDF into the targetFile.
     *
     * @param sourceFile The file to be converted
     * @param targetFile The resulting file
     * @return The result of the conversion
     */
    File convertToPDFInFile(final File sourceFile, final File targetFile);
}
