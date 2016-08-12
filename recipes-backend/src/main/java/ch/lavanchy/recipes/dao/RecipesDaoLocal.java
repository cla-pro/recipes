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
    /**
     * Find all the recipes that matches with the given queryOperation (filter by name and tags).
     *
     * @param queryOperation The query filter
     * @param chunkStart String used to get the next chunk, the chunkStart is not part of the result (>)
     * @return All the matching recipes
     */
    List<RecipeEntity> findRecipeWithFilter(final QueryOperation queryOperation, final Optional<String> chunkStart);

    /**
     * Find a recipe by id.
     *
     * @param id The recipe's id
     * @return The recipe.
     */
    RecipeEntity findRecipeById(final long id);

    /**
     * Persist the given recipe entity.
     *
     * @param recipeEntity The recipe to persist
     * @return The persisted entity
     */
    RecipeEntity persistRecipe(final RecipeEntity recipeEntity);
}
