package ch.lavanchy.recipes.factories.converters;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;

import java.io.IOException;

/**
 * @since 2.0.0
 */
public enum ImageOrientation {
    LANDSCAPE {
        @Override
        public int getRotation() {
            return 90;
        }

        @Override
        public Matrix getTransformMatrix(PDRectangle pageSize) {
            return new Matrix(0, 1, -1, 0, pageSize.getWidth(), 0);
        }

        @Override
        public void drawImage(PDPageContentStream contents, PDImageXObject pdImage) throws IOException {
            // pdImage.getWidth(), pdImage.getHeight()
            contents.drawImage(pdImage, 0, 0, PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth());
        }
    },
    PORTRAIT {
        @Override
        public int getRotation() {
            return 0;
        }

        @Override
        public Matrix getTransformMatrix(PDRectangle pageSize) {
            return new Matrix();
        }

        @Override
        public void drawImage(PDPageContentStream contents, PDImageXObject pdImage) throws IOException {
            contents.drawImage(pdImage, 0, 0, PDRectangle.A4.getWidth(), PDRectangle.A4.getHeight());
        }
    };

    public abstract int getRotation();

    public abstract Matrix getTransformMatrix(final PDRectangle pageSize);

    public abstract void drawImage(final PDPageContentStream contents, final PDImageXObject pdImage) throws IOException;
}
