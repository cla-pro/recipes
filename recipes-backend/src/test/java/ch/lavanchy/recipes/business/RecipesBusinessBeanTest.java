package ch.lavanchy.recipes.business;

import ch.lavanchy.recipes.converter.RecipeConverter;
import ch.lavanchy.recipes.dao.RecipesDaoLocal;
import ch.lavanchy.recipes.dao.TagsDaoLocal;
import ch.lavanchy.recipes.data.Recipe;
import ch.lavanchy.recipes.entities.RecipeEntity;
import ch.lavanchy.recipes.entities.TagEntity;
import ch.lavanchy.recipes.utils.AccentHandler;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Testclass for {@link RecipesBusinessBean}
 */
@RunWith(MockitoJUnitRunner.class)
public class RecipesBusinessBeanTest {
    private final long knownRecipeId = 123L;
    private final String knownTagName = "dessert";

    @Mock
    private RecipesDaoLocal recipesDao;

    @Mock
    private TagsDaoLocal tagsDao;

    @Spy
    private AccentHandler accentHandler = new AccentHandler();

    @Spy
    private RecipeConverter recipeConverter = new RecipeConverter();

    @InjectMocks
    private RecipesBusinessLocal recipesBusiness = new RecipesBusinessBean();

    private List<RecipeEntity> recipeEntities;

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
        when(recipesDao.findRecipeById(eq(knownRecipeId))).thenReturn(createRecipeEntity("My Recipe"));

        when(tagsDao.findAllTags()).thenReturn(Collections.singletonList(createTagEntity(knownTagName)));
        when(tagsDao.persistTag(any(TagEntity.class))).thenAnswer(new Answer<TagEntity>() {
            @Override
            public TagEntity answer(InvocationOnMock invocation) throws Throwable {
                final TagEntity param = (TagEntity) invocation.getArguments()[0];
                param.setId(0L);
                return param;
            }
        });
    }

    private TagEntity createTagEntity(final String knownTagName) {
        final TagEntity tagEntity = new TagEntity();
        tagEntity.setName(knownTagName);
        return tagEntity;
    }

    private RecipeEntity createRecipeEntity(final String name) {
        final RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setName(name);
        return recipeEntity;
    }

    @Test
    public void testFindRecipesNoFilter() {
        final List<Recipe> recipes = recipesBusiness.findRecipes("");
        assertThat(recipes).hasSameSizeAs(recipeEntities);
    }

    @Test
    public void testFindRecipesSpaceFilter() {
        final List<Recipe> recipes = recipesBusiness.findRecipes("   ");
        assertThat(recipes).hasSameSizeAs(recipeEntities);
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
        assertThat(recipes).hasSameSizeAs(recipeEntities);
    }

    @Test
    public void testFindRecipesAccentMatch() {
        final List<Recipe> recipes = recipesBusiness.findRecipes("jâmbón");
        assertThat(recipes).hasSameSizeAs(recipeEntities);
    }

    @Test
    public void testCreateRecipe() {
        final String name = "recipeName";
        final Recipe created = recipesBusiness.createRecipe(new Recipe(null, null, name, Arrays.asList("DESSERT", "strawberry")));

        verify(recipesDao).persistRecipe(any(RecipeEntity.class));
        verify(tagsDao).findAllTags();
        verify(tagsDao).persistTag(any(TagEntity.class));
        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).isEqualTo(name);
        assertThat(created.getTags()).isEqualTo(Arrays.asList("dessert", "strawberry"));
    }

    @Test
    public void testCleanupTags() {
        final String name = "recipeName";
        final Recipe created = recipesBusiness.createRecipe(new Recipe(null, null, name, Arrays.asList(null, "", "DESSERT", "STRAWBERRY", "strawberry")));
        assertThat(created.getTags()).isEqualTo(Arrays.asList("dessert", "strawberry"));
    }

    @Test
    public void testSetRecipeFilename() {
        final String filename = "recipe.xml";

        final Recipe recipe = recipesBusiness.setRecipeFilename(knownRecipeId, filename);
        assertThat(recipe.getFilename()).isEqualTo(filename);
    }

    @Test
    public void testSetRecipeFilenameNotFound() {
        Recipe persistedRecipe = recipesBusiness.setRecipeFilename(456L, "recipe.xml");
        assertThat(persistedRecipe).isNull();
    }
}