package ch.lavanchy.recipes.factories.converters;

import ch.lavanchy.recipes.factories.ToPDFConverter;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
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
            final BufferedImage originalImage = ImageIO.read(sourceFile);
            LOGGER.info("Original image size(w={}, h={})", originalImage.getWidth(), originalImage.getHeight());
            final AffineTransform transform = createTransformToRotateImage(sourceFile, originalImage.getWidth(), originalImage.getHeight());
            final BufferedImage transformedImage = applyTransform(transform, originalImage);
            ImageIO.write(transformedImage, "jpg", sourceFile);
            final ImageOrientation imageOrientation = getImageOrientation(transformedImage);

            final PDPage page = new PDPage(PDRectangle.A4);
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
        } catch (final IOException | MetadataException | ImageProcessingException e) {
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

    private BufferedImage applyTransform(final AffineTransform transform, final BufferedImage originalImage) {
        final AffineTransformOp affineTransformOp = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
        return affineTransformOp
                .filter(originalImage,
                        new BufferedImage(originalImage.getHeight(), originalImage.getWidth(), originalImage.getType()));
    }

    private AffineTransform createTransformToRotateImage(
            final File imageFile,
            final int width,
            final int height)
            throws ImageProcessingException, IOException, MetadataException {
        final Metadata metadata = ImageMetadataReader.readMetadata(imageFile);
        final ExifIFD0Directory exifIFD0Directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);

        final AffineTransform affineTransform = new AffineTransform();
        // source http://stackoverflow.com/questions/21951892/how-to-determine-and-auto-rotate-images
        switch (exifIFD0Directory.getInt(ExifIFD0Directory.TAG_ORIENTATION)) {
            case 1:
                return affineTransform;
            case 2: // Flip X
                affineTransform.scale(-1.0, 1.0);
                affineTransform.translate(-width, 0);
                return affineTransform;
            case 3: // PI rotation
                affineTransform.translate(width, height);
                affineTransform.rotate(Math.PI);
                return affineTransform;
            case 4: // Flip Y
                affineTransform.scale(1.0, -1.0);
                affineTransform.translate(0, -height);
                return affineTransform;
            case 5: // - PI/2 and Flip X
                affineTransform.rotate(-Math.PI / 2);
                affineTransform.scale(-1.0, 1.0);
                return affineTransform;
            case 6: // -PI/2 and -width
                affineTransform.translate(height, 0);
                affineTransform.rotate(Math.PI / 2);
                return affineTransform;
            case 7: // PI/2 and Flip
                affineTransform.scale(-1.0, 1.0);
                affineTransform.translate(-height, 0);
                affineTransform.translate(0, width);
                affineTransform.rotate(3 * Math.PI / 2);
                return affineTransform;
            case 8: // PI / 2
                affineTransform.translate(0, width);
                affineTransform.rotate(3 * Math.PI / 2);
                return affineTransform;
            default:
                return affineTransform;
        }
    }

    private ImageOrientation getImageOrientation(final BufferedImage image) throws IOException {
        LOGGER.info("Image size(w={}, h={})", image.getWidth(), image.getHeight());
        return (image.getHeight() >= image.getWidth()) ? ImageOrientation.PORTRAIT : ImageOrientation.LANDSCAPE;
    }
}
