package ch.lavanchy.recipes.factories.converters;

import ch.lavanchy.recipes.factories.ToPDFConverter;
import fr.opensagres.xdocreport.converter.ConverterRegistry;
import fr.opensagres.xdocreport.converter.IConverter;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.converter.XDocConverterException;

import java.io.*;

/**
 * Convert the .docx and .odt to PDF
 *
 * @since 2.0.0
 */
class DocToPDFConverter implements ToPDFConverter {
    private final Options options;

    DocToPDFConverter(final Options options) {
        this.options = options;
    }

    @Override
    public File convertToPDFInFile(final File sourceFile, final File targetFile) {
        try (final InputStream in = new FileInputStream(sourceFile);
             final OutputStream out = new FileOutputStream(targetFile)) {

            final IConverter converter = ConverterRegistry.getRegistry().getConverter(options);
            converter.convert(in, out, options);
        } catch (XDocConverterException e) {
            throw new RuntimeException(String.format("Error during the conversion with options=%s", options), e);
        } catch (IOException e) {
            throw new RuntimeException("Error with the files (opening or closing)", e);
        }

        return targetFile;
    }
}
