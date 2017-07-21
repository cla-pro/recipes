package ch.lavanchy.recipes.business;

import ch.lavanchy.recipes.data.Recipe;
import ch.lavanchy.recipes.query.QueryOperation;

import java.util.List;
import java.util.Optional;

/**
 * Provide the logic to handle the recipes' references
 *
 * @since 1.0.0
 */
public interface RecipesBusinessLocal {
    List<Recipe> findRecipesWithFilter(final QueryOperation filter, final Optional<Long> chunkStart, final Optional<Integer> size);

    Optional<Recipe> findRecipeById(final long id);

    Recipe createRecipe(final Recipe recipe);

    Recipe updateRecipe(final Recipe recipe);

    Recipe setRecipeFilename(final long id, final String filename);

    void deleteRecipe(long id);
}
