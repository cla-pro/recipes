package ch.lavanchy.recipes.config;

import ch.lavanchy.recipes.business.*;
import ch.lavanchy.recipes.dao.*;
import ch.lavanchy.recipes.factories.CommentFactory;
import ch.lavanchy.recipes.factories.FileConverter;
import ch.lavanchy.recipes.factories.RecipeFactory;
import ch.lavanchy.recipes.factories.TagFactory;
import ch.lavanchy.recipes.factories.converters.ToPDFConverterFactory;
import ch.lavanchy.recipes.query.QueryOperationFactory;
import ch.lavanchy.recipes.services.*;
import ch.lavanchy.recipes.services.filters.LoggingFilter;
import ch.lavanchy.recipes.services.filters.TransactionFilter;
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
        bind(LoggingFilter.class).in(Singleton.class);
        bind(TransactionFilter.class).in(Singleton.class);
        bind(ThrowableMapper.class).in(Singleton.class);

        bind(CommentFactory.class).in(Singleton.class);
        bind(RecipeFactory.class).in(Singleton.class);
        bind(TagFactory.class).in(Singleton.class);
        bind(FileConverter.class).in(Singleton.class);
        bind(ToPDFConverterFactory.class).in(Singleton.class);
        bind(QueryOperationFactory.class).in(Singleton.class);

        bind(PropertyProviderLocal.class).to(PropertyProviderBean.class);

        bind(CommentsDaoLocal.class).to(CommentsDaoBean.class);
        bind(RecipesDaoLocal.class).to(RecipesDaoBean.class);
        bind(TagsDaoLocal.class).to(TagsDaoBean.class);

        bind(CommentsBusinessLocal.class).to(CommentsBusinessBean.class);
        bind(FileBusinessLocal.class).to(FileBusinessBean.class);
        bind(RecipesBusinessLocal.class).to(RecipesBusinessBean.class);
        bind(TagsBusinessLocal.class).to(TagsBusinessBean.class);

        bind(CommentsService.class);
        bind(RecipesService.class);
        bind(TagsService.class);
        bind(CheckService.class);
        bind(RootService.class);

        // Route all requests through GuiceContainer
        final Map<String, String> params = new HashMap<>();
        params.put("com.sun.jersey.api.json.POJOMappingFeature", "true");
        filter("/services/*").through(LoggingFilter.class);
        filter("/services/*").through(PersistFilter.class);
        filter("/services/*").through(TransactionFilter.class);
        serve("/services/*").with(GuiceContainer.class, params);
    }
}
