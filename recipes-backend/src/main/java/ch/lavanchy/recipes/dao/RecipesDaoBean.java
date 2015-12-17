package ch.lavanchy.recipes.dao;

import ch.lavanchy.recipes.entities.RecipeEntity;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
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
    @Inject
    private EntityManager entityManager;

    /*private static final Map<String, RecipeEntity> db = new HashMap<>();

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
    }*/

    @Override
    public List<RecipeEntity> findAllRecipes() {
        final CriteriaQuery<RecipeEntity> criteriaQuery = entityManager.getCriteriaBuilder().createQuery(RecipeEntity.class);
        criteriaQuery.select(criteriaQuery.from(RecipeEntity.class));
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<RecipeEntity> findRecipesFilteredByName(String filter) {
        final Query query = entityManager.createQuery("SELECT recipe FROM RecipeEntity recipe WHERE recipe.name LIKE :filter");
        query.setParameter("filter", filter);
        return query.getResultList();
    }

    @Override
    public RecipeEntity findRecipeById(long id) {
        return entityManager.find(RecipeEntity.class, id);
    }

    @Override
    public RecipeEntity persistRecipe(RecipeEntity recipeEntity) {
        entityManager.persist(recipeEntity);
        return recipeEntity;
    }
}
