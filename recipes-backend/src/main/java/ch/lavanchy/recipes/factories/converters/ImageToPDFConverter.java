package ch.lavanchy.recipes.factories.converters;

import ch.lavanchy.recipes.factories.ToPDFConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Convert any type of image to a PDF file.
 * Example from https://svn.apache.org/viewvc/pdfbox/trunk/examples/src/main/java/org/apache/pdfbox/examples/pdmodel/ImageToPDF.java?view=markup
 *
 * @since 2.0.0
 */
class ImageToPDFConverter implements ToPDFConverter {
    private static final Logger LOGGER = LogManager.getLogger(ImageToPDFConverter.class);

    @Override
    public File convertToPDFInFile(final File sourceFile, final File targetFile) {
        final PDDocument doc = new PDDocument();
        try {
            final PDPage page = new PDPage(PDRectangle.A4);
            final ImageOrientation imageOrientation = getImageOrientation(sourceFile);
            page.setRotation(imageOrientation.getRotation());
            doc.addPage(page);

            final PDImageXObject pdImage = PDImageXObject.createFromFileByExtension(sourceFile, doc);
            final PDPageContentStream contents = new PDPageContentStream(doc, page);
            contents.transform(imageOrientation.getTransformMatrix(page.getMediaBox()));
            imageOrientation.drawImage(contents, pdImage);

            contents.close();
            doc.save(targetFile);
            LOGGER.debug("ImageToPDFConverter PDF saved {}", targetFile.getAbsolutePath());
            return targetFile;
        } catch (final IOException e) {
            throw new RuntimeException(
                    String.format("Error during the conversion from the image=%s to PDF", sourceFile.getAbsolutePath()),
                    e);
        } finally {
            try {
                doc.close();
                LOGGER.debug("Document closed name={}", targetFile.getName());
            } catch (final IOException e) {
                throw new RuntimeException(
                        String.format("Error while closing the PDF document (%s)", sourceFile.getAbsolutePath()),
                        e);
            }
        }
    }

    private ImageOrientation getImageOrientation(final File file) throws IOException {
        final BufferedImage bimg = ImageIO.read(file);
        final int width = bimg.getWidth();
        final int height = bimg.getHeight();
        return (height >= width) ? ImageOrientation.PORTRAIT : ImageOrientation.LANDSCAPE;
    }
}
