package ch.lavanchy.recipes.query;

import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * Query AND operation
 *
 * @since 1.0.2
 */
public class NotOp implements QueryOperation {
    private final QueryOperation operation;

    public NotOp(final QueryOperation operation) {
        this.operation = operation;
    }

    public QueryOperation getOperation() {
        return operation;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof NotOp)) {
            return false;
        } else if (obj == this) {
            return true;
        } else {
            final NotOp that = (NotOp) obj;
            return new EqualsBuilder()
                    .append(operation, that.operation)
                    .isEquals();
        }
    }

    @Override
    public String toString() {
        return String.format("[NOT %s]", operation);
    }
}
