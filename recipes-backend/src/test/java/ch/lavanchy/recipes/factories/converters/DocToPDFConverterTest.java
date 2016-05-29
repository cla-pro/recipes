package ch.lavanchy.recipes.factories.converters;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @since 2.0.0
 */
public class DocToPDFConverterTest {
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void convertToPDFInFile() throws Exception {
        final File sourceFile = new File(getClass().getClassLoader().getResource("recipes/recipe-1.jpg").getFile());
        final File targetFile = folder.newFile("converted.pdf");

        assertThat(sourceFile.exists()).isTrue();
        assertThat(targetFile.exists()).isTrue();
        assertThat(targetFile.length()).isEqualTo(0L);

        final ImageToPDFConverter imageToPDFConverter = new ImageToPDFConverter();
        final File result = imageToPDFConverter.convertToPDFInFile(sourceFile, targetFile);
        assertThat(result).isNotNull();
        assertThat(result.exists()).isTrue();
        assertThat(result.length()).isGreaterThan(0L);
    }
}