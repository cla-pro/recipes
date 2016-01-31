package ch.lavanchy.recipes.config;

import ch.lavanchy.recipes.business.FileBusinessBean;
import ch.lavanchy.recipes.business.FileBusinessLocal;
import ch.lavanchy.recipes.converter.FileConverter;
import ch.lavanchy.recipes.business.RecipesBusinessBean;
import ch.lavanchy.recipes.business.RecipesBusinessLocal;
import ch.lavanchy.recipes.business.TagsBusinessBean;
import ch.lavanchy.recipes.business.TagsBusinessLocal;
import ch.lavanchy.recipes.converter.RecipeConverter;
import ch.lavanchy.recipes.converter.TagConverter;
import ch.lavanchy.recipes.dao.RecipesDaoBean;
import ch.lavanchy.recipes.dao.RecipesDaoLocal;
import ch.lavanchy.recipes.dao.TagsDaoBean;
import ch.lavanchy.recipes.dao.TagsDaoLocal;
import ch.lavanchy.recipes.services.CheckService;
import ch.lavanchy.recipes.services.RecipesService;
import ch.lavanchy.recipes.services.RootService;
import ch.lavanchy.recipes.services.TagsService;
import ch.lavanchy.recipes.services.TransactionFilter;
import ch.lavanchy.recipes.utils.AccentHandler;
import ch.lavanchy.recipes.utils.PropertyProviderBean;
import ch.lavanchy.recipes.utils.PropertyProviderLocal;
import com.google.inject.Singleton;
import com.google.inject.persist.PersistFilter;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

import java.util.HashMap;
import java.util.Map;

/**
 * Jersey configuration and dependency injection with Guice
 *
 * @since 1.0.0
 */
class RecipesJerseyServletModule extends JerseyServletModule {
    @Override
    protected void configureServlets() {
        bind(TransactionFilter.class).in(Singleton.class);

        bind(RecipeConverter.class).in(Singleton.class);
        bind(TagConverter.class).in(Singleton.class);
        bind(FileConverter.class).in(Singleton.class);

        //bind(AccentHandler.class).in(Singleton.class);

        bind(PropertyProviderLocal.class).to(PropertyProviderBean.class);

        bind(RecipesDaoLocal.class).to(RecipesDaoBean.class);
        bind(TagsDaoLocal.class).to(TagsDaoBean.class);

        bind(FileBusinessLocal.class).to(FileBusinessBean.class);
        bind(RecipesBusinessLocal.class).to(RecipesBusinessBean.class);
        bind(TagsBusinessLocal.class).to(TagsBusinessBean.class);

        bind(RecipesService.class);
        bind(TagsService.class);
        bind(CheckService.class);
        bind(RootService.class);

        // Route all requests through GuiceContainer
        final Map<String, String> params = new HashMap<>();
        params.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
        filter("/services/*").through(PersistFilter.class);
        filter("/services/*").through(TransactionFilter.class);
        serve("/services/*").with(GuiceContainer.class, params);
    }
}
