package ch.lavanchy.recipes.services;

import ch.lavanchy.recipes.business.CommentsBusinessLocal;
import ch.lavanchy.recipes.data.Comment;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * REST Webservice to manage the comments
 *
 * @since 2.0.0
 */
@Path("/comments")
public class CommentsService {
    @Inject
    private CommentsBusinessLocal commentsBusiness;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getComments(final Long recipeId) {
        if (recipeId == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            final List<Comment> result = commentsBusiness.findCommentsForRecipe(recipeId);

            if (result.isEmpty()) {
                return Response.noContent().build();
            } else {
                return Response.ok(new Gson().toJson(result)).build();
            }
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String createRecipe(final Comment comment) {
        final Comment persisted = commentsBusiness.createComment(comment);
        return new Gson().toJson(persisted);
    }
}
