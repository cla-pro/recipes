package ch.lavanchy.recipes.data;

import java.time.LocalDateTime;

/**
 * @since 2.0.0
 */
public class Comment {
    private long id;
    private LocalDateTime lastModification;
    private String content;
    private long recipeId;

    private Comment() {}

    public long getId() {
        return id;
    }

    public LocalDateTime getLastModification() {
        return lastModification;
    }

    public String getContent() {
        return content;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public static CommentBuilder builder() {
        return new CommentBuilder();
    }

    public static class CommentBuilder {
        private Comment comment = new Comment();

        private CommentBuilder() {}

        public CommentBuilder withId(final long id) {
            comment.id = id;
            return this;
        }

        public CommentBuilder withLastModification(final LocalDateTime lastModification) {
            comment.lastModification = lastModification;
            return this;
        }

        public CommentBuilder withContent(final String content) {
            comment.content = content;
            return this;
        }

        public CommentBuilder withRecipeId(final long recipeId) {
            comment.recipeId = recipeId;
            return this;
        }

        public Comment build() {
            return comment;
        }
    }
}
