package ch.lavanchy.recipes.data;

/**
 * Contains the elements of a tag
 * <p/>
 * <ul>
 * <li>The id</li>
 * <li>The name</li>
 * <li>The change/creation date</li>
 * </ul>
 *
 * @since 1.0.0
 */
public class Tag {
    private final Long id;
    private final String name;
    private final long modificationDate;

    public Tag() {
        this(null, null, 0L);
    }

    public Tag(Long id, String name, long modificationDate) {
        this.id = id;
        this.name = name;
        this.modificationDate = modificationDate;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getModificationDate() {
        return modificationDate;
    }
}
