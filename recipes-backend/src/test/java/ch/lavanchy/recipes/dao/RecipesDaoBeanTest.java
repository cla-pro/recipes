package ch.lavanchy.recipes.dao;

import ch.lavanchy.recipes.InjectEntityManager;
import ch.lavanchy.recipes.JpaTransactionRule;
import ch.lavanchy.recipes.entities.RecipeEntity;
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

import java.util.List;

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
    public void testFindRecipeByQuery() {
        final String recipeName1 = "myRecipeFirst";
        testee.persistRecipe(createRecipeWithName(recipeName1));
        final String recipeName2 = "myRecipeSecond";
        testee.persistRecipe(createRecipeWithName(recipeName2));
        final String recipeName3 = "myRecipeThird";
        testee.persistRecipe(createRecipeWithName(recipeName3));

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
    public void testFindRecipeById() {
        final String recipeName = "myRecipe";
        final RecipeEntity recipeEntity = createRecipeWithName(recipeName);
        final long id = testee.persistRecipe(recipeEntity).getId();

        assertThat(testee.findRecipeById(id).getName()).isEqualTo(recipeName);
        assertThat(testee.findRecipeById(id + 1)).isNull();
    }

    @Test
    public void testPersistRecipe() {
        final RecipeEntity recipeEntity = createRecipeWithName("myRecipe");

        assertThat(recipeEntity.getId()).isNull();
        final RecipeEntity persistedEntity = testee.persistRecipe(recipeEntity);

        assertThat(persistedEntity).isNotNull();
        assertThat(persistedEntity.getId()).isNotNull();
    }

    private RecipeEntity createRecipeWithName(final String recipeName) {
        final RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setName(recipeName);
        return recipeEntity;
    }
}