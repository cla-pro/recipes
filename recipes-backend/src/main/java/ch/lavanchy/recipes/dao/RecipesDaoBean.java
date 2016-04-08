package ch.lavanchy.recipes.dao;

import ch.lavanchy.recipes.entities.QRecipeEntity;
import ch.lavanchy.recipes.entities.RecipeEntity;
import ch.lavanchy.recipes.query.QueryOperation;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

/**
 * Implementation of {@see RecipesDaoLocal}
 *
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
    public List<RecipeEntity> findAllRecipes() {
        return new JPAQueryFactory(entityManager)
                .selectFrom(qRecipeEntity)
                .orderBy(qRecipeEntity.name.asc())
                .createQuery()
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RecipeEntity> findRecipeWithFilter(final QueryOperation queryOperation) {
        return new JPAQueryFactory(entityManager)
                .selectFrom(qRecipeEntity)
                .where(filterQueryFactory.generateWhereExpression(queryOperation))
                .orderBy(qRecipeEntity.name.asc())
                .createQuery()
                .getResultList();
    }

    @Override
    public RecipeEntity findRecipeById(long id) {
        return entityManager.find(RecipeEntity.class, id);
    }

    @Override
    public RecipeEntity persistRecipe(RecipeEntity recipeEntity) {
        entityManager.persist(recipeEntity);
        return recipeEntity;
    }
}
