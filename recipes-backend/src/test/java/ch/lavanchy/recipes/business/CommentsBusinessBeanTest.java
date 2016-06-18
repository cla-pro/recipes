package ch.lavanchy.recipes.business;

import ch.lavanchy.recipes.dao.CommentsDaoLocal;
import ch.lavanchy.recipes.dao.RecipesDaoLocal;
import ch.lavanchy.recipes.data.Comment;
import ch.lavanchy.recipes.entities.CommentEntity;
import ch.lavanchy.recipes.entities.RecipeEntity;
import ch.lavanchy.recipes.factories.CommentFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CommentsBusinessBeanTest {
    @Mock
    private CommentsDaoLocal commentsDao;

    @Mock
    private RecipesDaoLocal recipesDao;

    @Spy
    private CommentFactory commentFactory = new CommentFactory();

    @InjectMocks
    private CommentsBusinessLocal testee = new CommentsBusinessBean();

    @Test
    public void testFindCommentsForRecipe() throws Exception {
        final List<CommentEntity> comments = Arrays.asList(
                mockCommentEntity(mockRecipeEntity(1L)),
                mockCommentEntity(mockRecipeEntity(2L)));
        doReturn(comments).when(commentsDao).findCommentsForRecipe(anyLong());

        final List<Comment> result = testee.findCommentsForRecipe(3L);

        assertThat(result).hasSameSizeAs(comments);
    }

    @Test
    public void testCreateComment() {
        final long recipeId = 12345L;
        final RecipeEntity recipeEntity = mock(RecipeEntity.class);
        doReturn(recipeId).when(recipeEntity).getId();
        doReturn(recipeEntity).when(recipesDao).findRecipeById(anyLong());

        doReturn(mockCommentEntity(recipeEntity)).when(commentsDao).persist(any(CommentEntity.class));

        final Comment base = Comment.builder().withRecipeId(12345L).withContent("bla bla").build();

        final Comment result = testee.createComment(base);

        assertThat(result.getRecipeId()).isEqualTo(recipeId);
        verify(commentsDao).persist(any(CommentEntity.class));
    }

    private CommentEntity mockCommentEntity(final RecipeEntity recipe) {
        final CommentEntity entity = new CommentEntity();
        entity.setId(1L);
        entity.setRecipe(recipe);
        entity.setLastModification(LocalDateTime.now());
        return entity;
    }

    private RecipeEntity mockRecipeEntity(final long id) {
        final RecipeEntity mock = mock(RecipeEntity.class);
        doReturn(id).when(mock).getId();
        return mock;
    }
}