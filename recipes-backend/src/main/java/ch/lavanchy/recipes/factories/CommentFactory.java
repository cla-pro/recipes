package ch.lavanchy.recipes.factories;

import ch.lavanchy.recipes.data.Comment;
import ch.lavanchy.recipes.entities.CommentEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @since 2.0.0
 */
public class CommentFactory {
    public List<Comment> createCommentsFromEntities(final List<CommentEntity> toConvert) {
        return toConvert
                .stream()
                .map(entity -> createCommentFromEntity(entity))
                .collect(Collectors.toList());
    }

    public Comment createCommentFromEntity(final CommentEntity toConvert) {
        return Comment
                .builder()
                .withId(toConvert.getId())
                .withContent(toConvert.getContent())
                .withLastModification(toConvert.getLastModification())
                .withRecipeId(toConvert.getRecipe().getId())
                .build();
    }

    public CommentEntity createCommentEntity(final Comment comment) {
        final CommentEntity commentEntity = new CommentEntity();
        commentEntity.setContent(comment.getContent());
        commentEntity.setLastModification(LocalDateTime.now());
        return commentEntity;
    }
}
