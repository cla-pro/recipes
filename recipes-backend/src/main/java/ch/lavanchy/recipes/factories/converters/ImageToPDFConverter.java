package ch.lavanchy.recipes.factories.converters;

import ch.lavanchy.recipes.factories.ToPDFConverter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;

/**
 * Convert any type of image to a PDF file.
 * Example from https://svn.apache.org/viewvc/pdfbox/trunk/examples/src/main/java/org/apache/pdfbox/examples/pdmodel/ImageToPDF.java?view=markup
 *
 * @since 2.0.0
 */
class ImageToPDFConverter implements ToPDFConverter {
    @Override
    public File convertToPDFInFile(File sourceFile, File targetFile) {
        try (final PDDocument doc = new PDDocument()) {
            final PDPage page = new PDPage();
            doc.addPage(page);

            final PDImageXObject pdImage = PDImageXObject.createFromFileByExtension(sourceFile, doc);
            final PDPageContentStream contents = new PDPageContentStream(doc, page);
            contents.drawImage(pdImage, 0, 0);

            contents.close();
            doc.save(targetFile);
            return targetFile;
        } catch (final IOException e) {
            throw new RuntimeException(
                    String.format("Error during the conversion from the image=%s to PDF", sourceFile.getAbsolutePath()),
                    e);
        }
    }
}
