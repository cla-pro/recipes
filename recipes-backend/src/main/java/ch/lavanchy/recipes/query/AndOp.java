package ch.lavanchy.recipes.query;

import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * Query AND operation
 *
 * @since 1.0.2
 */
public class AndOp implements QueryOperation {
    private final QueryOperation left;
    private final QueryOperation right;

    public AndOp(final QueryOperation left, final QueryOperation right) {
        this.left = left;
        this.right = right;
    }

    public QueryOperation getLeft() {
        return left;
    }

    public QueryOperation getRight() {
        return right;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof AndOp)) {
            return false;
        } else if (obj == this) {
            return true;
        } else {
            final AndOp that = (AndOp) obj;
            return new EqualsBuilder()
                    .append(left, that.left)
                    .append(right, that.right)
                    .isEquals();
        }
    }

    @Override
    public String toString() {
        return String.format("[%s AND %s]", left, right);
    }
}
