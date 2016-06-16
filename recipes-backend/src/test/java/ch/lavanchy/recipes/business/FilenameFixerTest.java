package ch.lavanchy.recipes.business;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FilenameFixerTest {
    private static final String EXTENSION = "pdf";

    @Test
    public void testFixFilenameValidShouldNotChange() throws Exception {
        final String validFilename = "my_file_name";

        final String fixedFilename = new FilenameFixer().fixFilename(validFilename, EXTENSION);

        assertThat(fixedFilename).isEqualTo(validFilename + "." + EXTENSION);
    }

    @Test
    public void testFixFilenameInvalidCharsShouldBeRemoved() throws Exception {
        final String invalidFilename = ",";

        final String fixedFilename = new FilenameFixer().fixFilename(invalidFilename, EXTENSION);

        assertThat(fixedFilename).isEqualTo("." + EXTENSION);
    }

    @Test
    public void testFixFilenameSpaceShouldBeReplacedByUnderscore() throws Exception {
        final String invalidFilename = "a b c";

        final String fixedFilename = new FilenameFixer().fixFilename(invalidFilename, EXTENSION);

        assertThat(fixedFilename).isEqualTo(invalidFilename.replaceAll(" ", "_") + "." + EXTENSION);
    }
}