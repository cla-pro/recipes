package ch.lavanchy.recipes.converter;

import ch.lavanchy.recipes.data.Recipe;
import ch.lavanchy.recipes.entities.RecipeEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to convert the {@link RecipeEntity} to {@link Recipe}
 *
 * @since 1.0.0
 */
public final class RecipeConverter {
    /**
     * Convert a list of {@link RecipeEntity} to a list of {@link Recipe}
     *
     * @param recipeEntities The list to convert
     * @return The converted list
     */
    public List<Recipe> convertRecipeEntityListToRecipe(final List<RecipeEntity> recipeEntities) {
        final List<Recipe> recipes = new ArrayList<>();

        for (RecipeEntity recipeEntity : recipeEntities) {
            recipes.add(convertRecipeEntityToRecipe(recipeEntity));
        }

        return recipes;
    }

    /**
     * Convert a single {@link RecipeEntity} to a {@link Recipe}.
     *
     * @param recipeEntity The entity to convert
     * @return The converted object
     */
    public Recipe convertRecipeEntityToRecipe(final RecipeEntity recipeEntity) {
        final Recipe recipe = new Recipe(recipeEntity.getId(), "", recipeEntity.getName());
        return recipe;
    }
}
