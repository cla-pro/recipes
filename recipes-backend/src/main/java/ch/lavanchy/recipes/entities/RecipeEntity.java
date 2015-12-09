package ch.lavanchy.recipes.entities;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * Data object which represents the table RECIPE from the db
 *
 * @since 1.0.0
 */
@Table(name = "RECIPE")
public class RecipeEntity {
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "FILENAME")
    private String filename;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
