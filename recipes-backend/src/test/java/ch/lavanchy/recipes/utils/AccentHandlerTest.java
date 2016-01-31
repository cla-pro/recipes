package ch.lavanchy.recipes.utils;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for {@link AccentHandler}
 *
 * @since 1.0.0
 */
public class AccentHandlerTest {
    @Test
    public void testRemoveAccent() {
        final AccentHandler accentHandler = new AccentHandler();
        assertThat(accentHandler.removeAccents("jambon")).isEqualTo("jambon");
        assertThat(accentHandler.removeAccents("äâàáa")).isEqualTo("aaaaa");
        assertThat(accentHandler.removeAccents("cç")).isEqualTo("cc");
        assertThat(accentHandler.removeAccents("èêeëé")).isEqualTo("eeeee");
        assertThat(accentHandler.removeAccents("iïìîí")).isEqualTo("iiiii");
        assertThat(accentHandler.removeAccents("oóòôö")).isEqualTo("ooooo");
        assertThat(accentHandler.removeAccents("üûùúu")).isEqualTo("uuuuu");
    }
}