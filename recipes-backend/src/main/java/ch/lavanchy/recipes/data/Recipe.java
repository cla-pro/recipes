package ch.lavanchy.recipes.data;

/**
 * Contains the elements of a recipe
 * <p/>
 * <ul>
 * <li>The filename</li>
 * <li>The list of tags</li>
 * </ul>
 *
 * @since 1.0.0
 */
public class Recipe {
    private final String filename;
    private final String name;

    public Recipe(String filename, String name) {
        this.filename = filename;
        this.name = name;

    }

    public String getFilename() {
        return filename;
    }

    public String getName() {
        return name;
    }
}
