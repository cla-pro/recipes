package ch.lavanchy.recipes.factories.converters;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @since 2.0.0
 */
public class ToPDFConverterFactoryTest {
    @Test
    public void testCreateConverter() throws Exception {
        final ToPDFConverterFactory testee = new ToPDFConverterFactory();

        assertThat(testee.createConverter("jpg")).isInstanceOf(ImageToPDFConverter.class);
        assertThat(testee.createConverter("JpG")).isInstanceOf(ImageToPDFConverter.class);
        assertThat(testee.createConverter("JPG")).isInstanceOf(ImageToPDFConverter.class);
        assertThat(testee.createConverter("jpeg")).isInstanceOf(ImageToPDFConverter.class);
        assertThat(testee.createConverter("BMP")).isInstanceOf(ImageToPDFConverter.class);
        assertThat(testee.createConverter("PNG")).isInstanceOf(ImageToPDFConverter.class);

        assertThat(testee.createConverter("docx")).isInstanceOf(DocToPDFConverter.class);
        assertThat(testee.createConverter("odt")).isInstanceOf(DocToPDFConverter.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateConverterUnknown() {
        new ToPDFConverterFactory().createConverter("lkdsajfé");
    }
}