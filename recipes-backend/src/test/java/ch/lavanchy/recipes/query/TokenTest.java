package ch.lavanchy.recipes.query;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for {@link Token}
 *
 * @since 1.0.2
 */
public class TokenTest {
    @Test
    public void testEqualsNull() throws Exception {
        final Token tokenAndAnd1 = new Token(TokenType.AND, "AND");
        final Token tokenAndAnd2 = new Token(TokenType.AND, "AND");
        final Token tokenAndOr = new Token(TokenType.AND, "OR");
        final Token tokenOrAnd = new Token(TokenType.OR, "AND");
        final Token tokenOrOr = new Token(TokenType.OR, "OR");

        assertThat(tokenAndAnd1).isNotEqualTo(null);
        assertThat(tokenAndAnd1).isNotEqualTo(Integer.valueOf(3));
        assertThat(tokenAndAnd1).isEqualTo(tokenAndAnd1);
        assertThat(tokenAndAnd1).isEqualTo(tokenAndAnd2);
        assertThat(tokenAndAnd1).isNotEqualTo(tokenAndOr);
        assertThat(tokenAndAnd1).isNotEqualTo(tokenOrAnd);
        assertThat(tokenAndAnd1).isNotEqualTo(tokenOrOr);
    }
}