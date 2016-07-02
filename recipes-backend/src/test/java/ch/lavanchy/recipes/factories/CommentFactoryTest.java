package ch.lavanchy.recipes.factories;

import ch.lavanchy.recipes.data.Comment;
import ch.lavanchy.recipes.entities.CommentEntity;
import ch.lavanchy.recipes.entities.RecipeEntity;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class CommentFactoryTest {
    @Test
    public void testConvertEmptyCommentEntityList() {
        final List<Comment> result = new CommentFactory().createCommentsFromEntities(Collections.emptyList());

        assertThat(result).isEmpty();
    }

    @Test
    public void testConvertCommentEntities() {
        final List<CommentEntity> toConvert = Arrays.asList(createCommentEntity("content1"), createCommentEntity("content2"));
        final List<Comment> result = new CommentFactory().createCommentsFromEntities(toConvert);

        assertThat(result).hasSameSizeAs(toConvert);
    }

    @Test
    public void testConvertCommentEntity() {
        final CommentEntity commentEntity = createCommentEntity("the best comment of the wooooooorld");

        final Comment result = new CommentFactory().createCommentFromEntity(commentEntity);

        assertThat(result.getId()).isEqualTo(commentEntity.getId());
        assertThat(result.getLastModification()).isEqualTo(commentEntity.getLastModification());
        assertThat(result.getContent()).isEqualTo(commentEntity.getContent());
        assertThat(result.getRecipeId()).isEqualTo(commentEntity.getRecipe().getId());
    }

    @Test
    public void testCreateCommentEntity() {
        final Comment input = Comment.builder().withContent("bla bla bla").build();

        final CommentEntity created = new CommentFactory().createCommentEntity(input);

        assertThat(created.getContent()).isEqualTo(input.getContent());
        assertThat(created.getLastModification()).isNotNull();
    }

    private CommentEntity createCommentEntity(String content) {
        final CommentEntity entity = new CommentEntity();
        entity.setId(3L);
        entity.setLastModification(LocalDateTime.now());
        entity.setContent(content);

        final RecipeEntity recipeMock = mock(RecipeEntity.class);
        doReturn(12345L).when(recipeMock).getId();
        entity.setRecipe(recipeMock);
        return entity;
    }
}