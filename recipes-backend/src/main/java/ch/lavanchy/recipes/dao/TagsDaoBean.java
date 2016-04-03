package ch.lavanchy.recipes.dao;

import ch.lavanchy.recipes.entities.TagEntity;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 * Implementation of {@see TagsDaoLocal}
 *
 * @since 1.0.0
 */
public class TagsDaoBean implements TagsDaoLocal {
    @Inject
    private EntityManager entityManager;

    @Override
    public List<TagEntity> findAllTags() {
        final CriteriaQuery<TagEntity> criteriaQuery = entityManager.getCriteriaBuilder().createQuery(TagEntity.class);
        criteriaQuery.select(criteriaQuery.from(TagEntity.class));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public TagEntity persistTag(final TagEntity tagEntity) {
        entityManager.persist(tagEntity);
        return tagEntity;
    }
}
