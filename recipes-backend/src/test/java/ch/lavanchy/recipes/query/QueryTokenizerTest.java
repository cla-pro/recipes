package ch.lavanchy.recipes.query;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class QueryTokenizerTest {
    private void runScenario(final String query, final List<Token> expectedTokens) {
        final QueryTokenizer queryTokenizer = new QueryTokenizer(query);

        for (final Token expectedToken : expectedTokens) {
            assertThat(queryTokenizer.hasMoreToken()).isTrue();
            assertThat(queryTokenizer.nextToken()).isEqualTo(expectedToken);
        }

        assertThat(queryTokenizer.hasMoreToken()).isFalse();
    }

    @Test
    public void testEmpty() {
        runScenario("", Collections.<Token> emptyList());
    }

    @Test
    public void testOnlySpaces() {
        runScenario("   ", Collections.<Token> emptyList());
    }

    @Test
    public void testReserved() {
        runScenario("  AND ", Collections.singletonList(andToken()));
        runScenario("  OR ", Collections.singletonList(orToken()));
        runScenario("  NOT ", Collections.singletonList(notToken()));
        runScenario("  ( ", Collections.singletonList(openToken()));
        runScenario("  ) ", Collections.singletonList(closeToken()));
    }

    @Test
    public void testSingleFilter() {
        runScenario("  abc ", Collections.singletonList(textToken("abc")));
    }

    @Test
    public void testMultipleFilter() {
        runScenario("  abc  def  ", Arrays.asList(textToken("abc"), textToken("def")));
    }

    @Test
    public void testQuotedFilter() {
        runScenario(" \" abc  def\" ", Collections.singletonList(textToken(" abc  def")));
    }

    @Test
    public void testParenthesis() {
        runScenario(" (abc def ) ", Arrays.asList(openToken(), textToken("abc"), textToken("def"), closeToken()));
    }

    @Test
    public void testNestedParenthesis() {
        runScenario(" ((((abc) def) ghi ) klm ) ",
                Arrays.asList(
                        openToken(),
                        openToken(),
                        openToken(),
                        openToken(),
                        textToken("abc"),
                        closeToken(),
                        textToken("def"),
                        closeToken(),
                        textToken("ghi"),
                        closeToken(),
                        textToken("klm"),
                        closeToken()));
    }

    @Test
    public void testFetch() {
        final QueryTokenizer queryTokenizer = new QueryTokenizer("bla blou");
        assertThat(queryTokenizer.hasMoreToken()).isTrue();
        assertThat(queryTokenizer.fetchToken()).isEqualTo(textToken("bla"));
        assertThat(queryTokenizer.hasMoreToken()).isTrue();
        assertThat(queryTokenizer.fetchToken()).isEqualTo(textToken("bla"));
        assertThat(queryTokenizer.hasMoreToken()).isTrue();
        assertThat(queryTokenizer.fetchToken()).isEqualTo(textToken("bla"));
        assertThat(queryTokenizer.hasMoreToken()).isTrue();
        assertThat(queryTokenizer.nextToken()).isEqualTo(textToken("bla"));
        assertThat(queryTokenizer.hasMoreToken()).isTrue();
        assertThat(queryTokenizer.fetchToken()).isEqualTo(textToken("blou"));
        assertThat(queryTokenizer.hasMoreToken()).isTrue();
        assertThat(queryTokenizer.fetchToken()).isEqualTo(textToken("blou"));
        assertThat(queryTokenizer.hasMoreToken()).isTrue();
        assertThat(queryTokenizer.fetchToken()).isEqualTo(textToken("blou"));
        assertThat(queryTokenizer.hasMoreToken()).isTrue();
        assertThat(queryTokenizer.nextToken()).isEqualTo(textToken("blou"));
        assertThat(queryTokenizer.hasMoreToken()).isFalse();
    }

    private Token textToken(final String text) {
        return new Token(TokenType.TEXT, text);
    }

    private Token andToken() {
        return new Token(TokenType.AND, "AND");
    }

    private Token orToken() {
        return new Token(TokenType.OR, "OR");
    }

    private Token openToken() {
        return new Token(TokenType.OPEN_PARENTHESIS, "(");
    }

    private Token closeToken() {
        return new Token(TokenType.CLOSE_PARENTHESIS, ")");
    }

    private Token notToken() {
        return new Token(TokenType.NOT, "NOT");
    }
}