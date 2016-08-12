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
import java.util.Optional;

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
    private static final long KNOWN_RECIPE_ID = 123L;
    private static final String KNOWN_TAG_NAME = "dessert";

    @Mock
    private RecipesDaoLocal recipesDao;

    @Mock
    private TagsDaoLocal tagsDao;

    @Spy
    private final AccentHandler accentHandler = new AccentHandler();

    @Spy
    private final KeywordFilter keywordFilter = new KeywordFilter();

    @Spy
    private final RecipeFactory recipeFactory = new RecipeFactory();

    @Spy
    private final FilenameFixer filenameFixer = new FilenameFixer();

    @InjectMocks
    private final RecipesBusinessLocal recipesBusiness = new RecipesBusinessBean();

    private List<RecipeEntity> recipeEntities;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        recipeEntities = Arrays.asList(createRecipeEntity("Croissant au jambon"), createRecipeEntity("Jambon au madere"));

        doReturn(recipeEntities)
                .when(recipesDao)
                .findRecipeWithFilter(any(QueryOperation.class), any(Optional.class), any(Optional.class));
        doAnswer(new Answer<RecipeEntity>() {
            @Override
            public RecipeEntity answer(final InvocationOnMock invocation) throws Throwable {
                final RecipeEntity param = (RecipeEntity) invocation.getArguments()[0];
                param.setId(0L);
                return param;
            }
        })
                .when(recipesDao)
                .persistRecipe(any(RecipeEntity.class));

        doReturn(createRecipeEntity("My Recipe")).when(recipesDao).findRecipeById(eq(KNOWN_RECIPE_ID));
        doReturn(Collections.singletonList(createTagEntity(KNOWN_TAG_NAME))).when(tagsDao).findAllTags();
        doAnswer(new Answer<TagEntity>() {
            @Override
            public TagEntity answer(final InvocationOnMock invocation) throws Throwable {
                final TagEntity param = (TagEntity) invocation.getArguments()[0];
                param.setId(0L);
                return param;
            }
        }).when(tagsDao).persistTag(any(TagEntity.class));
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
        final Optional<String> chunkStart = Optional.of("chunkStart");
        final Optional<Integer> size = Optional.of(50);

        final List<Recipe> recipes = recipesBusiness.findRecipesWithFilter(queryOperation, chunkStart, size);

        verify(recipesDao).findRecipeWithFilter(eq(queryOperation), eq(chunkStart), eq(size));
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
        doReturn(recipeMock).when(recipesDao).findRecipeById(anyInt());

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

        final Recipe recipe = recipesBusiness.setRecipeFilename(KNOWN_RECIPE_ID, filename);
        assertThat(recipe.getFilename()).isEqualTo(filename);
    }

    @Test
    public void testSetRecipeFilenameNotFound() {
        final Recipe persistedRecipe = recipesBusiness.setRecipeFilename(456L, "recipe.xml");
        assertThat(persistedRecipe).isNull();
    }
}