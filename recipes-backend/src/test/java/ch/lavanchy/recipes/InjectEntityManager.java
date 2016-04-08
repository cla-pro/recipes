package ch.lavanchy.recipes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark a field for EntityManager injection performed by the {@link JpaTransactionRule} in a unit-test class.
 *
 * @since 1.0.2
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface InjectEntityManager {
}
