package ch.lavanchy.recipes.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Help class to filter the keywords
 *
 * @since 1.0.0
 */
public class KeywordFilter {
    private final static Set<String> keywords = new HashSet<>();

    static {
        keywords.add("le");
        keywords.add("la");
        keywords.add("là");
        keywords.add("les");
        keywords.add("un");
        keywords.add("une");
        keywords.add("des");
        keywords.add("de");
        keywords.add("à");
        keywords.add("au");
        keywords.add("aux");
        keywords.add("et");
        keywords.add("ou");
        keywords.add("où");
    }

    public List<String> filterKeywords(final List<String> base) {
        return base.stream()
                .filter(text -> !keywords.contains(text.trim()))
                .collect(Collectors.toList());
    }
}
