package ch.lavanchy.recipes.dao;

import ch.lavanchy.recipes.entities.QRecipeEntity;
import ch.lavanchy.recipes.query.AndOp;
import ch.lavanchy.recipes.query.NotOp;
import ch.lavanchy.recipes.query.OrOp;
import ch.lavanchy.recipes.query.QueryOperation;
import ch.lavanchy.recipes.query.TextFilterOp;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;

/**
 * Create the QueryDSL from the string query (received as param).
 *
 * @since 1.0.2
 */
class FilterQueryFactory {
    private final QRecipeEntity qRecipeEntity = QRecipeEntity.recipeEntity;

    Predicate generateWhereExpression(final QueryOperation queryOperation) {
        if (queryOperation instanceof AndOp) {
            return generateAnd((AndOp) queryOperation);
        } else if (queryOperation instanceof OrOp) {
            return generateOr((OrOp) queryOperation);
        } else if (queryOperation instanceof NotOp) {
            return generateNot((NotOp) queryOperation);
        } else if (queryOperation instanceof TextFilterOp) {
            return generateFilter((TextFilterOp) queryOperation);
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

    private Predicate generateFilter(final TextFilterOp textFilterOp) {
        return qRecipeEntity.filename.contains(textFilterOp.getFilter());
    }
}
