package ch.lavanchy.recipes.factories.converters;

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.document.DocumentKind;
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
        final File sourceFile = new File(getClass().getClassLoader().getResource("recipes/recipe-1.odt").getFile());
        final File targetFile = folder.newFile("converted.pdf");

        assertThat(sourceFile.exists()).isTrue();
        assertThat(targetFile.exists()).isTrue();

        final DocToPDFConverter docToPDFConverter =
                new DocToPDFConverter(Options.getFrom(DocumentKind.ODT).to(ConverterTypeTo.PDF));
        final File result = docToPDFConverter.convertToPDFInFile(sourceFile, targetFile);
        assertThat(result).isNotNull();
        assertThat(result.exists()).isTrue();
        assertThat(result.length()).isGreaterThan(0L);

        folder.delete();
    }
}