package ch.lavanchy.recipes.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Data object which represents the table COMMENT from the db
 *
 * @since 2.0.0
 */
@Entity(name = "comment")
public class CommentEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recipe_id", referencedColumnName = "id")
    private RecipeEntity recipe;

    @Column(name = "last_modification")
    private Date lastModification;

    @Column(name = "content")
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RecipeEntity getRecipe() {
        return recipe;
    }

    public void setRecipe(final RecipeEntity recipe) {
        this.recipe = recipe;
    }

    public LocalDateTime getLastModification() {
        return lastModification.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public void setLastModification(final LocalDateTime lastModification) {
        this.lastModification = Date.from(lastModification.atZone(ZoneId.systemDefault()).toInstant());
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }
}
