package ch.lavanchy.recipes.factories;

import ch.lavanchy.recipes.data.Recipe;
import ch.lavanchy.recipes.entities.RecipeEntity;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class used to create the {@link RecipeEntity} and the {@link Recipe}
 *
 * @since 1.0.0
 */
public class RecipeFactory {
    private final static Set<String> INVALID_FILENAME_CHARS = Stream.of(",").collect(Collectors.toSet());
    private final static String EMPTY_STRING = "";

    /**
     * Convert a list of {@link RecipeEntity} into a list of {@link Recipe}
     *
     * @param recipeEntities The list to convert
     * @return The converted list
     */
    public List<Recipe> convertRecipeEntityListToRecipe(final List<RecipeEntity> recipeEntities) {
        return recipeEntities
                .stream()
                .map(recipeEntity -> convertRecipeEntityToRecipe(recipeEntity))
                .collect(Collectors.toList());
    }

    /**
     * Convert a single {@link RecipeEntity} into a {@link Recipe}.
     *
     * @param recipeEntity The entity to convert
     * @return The converted object
     */
    public Recipe convertRecipeEntityToRecipe(final RecipeEntity recipeEntity) {
        return Recipe.builder()
                .withId(recipeEntity.getId())
                .withFilename(recipeEntity.getFilename())
                .withName(recipeEntity.getName())
                .withRating(recipeEntity.getRating())
                .withTags(extractTags(recipeEntity))
                .build();
    }

    private List<String> extractTags(RecipeEntity recipeEntity) {
        return recipeEntity
                .getTags()
                .stream()
                .map(tagEntity -> tagEntity.getName())
                .collect(Collectors.toList());
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
        recipeEntity.setRating(recipe.getRating());
        return recipeEntity;
    }
}
