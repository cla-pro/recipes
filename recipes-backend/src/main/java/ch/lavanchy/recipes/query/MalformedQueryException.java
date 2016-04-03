package ch.lavanchy.recipes.query;

/**
 * Thrown if the query is malformed
 *
 * <ul>
 *     <li>Unbalanced quotes</li>
 *     <li>Unbalanced parenthesis</li>
 * </ul>
 *
 * @since 1.0.2
 */
public class MalformedQueryException extends RuntimeException {
    public MalformedQueryException(String message) {
        super(message);
    }
}
