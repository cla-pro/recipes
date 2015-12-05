package ch.lavanchy.recipes.dao;

import ch.lavanchy.recipes.entities.RecipeEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of {@see RecipesDaoLocal}
 *
 * @since 1.0.0
 */
public class RecipesDaoBean implements RecipesDaoLocal {
    private final Map<String, RecipeEntity> db = new HashMap<>();

    {
        db.put("Endives au jambon", createRecipeEntity("Endives au jambon"));
        db.put("Lasagnes", createRecipeEntity("Lasagnes"));
        db.put("Spaghetti bolognaise", createRecipeEntity("Spaghetti bolognaise"));
        db.put("Fondant au chocolat", createRecipeEntity("Fondant au chocolat"));
    }

    private RecipeEntity createRecipeEntity(final String name) {
        RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setName(name);
        return recipeEntity;
    }

    @Override
    public List<RecipeEntity> findAllRecipes() {
        return new ArrayList<>(db.values());
    }

    @Override
    public List<RecipeEntity> findRecipesFilteredByName(String filter) {
        final List<RecipeEntity> matches = new ArrayList<>();
        for (String name : db.keySet()) {
            if (name.contains(filter)) {
                matches.add(db.get(name));
            }
        }

        return matches;
    }
}
