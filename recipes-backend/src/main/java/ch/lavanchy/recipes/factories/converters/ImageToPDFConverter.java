package ch.lavanchy.recipes.factories.converters;

import ch.lavanchy.recipes.factories.ToPDFConverter;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
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
        PDDocument doc = new PDDocument();
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
            System.out.println(String.format("ImageToPDFConverter PDF saved %s", targetFile.getAbsolutePath()));
            return targetFile;
        } catch (final IOException e) {
            throw new RuntimeException(
                    String.format("Error during the conversion from the image=%s to PDF", sourceFile.getAbsolutePath()),
                    e);
        } finally {
            try {
                doc.close();
                System.out.println("Document closed");
            } catch (IOException e) {
                throw new RuntimeException(
                        String.format("Error while closing the PDF document (%s)", sourceFile.getAbsolutePath()),
                        e);
            }
        }
    }

    private ImageOrientation getImageOrientation(final File file) throws IOException {
        try {
            final Metadata metadata = ImageMetadataReader.readMetadata(file);
            final ExifIFD0Directory exifIFD0Directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);

            if (exifIFD0Directory == null) {
                return ImageOrientation.PORTRAIT;
            } else if (exifIFD0Directory.getInt(ExifIFD0Directory.TAG_ORIENTATION) == 0 ||
                    exifIFD0Directory.getInt(ExifIFD0Directory.TAG_ORIENTATION) == 180) {
                return ImageOrientation.PORTRAIT;
            } else {
                return ImageOrientation.LANDSCAPE;
            }
        } catch (final ImageProcessingException | MetadataException e) {
            return ImageOrientation.PORTRAIT;
        }
    }
}
