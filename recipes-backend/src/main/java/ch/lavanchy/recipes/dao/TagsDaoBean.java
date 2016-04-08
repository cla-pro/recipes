package ch.lavanchy.recipes.dao;

import ch.lavanchy.recipes.entities.QTagEntity;
import ch.lavanchy.recipes.entities.TagEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

/**
 * Implementation of {@see TagsDaoLocal}
 *
 * @since 1.0.0
 */
public class TagsDaoBean implements TagsDaoLocal {
    private final QTagEntity qTagEntity = QTagEntity.tagEntity;

    @Inject
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    @Override
    public List<TagEntity> findAllTags() {
        return new JPAQueryFactory(entityManager)
                .selectFrom(qTagEntity)
                .createQuery()
                .getResultList();
    }

    @Override
    public TagEntity persistTag(final TagEntity tagEntity) {
        entityManager.persist(tagEntity);
        return tagEntity;
    }
}
