package ch.lavanchy.recipes.data;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Contains the elements of a tag
 * <p/>
 * <ul>
 * <li>The id</li>
 * <li>The name</li>
 * <li>The change/creation date</li>
 * </ul>
 *
 * @since 1.0.0
 */
public class Tag {
    private Long id;
    private String name;
    private long modificationDate;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getModificationDate() {
        return modificationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Tag tag = (Tag) o;
        return new EqualsBuilder()
                .append(id, tag.id)
                .append(name, tag.name)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .toHashCode();
    }

    public static class TagBuilder {
        private final Tag tag = new Tag();

        public TagBuilder withId(final Long id) {
            tag.id = id;
            return this;
        }

        public TagBuilder withName(final String name) {
            tag.name = name;
            return this;
        }

        public TagBuilder withModificationDate(final long modificationDate) {
            tag.modificationDate = modificationDate;
            return this;
        }

        public Tag build() {
            return tag;
        }
    }
}
