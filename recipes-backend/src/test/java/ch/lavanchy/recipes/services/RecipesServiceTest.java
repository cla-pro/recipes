package ch.lavanchy.recipes.services;

import ch.lavanchy.recipes.business.RecipesBusinessLocal;
import ch.lavanchy.recipes.data.Recipe;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Testclass for {@link RecipesService}
 */
@RunWith(MockitoJUnitRunner.class)
public class RecipesServiceTest {
    @Mock
    private RecipesBusinessLocal recipesBusiness;

    @InjectMocks
    private RecipesService recipesService;

    private List<Recipe> recipes;

    @Before
    public void setUp() {
        recipes = Arrays.asList(createRecipe("Croissant au jambon"), createRecipe("Jambon au madere"));
        when(recipesBusiness.findRecipes(anyString())).thenReturn(recipes);
    }

    private Recipe createRecipe(String name) {
        final Recipe recipe = new Recipe("", name);
        return recipe;
    }

    @Test
    public void testGetRecipeListNoFilter() throws Exception {
        final String recipesAsJson = recipesService.getRecipeList(null);

        final List<?> parsed = new Gson().fromJson(recipesAsJson, List.class);
        verify(recipesBusiness).findRecipes(eq(""));
        assertEquals(recipes.size(), parsed.size());
    }

    @Test
    public void testGetRecipeListWithFilter() throws Exception {
        final String filter = "ham cheese";
        final String recipesAsJson = recipesService.getRecipeList(filter);

        final List<?> parsed = new Gson().fromJson(recipesAsJson, List.class);
        verify(recipesBusiness).findRecipes(eq(filter));
    }
}