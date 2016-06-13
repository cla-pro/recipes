package ch.lavanchy.recipes.factories.converters;

import ch.lavanchy.recipes.factories.ToPDFConverter;
import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.document.DocumentKind;

/**
 * Return the converter (already configured) according to the extension of the file to be converted.
 *
 * @since 2.0.0
 */
public class ToPDFConverterFactory {
    public ToPDFConverter createConverter(final String extension) {
        switch (extension.toLowerCase()) {
            case "jpg":
            case "jpeg":
            case "png":
            case "bmp":
                return new ImageToPDFConverter();
            case "odt":
                return new DocToPDFConverter(Options.getFrom(DocumentKind.ODT).to(ConverterTypeTo.PDF));
            case "docx":
                return new DocToPDFConverter(Options.getFrom(DocumentKind.DOCX).to(ConverterTypeTo.PDF));
            default:
                throw new IllegalArgumentException(String.format("File extension unknown %s", extension));
        }
    }
}
