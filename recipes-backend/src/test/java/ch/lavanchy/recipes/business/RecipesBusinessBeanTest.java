package ch.lavanchy.recipes.business;

import ch.lavanchy.recipes.dao.RecipesDaoLocal;
import ch.lavanchy.recipes.data.Recipe;
import ch.lavanchy.recipes.entities.RecipeEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Testclass for {@link RecipesBusinessBean}
 */
@RunWith(MockitoJUnitRunner.class)
public class RecipesBusinessBeanTest {
    @Mock
    private RecipesDaoLocal recipesDao;

    @InjectMocks
    private RecipesBusinessLocal recipesBusiness = new RecipesBusinessBean();

    private List<RecipeEntity> recipeEntities;

    @Before
    public void setUp() {
        recipeEntities = Arrays.asList(createRecipeEntity("Croissant au jambon"), createRecipeEntity("Jambon au madere"));

        when(recipesDao.findAllRecipes()).thenReturn(recipeEntities);
    }

    private RecipeEntity createRecipeEntity(String name) {
        final RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setName(name);
        return recipeEntity;
    }

    @Test
    public void testFindRecipesNoFilter() throws Exception {
        final List<Recipe> recipes = recipesBusiness.findRecipes("");
        assertEquals(recipeEntities.size(), recipes.size());
    }

    @Test
    public void testFindRecipesSpaceFilter() throws Exception {
        final List<Recipe> recipes = recipesBusiness.findRecipes("   ");
        assertEquals(recipeEntities.size(), recipes.size());
    }

    @Test
    public void testFindRecipesNoMatch() throws Exception {
        final List<Recipe> recipes = recipesBusiness.findRecipes("cheese");
        assertTrue(recipes.isEmpty());
    }

    @Test
    public void testFindRecipesPartialMatch() throws Exception {
        final List<Recipe> recipes = recipesBusiness.findRecipes("croissant");
        assertEquals(1, recipes.size());
    }

    @Test
    public void testFindRecipesFullMatch() throws Exception {
        final List<Recipe> recipes = recipesBusiness.findRecipes("jambon");
        assertEquals(recipeEntities.size(), recipes.size());
    }
}