package ch.lavanchy.recipes.utils;

/**
 * Help class to deal with the accents
 *
 * @since 1.0.0
 */
public class AccentHandler {
    public String removeAccents(final String text) {
        return text
                .replaceAll("[áàäâ]", "a")
                .replaceAll("[ç]", "c")
                .replaceAll("[éèëê]", "e")
                .replaceAll("[íìïî]", "i")
                .replaceAll("[óòöô]", "o")
                .replaceAll("[úùüû]", "u");
    }
}
