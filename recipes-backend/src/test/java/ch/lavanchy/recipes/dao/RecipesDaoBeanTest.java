package ch.lavanchy.recipes.dao;

import ch.lavanchy.recipes.InjectEntityManager;
import ch.lavanchy.recipes.JpaTransactionRule;
import ch.lavanchy.recipes.entities.RecipeEntity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for {@link RecipesDaoBean}
 *
 * @since 1.0.2
 */
public class RecipesDaoBeanTest {
    @Rule
    public TestRule jpaTransactionRule = new JpaTransactionRule(this);

    @Inject
    private EntityManager entityManager;

    @InjectEntityManager
    private RecipesDaoLocal testee = new RecipesDaoBean();

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