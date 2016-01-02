package ch.lavanchy.recipes.services;

import ch.lavanchy.recipes.business.FileBusinessLocal;
import ch.lavanchy.recipes.business.RecipesBusinessLocal;
import ch.lavanchy.recipes.data.Recipe;
import com.google.gson.Gson;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * REST Webservice to retrieve the recipes
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

    @GET
    public String getRecipeList(@QueryParam("filter") String filter) {
        final String validatedFilter = validateFilter(filter);
        final List<Recipe> recipes = recipesBusiness.findRecipes(validatedFilter);
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
        final Recipe persisted = recipesBusiness.createRecipe(recipe);
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
    @Path("/file/{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String uploadFile(
            @PathParam("id") long id,
            @FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition contentDispositionHeader) {
        System.out.println("File uploaded: " + contentDispositionHeader);

        final String filename = contentDispositionHeader.getFileName();
        fileBusiness.saveFile(fileInputStream, filename);
        return "{}";
    }

    private String validateFilter(String filter) {
        if (filter == null) {
            return EMPTY_FILTER;
        } else {
            return filter.trim();
        }
    }
}
