package ch.lavanchy.recipes.business;

import ch.lavanchy.recipes.dao.RecipesDaoLocal;
import ch.lavanchy.recipes.dao.TagsDaoLocal;
import ch.lavanchy.recipes.data.Recipe;
import ch.lavanchy.recipes.entities.RecipeEntity;
import ch.lavanchy.recipes.entities.TagEntity;
import ch.lavanchy.recipes.factories.RecipeFactory;
import ch.lavanchy.recipes.query.QueryOperation;
import ch.lavanchy.recipes.utils.AccentHandler;
import ch.lavanchy.recipes.utils.KeywordFilter;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Inject
    private FilenameFixer filenameFixer;

    @Override
    public List<Recipe> findRecipesWithFilter(final QueryOperation filter, final Optional<String> chunkStart) {
        final List<RecipeEntity> filteredRecipes = recipesDao.findRecipeWithFilter(filter, chunkStart);
        return recipeFactory.convertRecipeEntityListToRecipe(filteredRecipes);
    }

    @Override
    public Recipe findRecipeById(final long id) {
        final RecipeEntity recipeEntity = recipesDao.findRecipeById(id);
        return recipeFactory.convertRecipeEntityToRecipe(recipeEntity);
    }

    @Override
    public Recipe createRecipe(final Recipe recipe) {
        final Recipe fixedRecipe = Recipe
                .builder(recipe)
                .withFilename(filenameFixer.fixFilename(recipe.getName(), FilenameUtils.getExtension(recipe.getFilename())))
                .build();
        final RecipeEntity recipeEntity = recipeFactory.convertRecipeToRecipeEntity(fixedRecipe);
        final RecipeEntity persistedEntity = recipesDao.persistRecipe(recipeEntity);
        final List<String> tags = checkAndCleanTags(recipe.getTags());
        extractAndPersistTags(tags, persistedEntity);

        return recipeFactory.convertRecipeEntityToRecipe(persistedEntity);
    }

    private List<String> checkAndCleanTags(final List<String> tags) {
        final List<String> cleaned = tags.stream()
                .filter(tag -> StringUtils.isNotEmpty(tag))
                .map(tag -> tag.trim().toLowerCase())
                .distinct()
                .collect(Collectors.toList());
        return keywordFilter.filterKeywords(cleaned);
    }

    @Override
    public Recipe updateRecipe(final Recipe recipe) {
        final RecipeEntity recipeEntity = recipesDao.findRecipeById(recipe.getId());
        final List<String> tags = checkAndCleanTags(recipe.getTags());
        recipeEntity.setName(recipe.getName());
        recipeEntity.setRating(recipe.getRating());

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

    private void mapTagsToRecipe(final RecipeEntity persistedEntity, final List<TagEntity> tagEntities) {
        final Set<TagEntity> persistedTags = persistedEntity.getTags();
        tagEntities.stream()
                .filter(tagEntity -> !persistedTags.contains(tagEntity))
                .forEach(persistedTags::add);
    }

    private List<TagEntity> getAndPersistTags(final List<String> tags) {
        final List<TagEntity> tagEntities = new ArrayList<>(tags.size());
        final List<TagEntity> allTags = tagsDao.findAllTags();

        for (final String tag : tags) {
            final Optional<TagEntity> tagEntity = findTagEntity(tag, allTags);

            if (tagEntity.isPresent()) {
                tagEntities.add(tagEntity.get());
            } else {
                final TagEntity createTag = createAndPersistTag(tag);
                tagEntities.add(createTag);
            }
        }
        return tagEntities;
    }

    private Optional<TagEntity> findTagEntity(final String tag, final List<TagEntity> allTags) {
        // Don't use streams for efficiency
        for (final TagEntity tagEntity : allTags) {
            if (tagEntity.getName().equals(tag)) {
                return Optional.of(tagEntity);
            }
        }
        return Optional.empty();
    }

    private TagEntity createAndPersistTag(final String tagName) {
        final TagEntity tagEntity = new TagEntity();
        tagEntity.setName(tagName);
        return tagsDao.persistTag(tagEntity);
    }
}
