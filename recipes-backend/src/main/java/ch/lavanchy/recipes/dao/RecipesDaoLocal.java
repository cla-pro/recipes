package ch.lavanchy.recipes.dao;

import ch.lavanchy.recipes.entities.RecipeEntity;
import ch.lavanchy.recipes.query.QueryOperation;

import java.util.List;
import java.util.Optional;

/**
 * Data access to the DB for the recipes.
 *
 * @since 1.0.0
 */
public interface RecipesDaoLocal {
    List<RecipeEntity> findRecipeWithFilter(
            final QueryOperation queryOperation,
            final Optional<String> chunkStart,
            final Optional<Integer> size);

    RecipeEntity findRecipeById(final long id);

    RecipeEntity persistRecipe(final RecipeEntity recipeEntity);

    void deleteRecipe(long id);
}
