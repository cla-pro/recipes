package ch.lavanchy.recipes.query;

import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * Query text filter operation
 *
 * @since 1.0.2
 */
public class TextFilterOp implements QueryOperation {
    private final String filter;

    public TextFilterOp(final String filter) {
        this.filter = filter;
    }

    public String getFilter() {
        return filter;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null || !(obj instanceof TextFilterOp)) {
            return false;
        } else if (obj == this) {
            return true;
        } else {
            final TextFilterOp that = (TextFilterOp) obj;
            return new EqualsBuilder()
                    .append(filter, that.filter)
                    .isEquals();
        }
    }

    @Override
    public String toString() {
        return String.format("[TEXT %s]", filter);
    }
}
