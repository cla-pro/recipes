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
    LANDSCAPE(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()) {
        @Override
        public int getRotation() {
            return 90;
        }

        @Override
        public Matrix getTransformMatrix(final PDRectangle pageSize) {
            return new Matrix(0, 1, -1, 0, pageSize.getWidth(), 0);
        }
    },
    PORTRAIT(PDRectangle.A4.getWidth(), PDRectangle.A4.getHeight()) {
        @Override
        public int getRotation() {
            return 0;
        }

        @Override
        public Matrix getTransformMatrix(final PDRectangle pageSize) {
            return new Matrix();
        }
    };

    private final float drawWidth;
    private final float drawHeight;

    ImageOrientation(final float drawWidth, final float drawHeight) {
        this.drawWidth = drawWidth;
        this.drawHeight = drawHeight;
    }

    public abstract int getRotation();

    public abstract Matrix getTransformMatrix(final PDRectangle pageSize);

    public void drawImage(final PDPageContentStream contents, final PDImageXObject pdImage) throws IOException {
        contents.drawImage(pdImage, 0.0f, 0.0f, drawWidth, drawHeight);
    }}
