package ch.lavanchy.recipes.converter;

import ch.lavanchy.recipes.data.Recipe;
import ch.lavanchy.recipes.data.Tag;
import ch.lavanchy.recipes.entities.RecipeEntity;
import ch.lavanchy.recipes.entities.TagEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to convert the {@link TagEntity} to {@link Tag}
 *
 * @since 1.0.0
 */
public final class TagConverter {
    /**
     * Convert a list of {@link TagEntity} into a list of {@link Tag}
     *
     * @param tagEntities The list to convert
     * @return The converted list
     */
    public List<Tag> convertTagEntityListToTag(final List<TagEntity> tagEntities) {
        final List<Tag> tags = new ArrayList<>();

        for (TagEntity tagEntity : tagEntities) {
            tags.add(convertTagEntityToTag(tagEntity));
        }

        return tags;
    }

    /**
     * Convert a single {@link TagEntity} into a {@link Tag}.
     *
     * @param tagEntity The entity to convert
     * @return The converted object
     */
    public Tag convertTagEntityToTag(final TagEntity tagEntity) {
        return new Tag(tagEntity.getId(), tagEntity.getName(), tagEntity.getModificationDate());
    }

    /**
     * Convert a single {@link Tag} into a {@link TagEntity}
     *
     * @param tag The tag to convert
     * @return The converted entity
     */
    public TagEntity convertTagToTagEntity(final Tag tag) {
        final TagEntity tagEntity = new TagEntity();
        tagEntity.setId(tag.getId());
        tagEntity.setName(tag.getName());
        tagEntity.setModificationDate(tag.getModificationDate());
        return tagEntity;
    }
}
