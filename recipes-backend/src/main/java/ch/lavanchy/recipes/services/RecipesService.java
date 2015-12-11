package ch.lavanchy.recipes.services;

import ch.lavanchy.recipes.business.RecipesBusinessLocal;
import ch.lavanchy.recipes.data.Recipe;
import com.google.gson.Gson;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;
import java.util.ArrayList;
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
    private RecipesBusinessLocal recipesBusiness;

    @GET
    public String getRecipeList(@QueryParam("filter") String filter) {
        final String validatedFilter = validate(filter);
        final List<Recipe> recipes = recipesBusiness.findRecipes(validatedFilter);
        return new Gson().toJson(recipes);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String createRecipe(final String payload) {
        final Recipe recipe = new Gson().fromJson(payload, Recipe.class);
        final Recipe persisted = recipesBusiness.createRecipe(recipe);
        return new Gson().toJson(persisted);
    }

    @POST
    @Path("/file")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public String uploadFile(@FormDataParam("file") InputStream fileInputStream,
                             @FormDataParam("file") FormDataContentDisposition contentDispositionHeader) {
        System.out.println("File uploaded: " + contentDispositionHeader);
        return "";
    }

    private String validate(String filter) {
        if (filter == null) {
            return EMPTY_FILTER;
        } else {
            return filter.trim();
        }
    }
}
