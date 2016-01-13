package ch.lavanchy.recipes.services;

import ch.lavanchy.recipes.business.TagsBusinessLocal;
import ch.lavanchy.recipes.data.Tag;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.List;

/**
 * REST Webservice to manage the tags
 *
 * @since 1.0.0
 */
@Path("/tags")
public class TagsService {
    @Inject
    private TagsBusinessLocal tagsBusiness;

    @GET
    public String getAllTags(@QueryParam("since") final Long since) {
        final List<Tag> tags = findAllTags(since);
        return new Gson().toJson(tags);
    }

    private List<Tag> findAllTags(@QueryParam("since") Long since) {
        if (since == null) {
            return tagsBusiness.findAllTags();
        } else {
            return tagsBusiness.findAllTagsSince(since);
        }
    }
}
