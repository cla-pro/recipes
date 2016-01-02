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
     * Find a single {@link TagEntity} by name.
     *
     * @param name The name to search for
     * @return The {@link TagEntity} if found, else null
     */
    TagEntity findTagByName(final String name);

    /**
     * Persist the given {@link TagEntity}.
     *
     * @param tagEntity The tag to persist
     * @return The persisted entity
     */
    TagEntity persistTag(final TagEntity tagEntity);
}
