package ch.lavanchy.recipes.business;

import static ch.lavanchy.recipes.data.Tag.TagBuilder;

import ch.lavanchy.recipes.converter.TagFactory;
import ch.lavanchy.recipes.dao.TagsDaoLocal;
import ch.lavanchy.recipes.data.Tag;
import ch.lavanchy.recipes.entities.TagEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link TagsBusinessBean}
 *
 * @since 1.0.0
 */
@RunWith(MockitoJUnitRunner.class)
public class TagsBusinessBeanTest {
    @Mock
    private TagsDaoLocal tagsDao;

    @Spy
    private TagFactory tagFactory = new TagFactory();

    @InjectMocks
    private TagsBusinessLocal tagsBusiness = new TagsBusinessBean();

    @Before
    public void setUp() {
        when(tagsDao.findAllTags()).thenReturn(Arrays.asList(createTagEntity("fish", 0L), createTagEntity("bread", 10000L)));
    }

    private TagEntity createTagEntity(String name, long modificationDate) {
        final TagEntity tagEntity = new TagEntity();
        tagEntity.setName(name);
        tagEntity.setModificationDate(modificationDate);
        return tagEntity;
    }

    private Tag createTag(String name) {
        return new TagBuilder()
                .withName(name)
                .withModificationDate(0L)
                .build();
    }

    @Test
    public void testFindAllTags() throws Exception {
        final List<Tag> tags = tagsBusiness.findAllTags();
        assertThat(tags).hasSize(2).contains(createTag("fish"), createTag("bread"));
    }

    @Test
    public void testFindAllTagsSince() throws Exception {
        final List<Tag> allTagsSince = tagsBusiness.findAllTagsSince(5000L);
        assertThat(allTagsSince).hasSize(1).contains(createTag("bread"));
    }
}