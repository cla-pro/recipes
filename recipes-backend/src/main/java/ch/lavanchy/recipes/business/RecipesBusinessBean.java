package ch.lavanchy.recipes.business;


import ch.lavanchy.recipes.converter.RecipeConverter;
import ch.lavanchy.recipes.dao.RecipesDaoLocal;
import ch.lavanchy.recipes.data.Recipe;
import ch.lavanchy.recipes.entities.RecipeEntity;

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
        final List<RecipeEntity> recipeEntities = recipesDao.findAllRecipes();
        final List<RecipeEntity> filteredRecipeEntities = filterRecipeEntities(recipeEntities, filter);
        return new RecipeConverter().convertRecipeEntityListToRecipe(filteredRecipeEntities);
    }

    private List<RecipeEntity> filterRecipeEntities(List<RecipeEntity> recipeEntities, final String filter) {
        final List<RecipeEntity> filteredRecipeEntities = new ArrayList<>();

        final List<String> splitedFilter = splitFilter(filter);
        for (RecipeEntity recipeEntity : recipeEntities) {
            if (matches(recipeEntity, splitedFilter)) {
                filteredRecipeEntities.add(recipeEntity);
            }
        }

        return filteredRecipeEntities;
    }

    private boolean matches(RecipeEntity recipeEntity, List<String> splitedFilter) {
        final String name = recipeEntity.getName().toLowerCase();
        for (String filter : splitedFilter) {
            if (!name.contains(filter.toLowerCase())) {
                return false;
            }
        }

        return true;
    }

    private List<String> splitFilter(String filter) {
        String[] splitedFilter = filter.split(" ");
        return Arrays.asList(splitedFilter);
    }
}
