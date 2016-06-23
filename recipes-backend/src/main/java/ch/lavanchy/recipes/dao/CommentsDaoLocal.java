package ch.lavanchy.recipes.dao;

import ch.lavanchy.recipes.entities.CommentEntity;

import java.util.List;

/**
 *@since 2.0.0
 */
public interface CommentsDaoLocal {
    List<CommentEntity> findCommentsForRecipe(final long recipeId);

    CommentEntity findById(final long id);

    CommentEntity persist(final CommentEntity toPersist);

    void delete(final CommentEntity toDelete);
}
