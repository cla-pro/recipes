package ch.lavanchy.recipes.dao;

import ch.lavanchy.recipes.entities.RecipeEntity;
import ch.lavanchy.recipes.query.QueryOperation;

import java.util.List;

/**
 * Data access to the DB for the recipes.
 *
 * @since 1.0.0
 */
public interface RecipesDaoLocal {
    /**
     * Get all the recipes without any filtering.
     *
     * @return The whole list of recipes
     */
    List<RecipeEntity> findAllRecipes();

    /**
     * Find all the recipes that matches with the given queryOperation (filter by name and tags).
     *
     * @param queryOperation The query filter
     * @return All the matching recipes
     */
    List<RecipeEntity> findRecipeWithFilter(QueryOperation queryOperation);

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
