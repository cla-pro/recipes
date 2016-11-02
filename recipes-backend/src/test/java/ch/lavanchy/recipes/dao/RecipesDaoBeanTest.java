package ch.lavanchy.recipes.dao;

import ch.lavanchy.recipes.InjectEntityManager;
import ch.lavanchy.recipes.JpaTransactionRule;
import ch.lavanchy.recipes.entities.RecipeEntity;
import ch.lavanchy.recipes.entities.TagEntity;
import ch.lavanchy.recipes.query.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for {@link RecipesDaoBean}
 *
 * @since 1.0.2
 */
@RunWith(MockitoJUnitRunner.class)
public class RecipesDaoBeanTest {
    @Rule
    public TestRule jpaTransactionRule = new JpaTransactionRule(this);

    @Spy
    private final FilterQueryFactory filterQueryFactory = new FilterQueryFactory();

    @Inject
    private EntityManager entityManager;

    @InjectMocks
    @InjectEntityManager
    private final RecipesDaoLocal testee = new RecipesDaoBean();

    @Test
    public void testFindRecipeWithFilterNoTags() {
        final String recipeName1 = "myRecipeFirst";
        testee.persistRecipe(createRecipeWithNameAndTags(recipeName1));
        final String recipeName2 = "myRecipeSecond";
        testee.persistRecipe(createRecipeWithNameAndTags(recipeName2));
        final String recipeName3 = "myRecipeThird";
        testee.persistRecipe(createRecipeWithNameAndTags(recipeName3));

        final TextFilterOp filterText1 = new TextFilterOp(recipeName1);
        final TextFilterOp filterText2 = new TextFilterOp(recipeName2);
        final TextFilterOp filterText3 = new TextFilterOp(recipeName3);
        assertThat(testee.findRecipeWithFilter(filterText1, Optional.empty(), Optional.empty())).hasSize(1);
        assertThat(testee.findRecipeWithFilter(filterText2, Optional.empty(), Optional.empty())).hasSize(1);
        assertThat(testee.findRecipeWithFilter(filterText3, Optional.empty(), Optional.empty())).hasSize(1);
        assertThat(testee.findRecipeWithFilter(new TextFilterOp("myRecipe"), Optional.empty(), Optional.empty())).hasSize(3);
        assertThat(testee.findRecipeWithFilter(new TextFilterOp("bla"), Optional.empty(), Optional.empty())).isEmpty();
        assertThat(testee.findRecipeWithFilter(new NotOp(filterText1), Optional.empty(), Optional.empty())).hasSize(2);
        assertThat(testee.findRecipeWithFilter(new AndOp(filterText1, filterText2), Optional.empty(), Optional.empty())).isEmpty();
        assertThat(testee.findRecipeWithFilter(new OrOp(filterText1, filterText2), Optional.empty(), Optional.empty())).hasSize(2);
    }

