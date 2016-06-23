package ch.lavanchy.recipes.business;

import ch.lavanchy.recipes.data.Comment;

import java.util.List;

/**
 * @since 2.0.0
 */
public interface CommentsBusinessLocal {
    List<Comment> findCommentsForRecipe(final long recipeId);

    Comment createComment(final Comment base);

    Comment updateComment(final Comment base);

    void deleteComment(long id);
}
