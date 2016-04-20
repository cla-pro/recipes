package ch.lavanchy.recipes.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Data object which represents the table RECIPE from the db
 *
 * @since 1.0.0
 */
@Entity(name = "recipe")
public class RecipeEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "filename")
    private String filename;

    @Column(name = "rating")
    private float rating;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(name="recipe_tag",
            joinColumns={@JoinColumn(name="recipe_id")},
            inverseJoinColumns={@JoinColumn(name="tag_id")})
    private Set<TagEntity> tags = new HashSet<>();

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

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Set<TagEntity> getTags() {
        return tags;
    }
}
