package ch.lavanchy.recipes.data;

import java.util.Collections;
import java.util.List;

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
    private final List<String> tags;

    public Recipe() {
        this(null, null, null, Collections.<String>emptyList());
    }

    public Recipe(final Long id, final String filename, final String name, List<String> tags) {
        this.id = id;
        this.filename = filename;
        this.name = name;
        this.tags = tags;
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

    public List<String> getTags() {
        return tags;
    }
}
