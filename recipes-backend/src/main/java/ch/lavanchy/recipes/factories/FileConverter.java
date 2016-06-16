package ch.lavanchy.recipes.factories;

import ch.lavanchy.recipes.factories.converters.ToPDFConverterFactory;
import org.apache.commons.io.FilenameUtils;

import javax.inject.Inject;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Convert files of different types (ODT, DOCX, ...) to PDF
 *
 * @since 1.0.0
 */
public class FileConverter {
    private final static String PDF_EXTENSION = "pdf";
    private final static String PDF_FILENAME_EXTENSION = "." + PDF_EXTENSION;

    @Inject
    private ToPDFConverterFactory toPDFConverterFactory;

    /**
     * Return true if the file needs to be converted to a PDF. This checks is based on the filename's extension
     *
     * @param file The file
     * @return True if a conversion is required
     */
    public boolean isConversionRequired(final File file) {
        final String filename = file.getName();
        final String extension = FilenameUtils.getExtension(filename);
        return !extension.toLowerCase().equals(PDF_EXTENSION);
    }

    /**
     * Get the sourceFile as PDF. Does nothing if the file is already a PDF.
     *
     * @param sourceFile The source file to be converted
     * @return The converted file
     * @throws FileNotFoundException Thrown if the source file does not exists
     */
    public File getFileAsPDF(final File sourceFile) throws FileNotFoundException {
        final String filename = sourceFile.getName();
        final String extension = FilenameUtils.getExtension(filename);

        if (extension.toLowerCase().equals(PDF_EXTENSION)) {
            return sourceFile;
        }

        final String folder = sourceFile.getParent();
        final String filenamePDF = FilenameUtils.removeExtension(filename) + PDF_FILENAME_EXTENSION;
        final File targetFile = new File(folder, filenamePDF);
        System.out.println(String.format("Reading PDF file at %s and exists=%s", targetFile.getAbsolutePath(), targetFile.exists()));
        if (targetFile.exists() && sourceOlderThanPDF(sourceFile, targetFile)) {
            return targetFile;
        }

        System.out.println(String.format("Converting file at %s to PDF", sourceFile.getAbsolutePath()));
        return toPDFConverterFactory.createConverter(extension).convertToPDFInFile(sourceFile, targetFile);
    }

    private boolean sourceOlderThanPDF(File sourceFile, File targetFile) {
        return sourceFile.lastModified() < targetFile.lastModified();
    }
}
