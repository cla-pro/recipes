package ch.lavanchy.recipes.business;

import ch.lavanchy.recipes.data.Recipe;

import java.util.List;

/**
 * Provide the logic to retrieve the recipes' references
 *
 * @since 1.0.0
 */
public interface RecipesBusinessLocal {
    List<Recipe> findRecipes(String filter);
}
