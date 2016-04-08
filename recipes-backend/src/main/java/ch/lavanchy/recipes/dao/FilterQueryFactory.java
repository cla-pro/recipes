package ch.lavanchy.recipes.dao;

import ch.lavanchy.recipes.entities.QRecipeEntity;
import ch.lavanchy.recipes.entities.QTagEntity;
import ch.lavanchy.recipes.query.AndOp;
import ch.lavanchy.recipes.query.NotOp;
import ch.lavanchy.recipes.query.OrOp;
import ch.lavanchy.recipes.query.QueryOperation;
import ch.lavanchy.recipes.query.TextFilterOp;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;

/**
 * Create the QueryDSL from the string query (received as param).
 *
 * @since 1.0.2
 */
class FilterQueryFactory {
    private final QRecipeEntity qRecipeEntity = QRecipeEntity.recipeEntity;
    private final QTagEntity qTagEntity = QTagEntity.tagEntity;

    Predicate generateWhereExpression(final QueryOperation queryOperation) {
        if (queryOperation instanceof AndOp) {
            return generateAnd((AndOp) queryOperation);
        } else if (queryOperation instanceof OrOp) {
            return generateOr((OrOp) queryOperation);
        } else if (queryOperation instanceof NotOp) {
            return generateNot((NotOp) queryOperation);
        } else if (queryOperation instanceof TextFilterOp) {
            return generateTextFilter((TextFilterOp) queryOperation);
        } else {
            return new BooleanBuilder();
        }
    }

    private Predicate generateAnd(final AndOp andOp) {
        final Predicate left = generateWhereExpression(andOp.getLeft());
        final Predicate right = generateWhereExpression(andOp.getRight());

        return new BooleanBuilder(left).and(right);
    }

    private Predicate generateOr(final OrOp orOp) {
        final Predicate left = generateWhereExpression(orOp.getLeft());
        final Predicate right = generateWhereExpression(orOp.getRight());

        return new BooleanBuilder(left).or(right);
    }

    private Predicate generateNot(final NotOp notOp) {
        return generateWhereExpression(notOp.getOperation()).not();
    }

    private Predicate generateTextFilter(final TextFilterOp textFilterOp) {
        final BooleanExpression recipeFilter = qRecipeEntity.name.containsIgnoreCase(textFilterOp.getFilter());

        return new BooleanBuilder(qRecipeEntity.tags.isEmpty().and(recipeFilter))
                .or(qRecipeEntity.tags.isNotEmpty().and(recipeFilter.or(createTagFilter(textFilterOp))));
    }

    private Predicate createTagFilter(final TextFilterOp textFilterOp) {
        return qRecipeEntity.tags.any().in(JPAExpressions.selectFrom(qTagEntity).where(qTagEntity.name.containsIgnoreCase(textFilterOp.getFilter())));
    }
}
