package ch.lavanchy.recipes.factories.converters;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @since 2.0.0
 */
public class ImageToPDFConverterTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void testConvertToPDFInFileAndReadable() throws Exception {
        final File sourceFile = new File(getClass().getClassLoader().getResource("recipes/landscape.jpg").getFile());
        final File targetFile = folder.newFile("my_first_recipe-as_image.pdf");

        assertThat(sourceFile.exists()).isTrue();
        assertThat(targetFile.exists()).isTrue();
        assertThat(targetFile.length()).isEqualTo(0L);

        final ImageToPDFConverter imageToPDFConverter = new ImageToPDFConverter();
        final File result = imageToPDFConverter.convertToPDFInFile(sourceFile, targetFile);
        assertThat(result).isNotNull();
        assertThat(result.exists()).isTrue();
        assertThat(result.length()).isGreaterThan(0L);

        folder.delete();
    }
}