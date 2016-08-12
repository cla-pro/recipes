package ch.lavanchy.recipes.services;

import ch.lavanchy.recipes.business.RecipesBusinessLocal;
import ch.lavanchy.recipes.data.Recipe;
import ch.lavanchy.recipes.query.*;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    private final QueryOperationFactory queryOperationFactory = new QueryOperationFactory();

    @InjectMocks
    private RecipesService recipesService;

    private List<Recipe> recipes;

    @Before
    public void setUp() {
        recipes = Arrays.asList(createRecipe(1L, "Croissant au jambon"), createRecipe(2L, "Jambon au madere"));
        when(recipesBusiness.findRecipesWithFilter(any(QueryOperation.class), any(Optional.class))).thenReturn(recipes);
    }

    private Recipe createRecipe(final Long id, final String name) {
        return Recipe.builder()
                .withId(id)
                .withFilename("")
                .withName(name)
                .build();
    }

    @Test
    public void testGetRecipeListNoFilter() throws Exception {
        final String recipesAsJson = recipesService.getRecipeList(null, null);

        verify(queryOperationFactory).createQueryOperation(eq(""));
        verify(recipesBusiness).findRecipesWithFilter(eq(new EmptyOp()), eq(Optional.empty()));
        assertThat(new Gson().fromJson(recipesAsJson, List.class)).hasSameSizeAs(recipes);
    }

    @Test
    public void testGetRecipeListWithFilter() throws Exception {
        final String filter = "ham cheese";
        final String recipesAsJson = recipesService.getRecipeList(filter, null);

        verify(queryOperationFactory).createQueryOperation(eq(filter));
        verify(recipesBusiness).findRecipesWithFilter(eq(new AndOp(new TextFilterOp("ham"), new TextFilterOp("cheese"))), eq(Optional.empty()));
        assertThat(new Gson().fromJson(recipesAsJson, List.class)).hasSameSizeAs(recipes);
    }

    @Test
    public void testGetRecipeListWithChunkStart() {
        final String recipesAsJson = recipesService.getRecipeList(null, "start");

        verify(queryOperationFactory).createQueryOperation(eq(""));
        verify(recipesBusiness).findRecipesWithFilter(eq(new EmptyOp()), eq(Optional.of("start")));
        assertThat(new Gson().fromJson(recipesAsJson, List.class)).hasSameSizeAs(recipes);
    }
}