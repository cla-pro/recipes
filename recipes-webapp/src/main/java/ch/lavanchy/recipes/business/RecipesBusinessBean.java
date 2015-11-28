package ch.lavanchy.recipes.business;


import ch.lavanchy.recipes.dao.RecipesDaoLocal;
import ch.lavanchy.recipes.data.Recipe;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation of {@see RecipesBusinessLocal}
 *
 * @since 1.0.0
 */
public class RecipesBusinessBean implements RecipesBusinessLocal {
    @Inject
    private RecipesDaoLocal recipesDao;

    @Override
    public List<Recipe> findRecipes(String filter) {
        return new ArrayList<>();
    }
}
