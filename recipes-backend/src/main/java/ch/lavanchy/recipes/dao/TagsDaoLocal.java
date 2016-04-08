package ch.lavanchy.recipes.dao;

import ch.lavanchy.recipes.entities.TagEntity;

import java.util.List;

/**
 * Data access to the DB for the tags.
 *
 * @since 1.0.0
 */
public interface TagsDaoLocal {
    /**
     * Get the list of all the existing tags as {@link TagEntity}.
     *
     * @return The whole list of existing tags
     */
    List<TagEntity> findAllTags();

    /**
     * Persist the given {@link TagEntity}.
     *
     * @param tagEntity The tag to persist
     * @return The persisted entity
     */
    TagEntity persistTag(final TagEntity tagEntity);
}
