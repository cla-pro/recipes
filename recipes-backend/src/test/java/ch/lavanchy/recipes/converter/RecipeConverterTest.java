package ch.lavanchy.recipes.converter;

import ch.lavanchy.recipes.data.Recipe;
import ch.lavanchy.recipes.entities.RecipeEntity;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Testclass for {@link RecipeConverter}
 */
public class RecipeConverterTest {

    @Test
    public void testConvertRecipeEntityListToRecipeEmptyList() {
        final List<RecipeEntity> recipeEntityList = new ArrayList<>();

        final List<Recipe> recipeList = new RecipeConverter().convertRecipeEntityListToRecipe(recipeEntityList);
        assertThat(recipeList).isEmpty();
    }

    @Test
    public void testConvertRecipeEntityListToRecipe() {
        final List<RecipeEntity> recipeEntityList = Arrays.asList(createRecipeEntity("Croissant au jambon"), createRecipeEntity("Jambon au madere"));

        final List<Recipe> recipeList = new RecipeConverter().convertRecipeEntityListToRecipe(recipeEntityList);
        assertThat(recipeEntityList).hasSize(recipeList.size());
    }

    @Test
    public void testConvertRecipeEntityToRecipe() {
        final RecipeEntity recipeEntity = createRecipeEntity("Croissant au jambon");

        final Recipe recipe = new RecipeConverter().convertRecipeEntityToRecipe(recipeEntity);
        assertThat(recipeEntity.getId()).isEqualTo(recipe.getId());
        assertThat(recipeEntity.getName()).isEqualTo(recipe.getName());
        assertThat(recipeEntity.getFilename()).isEqualTo(recipe.getFilename());
    }

    @Test
    public void testConvertRecipeToRecipeEntity() {
        final Recipe recipe = createRecipe("Croissant au jambon");

        final RecipeEntity recipeEntity = new RecipeConverter().convertRecipeToRecipeEntity(recipe);
        assertThat(recipeEntity.getId()).isEqualTo(recipe.getId());
        assertThat(recipeEntity.getName()).isEqualTo(recipe.getName());
        assertThat(recipeEntity.getFilename()).isEqualTo(recipe.getFilename());
    }

    private RecipeEntity createRecipeEntity(String name) {
        final RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setId(4L);
        recipeEntity.setName(name);
        return recipeEntity;
    }

    private Recipe createRecipe(String name) {
        return new Recipe(4L, null, name);
    }
}