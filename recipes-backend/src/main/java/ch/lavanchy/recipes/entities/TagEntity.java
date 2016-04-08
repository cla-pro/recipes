package ch.lavanchy.recipes.entities;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Data object which represents the table TAG from the db
 *
 * @since 1.0.0
 */
@Entity(name = "tag")
public class TagEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag")
    private String name;

    @Column(name = "modification_date")
    private long modificationDate;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(long modificationDate) {
        this.modificationDate = modificationDate;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(id)
                .toHashCode();
    }

    @Override
    public boolean equals(final Object that) {
        if (that == null || !(that instanceof  TagEntity)) {
            return false;
        }

        final TagEntity thatTag = (TagEntity) that;
        return new EqualsBuilder()
                .append(id, thatTag.id)
                .isEquals();
    }
}
