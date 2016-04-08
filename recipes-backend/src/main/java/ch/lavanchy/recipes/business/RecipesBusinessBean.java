package ch.lavanchy.recipes.business;

import ch.lavanchy.recipes.factories.RecipeFactory;
import ch.lavanchy.recipes.dao.RecipesDaoLocal;
import ch.lavanchy.recipes.dao.TagsDaoLocal;
import ch.lavanchy.recipes.data.Recipe;
import ch.lavanchy.recipes.entities.RecipeEntity;
import ch.lavanchy.recipes.entities.TagEntity;
import ch.lavanchy.recipes.query.QueryOperation;
import ch.lavanchy.recipes.utils.AccentHandler;
import ch.lavanchy.recipes.utils.KeywordFilter;
import org.apache.commons.lang3.StringUtils;

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

    @Inject
    private TagsDaoLocal tagsDao;

    @Inject
    private RecipeFactory recipeFactory;

    @Inject
    private AccentHandler accentHandler;

    @Inject
    private KeywordFilter keywordFilter;

    @Override
    public List<Recipe> findRecipes(final String filter) {
        final List<RecipeEntity> recipeEntities = recipesDao.findAllRecipes();
        final List<RecipeEntity> filteredRecipeEntities = filterRecipeEntities(recipeEntities, filter);
        return recipeFactory.convertRecipeEntityListToRecipe(filteredRecipeEntities);
    }

    @Override
    public List<Recipe> findRecipesWithFilter(QueryOperation filter) {
        final List<RecipeEntity> filteredRecipes = recipesDao.findRecipeWithFilter(filter);
        return recipeFactory.convertRecipeEntityListToRecipe(filteredRecipes);
    }

    @Override
    public Recipe findRecipeById(final long id) {
        final RecipeEntity recipeEntity = recipesDao.findRecipeById(id);
        return recipeFactory.convertRecipeEntityToRecipe(recipeEntity);
    }

    @Override
    public Recipe createRecipe(final Recipe recipe) {
        final RecipeEntity recipeEntity = recipeFactory.convertRecipeToRecipeEntity(recipe);
        final RecipeEntity persistedEntity = recipesDao.persistRecipe(recipeEntity);
        final List<String> tags = checkAndCleanTags(recipe.getTags());
        extractAndPersistTags(tags, persistedEntity);

        return recipeFactory.convertRecipeEntityToRecipe(persistedEntity);
    }

    private List<String> checkAndCleanTags(final List<String> tags) {
        final List<String> cleaned = new ArrayList<>();
        for (String tag : tags) {
            if (StringUtils.isNotEmpty(tag)) {
                final String lowerCase = tag.trim().toLowerCase();
                if (!cleaned.contains(lowerCase)) {
                    cleaned.add(lowerCase);
                }
            }
        }
        return keywordFilter.filterKeywords(cleaned);
    }

    @Override
    public Recipe updateRecipe(final Recipe recipe) {
        final RecipeEntity recipeEntity = recipesDao.findRecipeById(recipe.getId());
        final List<String> tags = checkAndCleanTags(recipe.getTags());
        recipeEntity.setName(recipe.getName());

        removeTags(recipeEntity);
        extractAndPersistTags(tags, recipeEntity);

        return recipeFactory.convertRecipeEntityToRecipe(recipeEntity);
    }

    private void removeTags(final RecipeEntity recipeEntity) {
        recipeEntity.getTags().clear();
    }

    @Override
    public Recipe setRecipeFilename(final long id, final String filename) {
        final RecipeEntity recipeEntity = recipesDao.findRecipeById(id);
        if (recipeEntity == null) {
            return null;
        } else {
            recipeEntity.setFilename(filename);
            return recipeFactory.convertRecipeEntityToRecipe(recipeEntity);
        }
    }

    private void extractAndPersistTags(final List<String> tags, final RecipeEntity persistedEntity) {
        final List<TagEntity> tagEntities = getAndPersistTags(tags);
        mapTagsToRecipe(persistedEntity, tagEntities);
    }

    private void mapTagsToRecipe(RecipeEntity persistedEntity, List<TagEntity> tagEntities) {
        final List<TagEntity> persistedTags = persistedEntity.getTags();
        for (TagEntity tagEntity : tagEntities) {
            if (!persistedTags.contains(tagEntity)) {
                persistedTags.add(tagEntity);
            }
        }
    }

    private List<TagEntity> getAndPersistTags(List<String> tags) {
        final List<TagEntity> tagEntities = new ArrayList<>(tags.size());
        final List<TagEntity> allTags = tagsDao.findAllTags();

        for (final String tag : tags) {
            final TagEntity tagEntity = findTagEntity(tag, allTags);

            if (tagEntity == null) {
                final TagEntity createTag = createAndPersistTag(tag);
                tagEntities.add(createTag);
            } else {
                tagEntities.add(tagEntity);
            }
        }
        return tagEntities;
    }

    private TagEntity findTagEntity(String tag, List<TagEntity> allTags) {
        for (TagEntity tagEntity : allTags) {
            if (tagEntity.getName().equals(tag)) {
                return tagEntity;
            }
        }
        return null;
    }

    private TagEntity createAndPersistTag(final String tagName) {
        final TagEntity tagEntity = new TagEntity();
        tagEntity.setName(tagName);
        return tagsDao.persistTag(tagEntity);
    }

    private List<RecipeEntity> filterRecipeEntities(final List<RecipeEntity> recipeEntities, final String filter) {
        final List<RecipeEntity> filteredRecipeEntities = new ArrayList<>();

        final List<String> filters = splitFilter(filter);
        final List<String> noKeywordFilters = keywordFilter.filterKeywords(filters);
        for (RecipeEntity recipeEntity : recipeEntities) {
            if (matches(recipeEntity, noKeywordFilters)) {
                filteredRecipeEntities.add(recipeEntity);
            }
        }

        return filteredRecipeEntities;
    }

    private boolean matches(final RecipeEntity recipeEntity, final List<String> filters) {
        final String name = recipeEntity.getName().toLowerCase();
        final List<TagEntity> tags = recipeEntity.getTags();

        for (final String filter : filters) {
            if (notMatchName(name, filter) && notMatchTag(tags, filter)) {
                return false;
            }
        }

        return true;
    }

    private boolean notMatchTag(final List<TagEntity> tags, final String filter) {
        for (TagEntity tag : tags) {
            final String tagName = tag.getName();
            if (tagName.contains(filter) || accentHandler.removeAccents(tagName).contains(accentHandler.removeAccents(filter))) {
                return false;
            }
        }
        return true;
    }

    private boolean notMatchName(String name, String filter) {
        return !(name.contains(filter) || accentHandler.removeAccents(name).contains(accentHandler.removeAccents(filter)));
    }

    private List<String> splitFilter(final String filter) {
        final String[] splitedFilter = filter.split(" ");
        return normalizeFilters(Arrays.asList(splitedFilter));
    }

    private List<String> normalizeFilters(List<String> filters) {
        final List<String> normalized = new ArrayList<>(filters.size());
        for (String filter : filters) {
            normalized.add(filter.trim().toLowerCase());
        }
        return normalized;
    }
}
