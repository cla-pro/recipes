package ch.lavanchy.recipes.dao;

import ch.lavanchy.recipes.entities.QRecipeEntity;
import ch.lavanchy.recipes.entities.RecipeEntity;
import ch.lavanchy.recipes.query.QueryOperation;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/**
 * @since 1.0.0
 */
public class RecipesDaoBean implements RecipesDaoLocal {
    private final QRecipeEntity qRecipeEntity = QRecipeEntity.recipeEntity;

    @Inject
    private EntityManager entityManager;

    @Inject
    private FilterQueryFactory filterQueryFactory;

    @SuppressWarnings("unchecked")
    @Override
    public List<RecipeEntity> findRecipeWithFilter(
            final QueryOperation queryOperation,
            final Optional<String> chunkStart,
            final Optional<Integer> size) {
        return createQueryDSL(queryOperation, chunkStart, size)
                .createQuery()
                .getResultList();
    }

    private JPAQuery<RecipeEntity> createQueryDSL(
            final QueryOperation queryOperation,
            final Optional<String> chunkStart,
            final Optional<Integer> chunkSize) {
        return applyChunkSize(
                new JPAQueryFactory(entityManager)
                        .selectFrom(qRecipeEntity)
                        .where(generateWhereClause(queryOperation, chunkStart))
                        .orderBy(qRecipeEntity.name.asc()),
                chunkSize);
    }

    private JPAQuery<RecipeEntity> applyChunkSize(final JPAQuery<RecipeEntity> query, final Optional<Integer> chunkSize) {
        if (chunkSize.isPresent()) {
            return query.limit(chunkSize.get());
        } else {
            return query;
        }
    }

    private Predicate generateWhereClause(final QueryOperation queryOperation, final Optional<String> chunkStart) {
        final Predicate where = filterQueryFactory.generateWhereExpression(queryOperation);
        if (chunkStart.isPresent()) {
            return new BooleanBuilder(where).and(qRecipeEntity.name.gt(chunkStart.get()));
        } else {
            return where;
        }
    }

    @Override
    public RecipeEntity findRecipeById(final long id) {
        return entityManager.find(RecipeEntity.class, id);
    }

    @Override
    public RecipeEntity persistRecipe(final RecipeEntity recipeEntity) {
        entityManager.persist(recipeEntity);
        return recipeEntity;
    }
}
