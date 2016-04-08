package ch.lavanchy.recipes.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Contains the elements of a recipe
 * <p/>
 * <ul>
 * <li>The id</li>
 * <li>The recipe's name</li>
 * <li>The filename</li>
 * <li>The list of tags</li>
 * </ul>
 *
 * @since 1.0.0
 */
public class Recipe {
    private Long id;
    private String name;
    private String filename;
    private final List<String> tags = new ArrayList<>();

    private Recipe() {}

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

    public static class RecipeBuilder {
        private final Recipe recipe = new Recipe();

        public RecipeBuilder withId(final Long id) {
            recipe.id = id;
            return this;
        }

        public RecipeBuilder withFilename(final String filename) {
            recipe.filename = filename;
            return this;
        }

        public RecipeBuilder withName(final String name) {
            recipe.name = name;
            return this;
        }

        public RecipeBuilder withTags(final List<String> tags) {
            recipe.tags.clear();
            recipe.tags.addAll(tags);
            return this;
        }

        public Recipe build() {
            return recipe;
        }
    }
}
