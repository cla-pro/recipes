package ch.lavanchy.recipes.factories;

import ch.lavanchy.recipes.data.Tag;
import ch.lavanchy.recipes.entities.TagEntity;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class used to create the {@link TagEntity} and the {@link Tag}
 *
 * @since 1.0.0
 */
public class TagFactory {
    /**
     * Convert a list of {@link TagEntity} into a list of {@link Tag}
     *
     * @param tagEntities The list to convert
     * @return The converted list
     */
    public List<Tag> convertTagEntityListToTag(final List<TagEntity> tagEntities) {
        return tagEntities.stream()
                .map(tagEntity -> convertTagEntityToTag(tagEntity))
                .collect(Collectors.toList());
    }

    /**
     * Convert a single {@link TagEntity} into a {@link Tag}.
     *
     * @param tagEntity The entity to convert
     * @return The converted object
     */
    private Tag convertTagEntityToTag(final TagEntity tagEntity) {
        return Tag.builder()
                .withId(tagEntity.getId())
                .withName(tagEntity.getName())
                .withModificationDate(tagEntity.getModificationDate())
                .build();
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
