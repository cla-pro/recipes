package ch.lavanchy.recipes.business;

import ch.lavanchy.recipes.data.Tag;

import java.util.List;

/**
 * Provide the logic to handle the tags' references
 *
 * @since 1.0.0
 */
public interface TagsBusinessLocal {
    /**
     * Return all the tags
     *
     * @return the list of all tags
     */
    List<Tag> findAllTags();

    /**
     * Return all the tags which with a change/create date younger as "since"
     *
     * @param since The reference date
     * @return the list of all the corresponding tags
     */
    List<Tag> findAllTagsSince(final long since);
}
