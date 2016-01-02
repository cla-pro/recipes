package ch.lavanchy.recipes.converter;

import ch.lavanchy.recipes.data.Recipe;
import ch.lavanchy.recipes.entities.RecipeEntity;
import ch.lavanchy.recipes.entities.TagEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to convert the {@link RecipeEntity} to {@link Recipe}
 *
 * @since 1.0.0
 */
public final class RecipeConverter {
    /**
     * Convert a list of {@link RecipeEntity} into a list of {@link Recipe}
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
     * Convert a single {@link RecipeEntity} into a {@link Recipe}.
     *
     * @param recipeEntity The entity to convert
     * @return The converted object
     */
    public Recipe convertRecipeEntityToRecipe(final RecipeEntity recipeEntity) {
        return new Recipe(recipeEntity.getId(), recipeEntity.getFilename(), recipeEntity.getName(), extractTags(recipeEntity));
    }

    private List<String> extractTags(RecipeEntity recipeEntity) {
        final List<String> tags = new ArrayList<>();
        for (TagEntity tagEntity : recipeEntity.getTags()) {
            tags.add(tagEntity.getName());
        }

        return tags;
    }

    /**
     * Convert a single {@link Recipe} into a {@link RecipeEntity}
     *
     * @param recipe The recipe to convert
     * @return The converted entity
     */
    public RecipeEntity convertRecipeToRecipeEntity(final Recipe recipe) {
        final RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setId(recipe.getId());
        recipeEntity.setName(recipe.getName());
        recipeEntity.setFilename(recipe.getFilename());
        return recipeEntity;
    }
}
