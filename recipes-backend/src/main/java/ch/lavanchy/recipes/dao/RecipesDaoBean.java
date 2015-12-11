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
    private static final Map<String, RecipeEntity> db = new HashMap<>();

    private static long nextId = 0L;

    static {
        db.put("Endives au jambon", createRecipeEntity(getAndIncNextId(), "Endives au jambon"));
        db.put("Lasagnes", createRecipeEntity(getAndIncNextId(), "Lasagnes"));
        db.put("Spaghetti bolognaise", createRecipeEntity(getAndIncNextId(), "Spaghetti bolognaise"));
        db.put("Fondant au chocolat", createRecipeEntity(getAndIncNextId(), "Fondant au chocolat"));
    }

    private static long getAndIncNextId() {
        return nextId++;
    }

    private static RecipeEntity createRecipeEntity(long id, final String name) {
        RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setId(id);
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

    @Override
    public RecipeEntity persistRecipe(RecipeEntity recipeEntity) {
        long id = getAndIncNextId();
        recipeEntity.setId(id);
        db.put(recipeEntity.getName(), recipeEntity);
        return recipeEntity;
    }
}
