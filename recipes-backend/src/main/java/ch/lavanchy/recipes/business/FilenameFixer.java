package ch.lavanchy.recipes.business;

import org.apache.commons.io.FilenameUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Fix the filename
 *
 * @since 2.0.0
 */
class FilenameFixer {
    private final static Map<String, String> MAPPED_FILENAME_CHARS = new HashMap<>();

    static {
        MAPPED_FILENAME_CHARS.put(",", "");

        MAPPED_FILENAME_CHARS.put(" ", "_");
    }

    String fixFilename(final String filename, final String extension) {
        return replaceMappedChars(filename) + FilenameUtils.EXTENSION_SEPARATOR_STR + extension;
    }

    private String replaceMappedChars(final String toBeFixed) {
        return MAPPED_FILENAME_CHARS
                .keySet()
                .stream()
                .reduce(toBeFixed, (acc, mapped) -> acc.replaceAll(mapped, MAPPED_FILENAME_CHARS.get(mapped)));
    }
}
