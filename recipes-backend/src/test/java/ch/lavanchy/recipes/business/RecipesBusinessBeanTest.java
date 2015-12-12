package ch.lavanchy.recipes.business;

import ch.lavanchy.recipes.dao.RecipesDaoLocal;
import ch.lavanchy.recipes.data.Recipe;
import ch.lavanchy.recipes.entities.RecipeEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
    private final long knownId = 123L;

    @Before
    public void setUp() {
        recipeEntities = Arrays.asList(createRecipeEntity("Croissant au jambon"), createRecipeEntity("Jambon au madere"));

        when(recipesDao.findAllRecipes()).thenReturn(recipeEntities);
        when(recipesDao.persistRecipe(any(RecipeEntity.class))).thenAnswer(new Answer<RecipeEntity>() {
            @Override
            public RecipeEntity answer(InvocationOnMock invocation) throws Throwable {
                final RecipeEntity param = (RecipeEntity) invocation.getArguments()[0];
                param.setId(0L);
                return param;
            }
        });
        when(recipesDao.findRecipeById(eq(knownId))).thenReturn(createRecipeEntity("My Recipe"));
    }

    private RecipeEntity createRecipeEntity(String name) {
        final RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setName(name);
        return recipeEntity;
    }

    @Test
    public void testFindRecipesNoFilter() {
        final List<Recipe> recipes = recipesBusiness.findRecipes("");
        assertThat(recipeEntities).hasSameSizeAs(recipes);
    }

    @Test
    public void testFindRecipesSpaceFilter() {
        final List<Recipe> recipes = recipesBusiness.findRecipes("   ");
        assertThat(recipeEntities).hasSameSizeAs(recipes);
    }

    @Test
    public void testFindRecipesNoMatch() {
        final List<Recipe> recipes = recipesBusiness.findRecipes("cheese");
        assertThat(recipes).isEmpty();
    }

    @Test
    public void testFindRecipesPartialMatch() {
        final List<Recipe> recipes = recipesBusiness.findRecipes("croissant");
        assertThat(recipes).hasSize(1);
    }

    @Test
    public void testFindRecipesFullMatch() {
        final List<Recipe> recipes = recipesBusiness.findRecipes("jambon");
        assertThat(recipeEntities).hasSameSizeAs(recipes);
    }

    @Test
    public void testCreateRecipe() {
        final String name = "recipeName";
        final Recipe created = recipesBusiness.createRecipe(new Recipe(null, null, name));

        verify(recipesDao).persistRecipe(any(RecipeEntity.class));
        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).isEqualTo(name);
    }

    @Test
    public void testSetRecipeFilename() {
        final String filename = "recipe.xml";

        final Recipe recipe = recipesBusiness.setRecipeFilename(knownId, filename);
        assertThat(recipe.getFilename()).isEqualTo(filename);
    }

    @Test
    public void testSetRecipeFilenameNotFound() {
        Recipe persistedRecipe = recipesBusiness.setRecipeFilename(456L, "recipe.xml");
        assertThat(persistedRecipe).isNull();
    }
}