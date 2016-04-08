package ch.lavanchy.recipes.query;

import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * Query Empty operation
 *
 * @since 1.0.2
 */
public class EmptyOp implements QueryOperation {
    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean equals(final Object obj) {
        return obj != null && obj instanceof EmptyOp;
    }

    @Override
    public String toString() {
        return String.format("[Empty]");
    }
}
