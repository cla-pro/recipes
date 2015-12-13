package ch.lavanchy.recipes.dao;

import ch.lavanchy.recipes.entities.RecipeEntity;

import java.util.List;

/**
 * Data access to the DB for the recipes
 *
 * @since 1.0.0
 */
public interface RecipesDaoLocal {
    /**
     * Get all the recipes without any filtering
     *
     * @return The whole list of recipes
     */
    List<RecipeEntity> findAllRecipes();

    /**
     * Find all the recipes whose name matches the given filter
     * @param filter Word that must be included in the recipe's name
     * @return All the recipes whose name matches the filter
     */
    List<RecipeEntity> findRecipesFilteredByName(final String filter);

    /**
     * Find a recipe by id
     *
     * @param id The recipe's id
     * @return The recipe.
     */
    RecipeEntity findRecipeById(final long id);

    /**
     * Persist the given recipe entity
     *
     * @param recipeEntity The recipe to persist
     * @return The persisted entity
     */
    RecipeEntity persistRecipe(final RecipeEntity recipeEntity);
}
