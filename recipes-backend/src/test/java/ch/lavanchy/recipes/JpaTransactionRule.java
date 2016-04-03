package ch.lavanchy.recipes;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import java.lang.reflect.Field;

/**
 * Rule that create the in-memory H2 DB and create the tables, start a transaction before the test,
 * rollback it right after and drop all the DB objects.
 *
 * @since 1.0.2
 */
public class JpaTransactionRule implements TestRule {
    private final static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("recipes-test-pu");
    private final Object target;

    public JpaTransactionRule(final Object target) {
        this.target = target;
    }

    @Override
    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                final EntityManager entityManager = entityManagerFactory.createEntityManager();
                injectEntityManager(target, entityManager);

                try {
                    entityManager.getTransaction().begin();

                    base.evaluate();
                } finally {
                    if (entityManager.getTransaction().isActive()) {
                        entityManager.getTransaction().rollback();
                    }
                }
            }
        };
    }

    private void injectEntityManager(final Object target, final EntityManager entityManager) throws IllegalAccessException {
        final Class<?> targetClass = target.getClass();
        for (final Field field : targetClass.getDeclaredFields()) {
            if (field.getAnnotation(Inject.class) != null && isSubclassOfEntityManager(field.getType())) {
                field.setAccessible(true);
                field.set(target, entityManager);
            } else if (field.getAnnotation(InjectEntityManager.class) != null) {
                field.setAccessible(true);
                final Object value = field.get(target);
                if (value != null) {
                    injectEntityManager(value, entityManager);
                }
            }
        }
    }

    private boolean isSubclassOfEntityManager(final Class<?> type) {
        return EntityManager.class.isAssignableFrom(type);
    }
}
