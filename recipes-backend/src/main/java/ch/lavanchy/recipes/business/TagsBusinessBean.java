package ch.lavanchy.recipes.business;

import ch.lavanchy.recipes.converter.TagConverter;
import ch.lavanchy.recipes.dao.TagsDaoLocal;
import ch.lavanchy.recipes.data.Tag;
import ch.lavanchy.recipes.entities.TagEntity;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of {@see TagsBusinessLocal}
 *
 * @since 1.0.0
 */
public class TagsBusinessBean implements TagsBusinessLocal {
    @Inject
    private TagsDaoLocal tagsDao;

    @Inject
    private TagConverter tagConverter;

    @Override
    public List<Tag> findAllTags() {
        List<TagEntity> tags = tagsDao.findAllTags();
        return tagConverter.convertTagEntityListToTag(tags);
    }

    @Override
    public List<Tag> findAllTagsSince(long since) {
        List<TagEntity> tags = tagsDao.findAllTags();
        List<TagEntity> youngTags = filterTagsByModificationDate(tags, since);
        return tagConverter.convertTagEntityListToTag(youngTags);
    }

    private List<TagEntity> filterTagsByModificationDate(List<TagEntity> tags, long since) {
        List<TagEntity> filtered = new ArrayList<>();
        for (TagEntity tagEntity : tags) {
            if (tagEntity.getModificationDate() > since) {
                filtered.add(tagEntity);
            }
        }
        return filtered;
    }
}
