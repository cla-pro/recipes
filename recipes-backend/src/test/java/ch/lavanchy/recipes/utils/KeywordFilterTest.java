package ch.lavanchy.recipes.utils;

import org.junit.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testclass for {@link KeywordFilter}
 *
 * @since 1.0.0
 */
public class KeywordFilterTest {
    @Test
    public void testFilterKeywords() {
        final KeywordFilter keywordFilter = new KeywordFilter();

        assertThat(keywordFilter.filterKeywords(Arrays.asList("de", "la", "une"))).isEmpty();
        assertThat(keywordFilter.filterKeywords(Arrays.asList("de", "bonjour", "une"))).hasSize(1);
    }
}