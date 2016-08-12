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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

/**
 * REST Webservice to manage the recipes
 *
 * @since 1.0.0
 */
@Path("/recipes")
public class RecipesService {
    private static final Logger LOGGER = LogManager.getLogger(RecipesService.class);

    private static final String EMPTY_FILTER = "";

    @Inject
    private FileBusinessLocal fileBusiness;

    @Inject
    private RecipesBusinessLocal recipesBusiness;

    @Inject
    private QueryOperationFactory queryOperationFactory;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getRecipeList(
            @QueryParam("filter") final String filter,
            @QueryParam("chunkStart") final String chunkStart,
            @QueryParam("size") final Integer size) {
        final String validatedFilter = validateFilter(filter);
        final QueryOperation queryOperation = queryOperationFactory.createQueryOperation(validatedFilter);
        final List<Recipe> recipes = recipesBusiness.findRecipesWithFilter(
                queryOperation,
                Optional.ofNullable(chunkStart),
                Optional.ofNullable(size));
        return new Gson().toJson(recipes);
    }

    @GET
    @Path("/{id}")
    public String getRecipe(@PathParam("id") final long id) {
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
            @FormDataParam("file") final InputStream fileInputStream,
            @FormDataParam("file") final FormDataContentDisposition contentDispositionHeader) {
        final Recipe recipe = new Gson().fromJson(multiPart.getField("recipe").getValue(), Recipe.class);
        final Recipe persisted = recipesBusiness.updateRecipe(recipe);

        if (fileInputStream != null) {
            fileBusiness.saveFile(fileInputStream, persisted.getFilename(), true);
        }

        return new Gson().toJson(persisted);
    }

    @GET
    @Path("/pdf/{id}")
    public Response getPDFFile(@PathParam("id") final long id) throws IOException {
        final Recipe recipe = recipesBusiness.findRecipeById(id);
        final File toUpload = fileBusiness.readPDFFile(recipe.getFilename());
        return createResponseFromFile(toUpload);
    }

    @GET
    @Path("/file/{id}")
    public Response getOriginalFile(@PathParam("id") final long id) throws IOException {
        final Recipe recipe = recipesBusiness.findRecipeById(id);
        final File toUpload = fileBusiness.readOriginalFile(recipe.getFilename());
        return createResponseFromFile(toUpload);
    }

    private Response createResponseFromFile(final File toUpload) {
        if (toUpload == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } else {
            return Response
                    .ok(new BytesStreamingOutput(toUpload))
                    .header("content-disposition", "attachment; filename = " + toUpload.getName())
                    .build();
        }
    }

    @POST
    @Path("/file")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String uploadFile(
            final FormDataMultiPart multiPart,
            @FormDataParam("file") final InputStream fileInputStream,
            @FormDataParam("file") final FormDataContentDisposition contentDispositionHeader) {
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
            LOGGER.info("Invalid recipe name=\"{}\"", recipe.getName());
            throw new IllegalArgumentException("Recipe's name is empty");
        }
    }
}
