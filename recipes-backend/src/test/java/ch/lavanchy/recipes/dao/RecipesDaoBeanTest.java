package ch.lavanchy.recipes.dao;

import ch.lavanchy.recipes.InjectEntityManager;
import ch.lavanchy.recipes.JpaTransactionRule;
import ch.lavanchy.recipes.entities.RecipeEntity;
import ch.lavanchy.recipes.entities.TagEntity;
import ch.lavanchy.recipes.query.AndOp;
import ch.lavanchy.recipes.query.NotOp;
import ch.lavanchy.recipes.query.OrOp;
import ch.lavanchy.recipes.query.TextFilterOp;
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
    private FilterQueryFactory filterQueryFactory = new FilterQueryFactory();

    @Inject
    private EntityManager entityManager;

    @InjectMocks
    @InjectEntityManager
    private RecipesDaoLocal testee = new RecipesDaoBean();

    @Test
    public void testFindRecipeByQueryNoTags() {
        final String recipeName1 = "myRecipeFirst";
        testee.persistRecipe(createRecipeWithNameAndTags(recipeName1));
        final String recipeName2 = "myRecipeSecond";
        testee.persistRecipe(createRecipeWithNameAndTags(recipeName2));
        final String recipeName3 = "myRecipeThird";
        testee.persistRecipe(createRecipeWithNameAndTags(recipeName3));

        TextFilterOp filterText1 = new TextFilterOp(recipeName1);
        TextFilterOp filterText2 = new TextFilterOp(recipeName2);
        TextFilterOp filterText3 = new TextFilterOp(recipeName3);
        assertThat(testee.findRecipeWithFilter(filterText1)).hasSize(1);
        assertThat(testee.findRecipeWithFilter(filterText2)).hasSize(1);
        assertThat(testee.findRecipeWithFilter(filterText3)).hasSize(1);
        assertThat(testee.findRecipeWithFilter(new TextFilterOp("myRecipe"))).hasSize(3);
        assertThat(testee.findRecipeWithFilter(new TextFilterOp("bla"))).isEmpty();
        assertThat(testee.findRecipeWithFilter(new NotOp(filterText1))).hasSize(2);
        assertThat(testee.findRecipeWithFilter(new AndOp(filterText1, filterText2))).isEmpty();
        assertThat(testee.findRecipeWithFilter(new OrOp(filterText1, filterText2))).hasSize(2);
    }

    @Test
    public void testFindRecipeByQueryWithTags() {
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

        TextFilterOp filterTagText1 = new TextFilterOp(tagName1);
        TextFilterOp filterTagText2 = new TextFilterOp(tagName2);
        TextFilterOp filterRecipeText1 = new TextFilterOp(recipeName1);
        TextFilterOp filterRecipeText2 = new TextFilterOp(recipeName2);
        TextFilterOp filterRecipeText3 = new TextFilterOp(recipeName3);
        assertThat(testee.findRecipeWithFilter(filterTagText1)).hasSize(2);
        assertThat(testee.findRecipeWithFilter(filterTagText2)).hasSize(2);
        assertThat(testee.findRecipeWithFilter(filterRecipeText1)).hasSize(1);
        assertThat(testee.findRecipeWithFilter(filterRecipeText2)).hasSize(1);
        assertThat(testee.findRecipeWithFilter(filterRecipeText3)).hasSize(1);
        assertThat(testee.findRecipeWithFilter(new TextFilterOp("tag"))).hasSize(3);
        assertThat(testee.findRecipeWithFilter(new TextFilterOp("bla"))).isEmpty();
        assertThat(testee.findRecipeWithFilter(new NotOp(filterTagText1))).hasSize(1);
        assertThat(testee.findRecipeWithFilter(new NotOp(filterRecipeText1))).hasSize(2);
        assertThat(testee.findRecipeWithFilter(new AndOp(filterTagText1, filterTagText2))).hasSize(1);
        assertThat(testee.findRecipeWithFilter(new AndOp(filterTagText1, filterRecipeText3))).hasSize(1);
        assertThat(testee.findRecipeWithFilter(new OrOp(filterTagText1, filterTagText2))).hasSize(3);
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