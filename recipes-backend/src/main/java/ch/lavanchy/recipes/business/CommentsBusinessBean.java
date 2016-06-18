package ch.lavanchy.recipes.business;

import ch.lavanchy.recipes.dao.CommentsDaoLocal;
import ch.lavanchy.recipes.dao.RecipesDaoLocal;
import ch.lavanchy.recipes.data.Comment;
import ch.lavanchy.recipes.entities.CommentEntity;
import ch.lavanchy.recipes.entities.RecipeEntity;
import ch.lavanchy.recipes.factories.CommentFactory;

import javax.inject.Inject;
import java.util.List;

/**
 * Implementation of {@see CommentsBusinessLocal}
 *
 * @since 2.0.0
 */
public class CommentsBusinessBean implements CommentsBusinessLocal {
    @Inject
    private CommentsDaoLocal commentsDao;

    @Inject
    private RecipesDaoLocal recipesDao;

    @Inject
    private CommentFactory commentFactory;

    @Override
    public List<Comment> findCommentsForRecipe(final long recipeId) {
        final List<CommentEntity> found = commentsDao.findCommentsForRecipe(recipeId);
        return commentFactory.createCommentsFromEntities(found);
    }

    @Override
    public Comment createComment(final Comment base) {
        final RecipeEntity recipe = recipesDao.findRecipeById(base.getRecipeId());
        final CommentEntity commentEntity = commentFactory.createCommentEntity(base);
        commentEntity.setRecipe(recipe);
        final CommentEntity persisted = commentsDao.persist(commentEntity);
        return commentFactory.createCommentFromEntity(persisted);
    }
}
