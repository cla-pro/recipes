package ch.lavanchy.recipes.data;

import java.util.ArrayList;
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
    private int rating;
    private final List<String> tags = new ArrayList<>();

    private Recipe() {
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

    public int getRating() {
        return rating;
    }

    public List<String> getTags() {
        return tags;
    }

    public static RecipeBuilder builder() {
        return new RecipeBuilder();
    }

    public static RecipeBuilder builder(final Recipe base) {
        return new RecipeBuilder()
                .withId(base.id)
                .withFilename(base.filename)
                .withName(base.name)
                .withRating(base.rating)
                .withTags(base.tags);
    }

    public static class RecipeBuilder {
        private final Recipe recipe = new Recipe();

        private RecipeBuilder() {
        }

        public final RecipeBuilder withId(final Long id) {
            recipe.id = id;
            return this;
        }

        public final RecipeBuilder withFilename(final String filename) {
            recipe.filename = filename;
            return this;
        }

        public final RecipeBuilder withName(final String name) {
            recipe.name = name;
            return this;
        }

        public final RecipeBuilder withRating(final float rating) {
            recipe.rating = Math.round(rating);
            return this;
        }

        public final RecipeBuilder withTags(final List<String> tags) {
            recipe.tags.clear();
            recipe.tags.addAll(tags);
            return this;
        }

        public Recipe build() {
            return recipe;
        }
    }
}