    @Test
    public void testFindRecipeWithFilterWithTags() {
        final String tagName1 = "tag1";
        final TagEntity tagEntity1 = createAndPersistTagWithName(tagName1);
        final String tagName2 = "tag2";
        final TagEntity tagEntity2 = createAndPersistTagWithName(tagName2);

        final String recipeName1 = "myRecipeFirst";
        testee.persistRecipe(createRecipeWithNameAndTags(recipeName1, tagEntity1));
        final String recipeName2 = "myRecipeSecond";
        testee.persistRecipe(createRecipeWithNameAndTags(recipeName2, tagEntity2));
        final String recipeName3 = "myRecipeThird";
        testee.persistRecipe(createRecipeWithNameAndTags(recipeName3, tagEntity1, tagEntity2));

        final TextFilterOp filterTagText1 = new TextFilterOp(tagName1);
        final TextFilterOp filterTagText2 = new TextFilterOp(tagName2);
        final TextFilterOp filterRecipeText1 = new TextFilterOp(recipeName1);
        final TextFilterOp filterRecipeText2 = new TextFilterOp(recipeName2);
        final TextFilterOp filterRecipeText3 = new TextFilterOp(recipeName3);
        assertThat(testee.findRecipeWithFilter(filterTagText1, Optional.empty(), Optional.empty())).hasSize(2);
        assertThat(testee.findRecipeWithFilter(filterTagText2, Optional.empty(), Optional.empty())).hasSize(2);
        assertThat(testee.findRecipeWithFilter(filterRecipeText1, Optional.empty(), Optional.empty())).hasSize(1);
        assertThat(testee.findRecipeWithFilter(filterRecipeText2, Optional.empty(), Optional.empty())).hasSize(1);
        assertThat(testee.findRecipeWithFilter(filterRecipeText3, Optional.empty(), Optional.empty())).hasSize(1);
        assertThat(testee.findRecipeWithFilter(new TextFilterOp("tag"), Optional.empty(), Optional.empty())).hasSize(3);
        assertThat(testee.findRecipeWithFilter(new TextFilterOp("bla"), Optional.empty(), Optional.empty())).isEmpty();
        assertThat(testee.findRecipeWithFilter(new NotOp(filterTagText1), Optional.empty(), Optional.empty())).hasSize(1);
        assertThat(testee.findRecipeWithFilter(new NotOp(filterRecipeText1), Optional.empty(), Optional.empty())).hasSize(2);
        assertThat(testee.findRecipeWithFilter(new AndOp(filterTagText1, filterTagText2), Optional.empty(), Optional.empty())).hasSize(1);
        assertThat(testee.findRecipeWithFilter(new AndOp(filterTagText1, filterRecipeText3), Optional.empty(), Optional.empty())).hasSize(1);
        assertThat(testee.findRecipeWithFilter(new OrOp(filterTagText1, filterTagText2), Optional.empty(), Optional.empty())).hasSize(3);
    }

    @Test
    public void testFindRecipeWithFilterChunk() {
        final String recipeName1 = "myRecipeFirst";
        testee.persistRecipe(createRecipeWithNameAndTags(recipeName1));
        final String recipeName2 = "myRecipeSecond";
        testee.persistRecipe(createRecipeWithNameAndTags(recipeName2));
        final String recipeName3 = "myRecipeThird";
        testee.persistRecipe(createRecipeWithNameAndTags(recipeName3));

        assertThat(testee.findRecipeWithFilter(new EmptyOp(), Optional.of(recipeName2), Optional.empty()).get(0).getName())
                .isEqualTo(recipeName3);
    }

    @Test
    public void testFindRecipeWithSize() {
        final String recipeName1 = "myRecipeFirst";
        testee.persistRecipe(createRecipeWithNameAndTags(recipeName1));
        final String recipeName2 = "myRecipeSecond";
        testee.persistRecipe(createRecipeWithNameAndTags(recipeName2));
        final String recipeName3 = "myRecipeThird";
        testee.persistRecipe(createRecipeWithNameAndTags(recipeName3));

        final List<RecipeEntity> recipes = testee.findRecipeWithFilter(new EmptyOp(), Optional.empty(), Optional.of(1));
        assertThat(recipes).hasSize(1);
        assertThat(recipes.get(0).getName()).isEqualTo(recipeName1);
    }

    @Test
    public void testFindRecipeById() {
        final String recipeName = "myRecipe";
        final RecipeEntity recipeEntity = createRecipeWithNameAndTags(recipeName);
        final long id = testee.persistRecipe(recipeEntity).getId();

        assertThat(testee.findRecipeById(id).getName()).isEqualTo(recipeName);
        assertThat(testee.findRecipeById(id + 1)).isNull();
    }

    @Test
    public void testPersistRecipe() {
        final RecipeEntity recipeEntity = createRecipeWithNameAndTags("myRecipe");

        assertThat(recipeEntity.getId()).isNull();
        final RecipeEntity persistedEntity = testee.persistRecipe(recipeEntity);

        assertThat(persistedEntity).isNotNull();
        assertThat(persistedEntity.getId()).isNotNull();
    }

    private RecipeEntity createRecipeWithNameAndTags(final String recipeName, final TagEntity... tags) {
        final RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setName(recipeName);
        recipeEntity.getTags().addAll(Arrays.asList(tags));
        return recipeEntity;
    }

    private TagEntity createAndPersistTagWithName(final String tagName) {
        final TagEntity tagEntity = new TagEntity();
        tagEntity.setName(tagName);
        return tagEntity;
    }
}