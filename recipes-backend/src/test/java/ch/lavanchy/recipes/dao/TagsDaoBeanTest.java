package ch.lavanchy.recipes.dao;

import ch.lavanchy.recipes.InjectEntityManager;
import ch.lavanchy.recipes.JpaTransactionRule;
import ch.lavanchy.recipes.entities.TagEntity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for {@link TagsDaoBean}
 *
 * @since 1.0.2
 */
public class TagsDaoBeanTest {
    @Rule
    public TestRule jpaTransactionRule = new JpaTransactionRule(this);

    @Inject
    private EntityManager entityManager;

    @InjectEntityManager
    private TagsDaoLocal testee = new TagsDaoBean();

    @Test
    public void testFindAllTags() {
        assertThat(testee.findAllTags()).isEmpty();
        testee.persistTag(createTagWithName("tag1"));
        assertThat(testee.findAllTags()).hasSize(1);
        testee.persistTag(createTagWithName("tag2"));
        assertThat(testee.findAllTags()).hasSize(2);
    }

    @Test
    public void testPersistTag() {
        final TagEntity tagEntity = createTagWithName("myTag");

        assertThat(tagEntity.getId()).isNull();
        final TagEntity persistedEntity = testee.persistTag(tagEntity);

        assertThat(persistedEntity.getId()).isNotNull();
    }

    private TagEntity createTagWithName(final String tagName) {
        final TagEntity tagEntity = new TagEntity();
        tagEntity.setName(tagName);
        return tagEntity;
    }
}