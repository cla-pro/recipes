package ch.lavanchy.recipes.services;

import ch.lavanchy.recipes.business.FileBusinessLocal;
import ch.lavanchy.recipes.business.RecipesBusinessLocal;
import ch.lavanchy.recipes.data.Recipe;
import ch.lavanchy.recipes.query.QueryOperation;
import ch.lavanchy.recipes.query.QueryOperationFactory;
import com.google.gson.Gson;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.FormDataParam;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * REST Webservice to manage the recipes
 *
 * @since 1.0.0
 */
@Path("/recipes")
public class RecipesService {
    private static final String EMPTY_FILTER = "";

    @Inject
    private FileBusinessLocal fileBusiness;

    @Inject
    private RecipesBusinessLocal recipesBusiness;

    @Inject
    private QueryOperationFactory queryOperationFactory;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getRecipeList(@QueryParam("filter") String filter) {
        final String validatedFilter = validateFilter(filter);
        final QueryOperation queryOperation = queryOperationFactory.createQueryOperation(validatedFilter);
        final List<Recipe> recipes = recipesBusiness.findRecipesWithFilter(queryOperation);
        return new Gson().toJson(recipes);
    }

    @GET
    @Path("/{id}")
    public String getRecipe(@PathParam("id") long id) {
        final Recipe recipe = recipesBusiness.findRecipeById(id);
        return new Gson().toJson(recipe);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String createRecipe(final Recipe recipe) {
        validateRecipe(recipe);

        final Recipe persisted = recipesBusiness.createRecipe(recipe);
        return new Gson().toJson(persisted);
    }

    @PUT
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String updateRecipe(
            final FormDataMultiPart multiPart,
            @FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition contentDispositionHeader) {
        final Recipe recipe = new Gson().fromJson(multiPart.getField("recipe").getValue(), Recipe.class);
        final Recipe persisted = recipesBusiness.updateRecipe(recipe);

        if (fileInputStream != null) {
            fileBusiness.saveFile(fileInputStream, persisted.getFilename(), true);
        }

        return new Gson().toJson(persisted);
    }

    @GET
    @Path("/pdf/{id}")
    public Response getFile(@PathParam("id") long id) throws IOException {
        Recipe recipe = recipesBusiness.findRecipeById(id);
        final InputStream inputStream = fileBusiness.readFile(recipe.getFilename());

        if (inputStream == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response
                    .ok(new FileStreamingOutput(inputStream))
                    .header("content-disposition", "attachment; filename = " + recipe.getFilename())
                    .build();
        }
    }

    @POST
    @Path("/file")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String uploadFile(
            final FormDataMultiPart multiPart,
            @FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition contentDispositionHeader) {
        final Recipe recipe = new Gson().fromJson(multiPart.getField("recipe").getValue(), Recipe.class);
        validateRecipe(recipe);
        final Recipe persisted = recipesBusiness.createRecipe(recipe);
        fileBusiness.saveFile(fileInputStream, persisted.getFilename(), false);
        return new Gson().toJson(persisted);
    }

    private String validateFilter(final String filter) {
        if (filter == null) {
            return EMPTY_FILTER;
        } else {
            return filter.trim();
        }
    }

    private void validateRecipe(final Recipe recipe) {
        if (StringUtils.isEmpty(recipe.getName())) {
            throw new RuntimeException("Recipe's name is empty");
        }
    }
}
