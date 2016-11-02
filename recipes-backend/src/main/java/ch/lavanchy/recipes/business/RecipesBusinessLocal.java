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
    /**
     * Find all the recipes matching the given filter
     *
     * @param filter Filter given as a string. Will be parsed to extract each word.
     * @param chunkStart String used to get the next chunk, the chunkStart is not part of the result (>).
     * @param size The size of the result list to return, return all if empty.
     * @return The matching recipes
     */
    List<Recipe> findRecipesWithFilter(final QueryOperation filter, final Optional<Long> chunkStart, final Optional<Integer> size);

    /**
     * Find a single {@link Recipe} by id
     *
     * @param id The id to search
     * @return The recipe or null if not found
     */
    Recipe findRecipeById(final long id);

    /**
     * Create a new recipe into the system.
     *
     * @param recipe The information about the recipe to save
     * @return The persisted recipe
     */
    Recipe createRecipe(final Recipe recipe);

    /**
     * Update a recipe into the system.
     *
     * @param recipe The information about the recipe to update
     * @return The persisted recipe
     */
    Recipe updateRecipe(final Recipe recipe);

    /**
     * Update the recipe with the filename
     *
     * @param id The recipe's id
     * @param filename The filename to save
     * @return The updated recipe
     */
    Recipe setRecipeFilename(final long id, final String filename);
}
