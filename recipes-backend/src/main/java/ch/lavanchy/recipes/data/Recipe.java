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
    private final Long id;
    private final String name;
    private final String filename;

    public Recipe(final Long id, final String filename, final String name) {
        this.id = id;
        this.filename = filename;
        this.name = name;

    }

    public Long getId() {
        return id;
    }

    public String getFilename() {
        return filename;
    }

    public String getName() {
        return name;
    }
}
