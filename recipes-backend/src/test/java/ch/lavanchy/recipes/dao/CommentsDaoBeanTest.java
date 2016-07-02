package ch.lavanchy.recipes.dao;

import ch.lavanchy.recipes.InjectEntityManager;
import ch.lavanchy.recipes.JpaTransactionRule;
import ch.lavanchy.recipes.entities.CommentEntity;
import ch.lavanchy.recipes.entities.RecipeEntity;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for {@link CommentsDaoBean}
 *
 * @since 2.0.0
 */
public class CommentsDaoBeanTest {
    private static final String COMMENT_TEXT = "my very goooooooood comment";

    @Rule
    public TestRule jpaTransactionRule = new JpaTransactionRule(this);

    @Inject
    private EntityManager entityManager;

    @InjectEntityManager
    private CommentsDaoLocal testee = new CommentsDaoBean();

    private final RecipeEntity recipeEntity = new RecipeEntity();

    @Before
    public void setUp() {
        entityManager.persist(recipeEntity);
        entityManager.flush();
    }

    @Test
    public void testFindCommentsEmptyDb() {
        final List<CommentEntity> comments = testee.findCommentsForRecipe(0L);

        assertThat(comments).isEmpty();
    }

    @Test
    public void testFindCommentsNotMatching() {
        entityManager.persist(createCommentEntity(COMMENT_TEXT));

        final List<CommentEntity> comments = testee.findCommentsForRecipe(recipeEntity.getId() + 1);

        assertThat(comments).isEmpty();
    }

    @Test
    public void testFindCommentsMultipleMatching() {
        entityManager.persist(createCommentEntity(COMMENT_TEXT));
        entityManager.persist(createCommentEntity(COMMENT_TEXT + " best of the world"));

        final List<CommentEntity> comments = testee.findCommentsForRecipe(recipeEntity.getId());

        assertThat(comments).hasSize(2);
    }

    private CommentEntity createCommentEntity(final String content) {
        final CommentEntity entity = new CommentEntity();
        entity.setContent(content);
        entity.setRecipe(recipeEntity);
        return entity;
    }
}