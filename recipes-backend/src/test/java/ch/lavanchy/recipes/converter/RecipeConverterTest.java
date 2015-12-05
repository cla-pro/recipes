package ch.lavanchy.recipes.converter;

import ch.lavanchy.recipes.data.Recipe;
import ch.lavanchy.recipes.entities.RecipeEntity;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Testclass for {@link RecipeConverter}
 */
public class RecipeConverterTest {

    @Test
    public void testConvertRecipeEntityListToRecipeEmptyList() throws Exception {
        final List<RecipeEntity> recipeEntityList = new ArrayList<>();

        final List<Recipe> recipeList = new RecipeConverter().convertRecipeEntityListToRecipe(recipeEntityList);
        assertTrue(recipeList.isEmpty());
    }

    @Test
    public void testConvertRecipeEntityListToRecipe() throws Exception {
        final List<RecipeEntity> recipeEntityList = Arrays.asList(createRecipeEntity("Croissant au jambon"), createRecipeEntity("Jambon au madere"));

        final List<Recipe> recipeList = new RecipeConverter().convertRecipeEntityListToRecipe(recipeEntityList);
        assertEquals(recipeEntityList.size(), recipeList.size());
    }

    @Test
    public void testConvertRecipeEntityToRecipe() throws Exception {
        final RecipeEntity recipeEntity = createRecipeEntity("Croissant au jambon");

        final Recipe recipe = new RecipeConverter().convertRecipeEntityToRecipe(recipeEntity);
        assertEquals(recipeEntity.getName(), recipe.getName());
        assertEquals("", recipe.getFilename());
    }

    private RecipeEntity createRecipeEntity(String name) {
        final RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setName(name);
        return recipeEntity;
    }
}