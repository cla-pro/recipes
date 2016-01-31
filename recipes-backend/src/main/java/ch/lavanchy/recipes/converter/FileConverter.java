package ch.lavanchy.recipes.converter;

import fr.opensagres.xdocreport.converter.ConverterRegistry;
import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.IConverter;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.converter.XDocConverterException;
import fr.opensagres.xdocreport.core.document.DocumentKind;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Convert files of different types (ODT, DOCX, ...) to PDF
 *
 * @since 1.0.0
 */
public class FileConverter {
    private final static String PDF_EXTENSION = "pdf";
    private final static String PDF_FILENAME_EXTENSION = "." + PDF_EXTENSION;

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
        final File targetFile = new File(folder + File.separatorChar +  filenamePDF);
        if (targetFile.exists() && sourceOlderThanPDF(sourceFile, targetFile)) {
            return targetFile;
        }

        return convertToPDF(sourceFile, targetFile, extension);
    }

    private boolean sourceOlderThanPDF(File sourceFile, File targetFile) {
        return sourceFile.lastModified() < targetFile.lastModified();
    }

    private File convertToPDF(final File sourceFile, final File targetFile, final String extension) throws FileNotFoundException {
        final Options options = getOptions(extension);
        final IConverter converter = ConverterRegistry.getRegistry().getConverter(options);

        final InputStream in = new FileInputStream(sourceFile);
        final OutputStream out = new FileOutputStream(targetFile);
        try {
            converter.convert(in, out, options);
        } catch (XDocConverterException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return targetFile;
    }

    private Options getOptions(final String extension) {
        switch (extension.toLowerCase()) {
            case "odt":
                return Options.getFrom(DocumentKind.ODT).to(ConverterTypeTo.PDF);
            case "docx":
                return Options.getFrom(DocumentKind.DOCX).to(ConverterTypeTo.PDF);
            default:
                return Options.getFrom(DocumentKind.DOCX).to(ConverterTypeTo.PDF);
        }
    }
}
