package ch.lavanchy.recipes.data;

/**
 * Contains the elements of a recipe
 *
 * <ul>
 *     <li>The filename</li>
 *     <li>The list of tags</li>
 * </ul>
 *
 * @since 1.0.0
 */
public class Recipe {
    private final String filename;

    public Recipe(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
