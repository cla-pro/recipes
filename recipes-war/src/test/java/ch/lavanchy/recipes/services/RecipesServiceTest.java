package ch.lavanchy.recipes.services;

import ch.lavanchy.recipes.business.RecipesBusinessLocal;
import ch.lavanchy.recipes.data.Recipe;
import ch.lavanchy.recipes.query.AndOp;
import ch.lavanchy.recipes.query.EmptyOp;
import ch.lavanchy.recipes.query.QueryOperation;
import ch.lavanchy.recipes.query.QueryOperationFactory;
import ch.lavanchy.recipes.query.TextFilterOp;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Testclass for {@link RecipesService}
 */
@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class RecipesServiceTest {
    @Mock
    private RecipesBusinessLocal recipesBusiness;

    @Spy
    private QueryOperationFactory queryOperationFactory = new QueryOperationFactory();

    @InjectMocks
    private RecipesService recipesService;

    private List<Recipe> recipes;

    @Before
    public void setUp() {
        recipes = Arrays.asList(createRecipe(1L, "Croissant au jambon"), createRecipe(2L, "Jambon au madere"));
        when(recipesBusiness.findRecipesWithFilter(any(QueryOperation.class))).thenReturn(recipes);
    }

    private Recipe createRecipe(final Long id, final String name) {
        return new Recipe(id, "", name, Collections.<String> emptyList());
    }

    @Test
    public void testGetRecipeListNoFilter() throws Exception {
        final String recipesAsJson = recipesService.getRecipeList(null);

        verify(queryOperationFactory).createQueryOperation(eq(""));
        verify(recipesBusiness).findRecipesWithFilter(eq(new EmptyOp()));
        assertThat(new Gson().fromJson(recipesAsJson, List.class)).hasSameSizeAs(recipes);
    }

    @Test
    public void testGetRecipeListWithFilter() throws Exception {
        final String filter = "ham cheese";
        final String recipesAsJson = recipesService.getRecipeList(filter);

        verify(queryOperationFactory).createQueryOperation(eq(filter));
        verify(recipesBusiness).findRecipesWithFilter(eq(new AndOp(new TextFilterOp("ham"), new TextFilterOp("cheese"))));
        assertThat(new Gson().fromJson(recipesAsJson, List.class)).hasSameSizeAs(recipes);
    }
}