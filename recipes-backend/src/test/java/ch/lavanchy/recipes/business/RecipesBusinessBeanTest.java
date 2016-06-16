package ch.lavanchy.recipes.business;

import ch.lavanchy.recipes.dao.RecipesDaoLocal;
import ch.lavanchy.recipes.dao.TagsDaoLocal;
import ch.lavanchy.recipes.data.Recipe;
import ch.lavanchy.recipes.entities.RecipeEntity;
import ch.lavanchy.recipes.entities.TagEntity;
import ch.lavanchy.recipes.factories.RecipeFactory;
import ch.lavanchy.recipes.query.QueryOperation;
import ch.lavanchy.recipes.query.TextFilterOp;
import ch.lavanchy.recipes.utils.AccentHandler;
import ch.lavanchy.recipes.utils.KeywordFilter;
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
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Testclass for {@link RecipesBusinessBean}
 *
 * @since 1.0.0
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
    private KeywordFilter keywordFilter = new KeywordFilter();

    @Spy
    private RecipeFactory recipeFactory = new RecipeFactory();

    @Spy
    private FilenameFixer filenameFixer = new FilenameFixer();

    @InjectMocks
    private RecipesBusinessLocal recipesBusiness = new RecipesBusinessBean();

    private List<RecipeEntity> recipeEntities;

    @Before
    public void setUp() {
        recipeEntities = Arrays.asList(createRecipeEntity("Croissant au jambon"), createRecipeEntity("Jambon au madere"));

        when(recipesDao.findRecipeWithFilter(any(QueryOperation.class))).thenReturn(recipeEntities);
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
    public void testFindRecipesWithFilter() {
        final TextFilterOp queryOperation = new TextFilterOp("myFilter");
        final List<Recipe> recipes = recipesBusiness.findRecipesWithFilter(queryOperation);
        verify(recipesDao).findRecipeWithFilter(eq(queryOperation));
        assertThat(recipes).hasSameSizeAs(recipeEntities);
    }

    @Test
    public void testCreateRecipe() {
        final String name = "recipeName";
        final Recipe created = recipesBusiness.createRecipe(Recipe.builder()
                .withName(name)
                .withFilename("")
                .withTags(Arrays.asList("DESSERT", "strawberry"))
                .build());

        verify(recipesDao).persistRecipe(any(RecipeEntity.class));
        verify(tagsDao).findAllTags();
        verify(tagsDao).persistTag(any(TagEntity.class));
        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).isEqualTo(name);
        assertThat(created.getTags()).isEqualTo(Arrays.asList("dessert", "strawberry"));
    }

    @Test
    public void testUpdateRecipe() {
        final RecipeEntity recipeMock = new RecipeEntity();
        recipeMock.getTags().add(createTagEntity("dessert"));
        recipeMock.getTags().add(createTagEntity("blackberry"));
        recipeMock.setName("newName");
        when(recipesDao.findRecipeById(anyInt())).thenReturn(recipeMock);

        final Recipe recipe = Recipe.builder()
                .withId(3L)
                .withFilename("filename")
                .withName("newName")
                .withTags(Arrays.asList("DESSERT", "strawberry"))
                .build();
        final Recipe updatedRecipe = recipesBusiness.updateRecipe(recipe);

        verify(tagsDao).persistTag(any(TagEntity.class));
        assertThat(updatedRecipe.getTags()).hasSize(2);
        assertThat(updatedRecipe.getName()).isEqualTo("newName");
    }

    @Test
    public void testCleanupTags() {
        final String name = "recipeName";
        final Recipe created = recipesBusiness.createRecipe(
                Recipe.builder()
                        .withName(name)
                        .withFilename("")
                        .withTags(Arrays.asList(null, "", "DESSERT", "à", "LA", "STRAWBERRY", "strawberry"))
                        .build());
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