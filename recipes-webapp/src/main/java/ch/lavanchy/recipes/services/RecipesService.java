package ch.lavanchy.recipes.services;

import ch.lavanchy.recipes.business.RecipesBusinessLocal;
import ch.lavanchy.recipes.data.Recipe;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.ArrayList;
import java.util.List;

/**
 * REST Webservice to retrieve the recipes
 *
 * @since 1.0.0
 */
@Path("recipes")
public class RecipesService {
    @Inject
    private RecipesBusinessLocal recipesBusiness;

    @GET
    public String getRecipeList(@QueryParam("filter") String filter) {
        List<Recipe> recipes = recipesBusiness.findRecipes(filter);
        return new Gson().toJson(recipes);
    }
}
