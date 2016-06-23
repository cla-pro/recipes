package ch.lavanchy.recipes.dao;

import ch.lavanchy.recipes.entities.CommentEntity;
import ch.lavanchy.recipes.entities.QCommentEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

/**
 * @since 2.0.0
 */
public class CommentsDaoBean implements CommentsDaoLocal {
    private final QCommentEntity qCommentEntity = QCommentEntity.commentEntity;

    @Inject
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    @Override
    public List<CommentEntity> findCommentsForRecipe(final long recipeId) {
        return new JPAQueryFactory(entityManager)
                .selectFrom(qCommentEntity)
                .where(qCommentEntity.recipe().id.eq(recipeId))
                .orderBy(qCommentEntity.lastModification.desc())
                .createQuery()
                .getResultList();
    }

    @Override
    public CommentEntity findById(final long id) {
        return entityManager.find(CommentEntity.class, id);
    }

    @Override
    public CommentEntity persist(final CommentEntity toPersist) {
        entityManager.persist(toPersist);
        return toPersist;
    }

    @Override
    public void delete(final CommentEntity toDelete) {
        entityManager.remove(toDelete);
    }
}
