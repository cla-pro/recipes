package ch.lavanchy.recipes.query;

import java.util.HashSet;
import java.util.Set;

/**
 * Create the QueryDSL from the string query (received as param).
 *
 * @since 1.0.2
 */
class QueryTokenizer {
    private static final Set<Character> RESERVED_CHARS;

    static {
        RESERVED_CHARS = new HashSet<>();
        RESERVED_CHARS.add(' ');
        RESERVED_CHARS.add('(');
        RESERVED_CHARS.add(')');
        RESERVED_CHARS.add('"');
    }

    private final String query;
    private int previousPosition = 0;
    private int position = 0;
    private Token nextToken = null;

    QueryTokenizer(final String query) {
        this.query = query.trim();
    }

    boolean hasMoreToken() {
        if (nextToken == null) {
            return position < query.length();
        } else {
            return previousPosition < query.length();
        }
    }

    /**
     * Extract the next token from the query. A call to this method moves the cursor and the last token
     * can thus not been retrieved anymore. Two following calls to this method won't return the same result.
     * If the goal is not to move the cursor, use {@link #fetchToken()}.
     *
     * @return The next token
     */
    Token nextToken() {
        if (nextToken == null) {
            final String tokenText = extractTokenAndMovePointer();
            position = skipSpaces(position);
            return new Token(findTokenType(tokenText), tokenText);
        } else {
            final Token toReturn = nextToken;
            nextToken = null;
            return toReturn;
        }
    }

    /**
     * Fetch the next token without moving the cursor (to move the cursor, use {@link #nextToken()}). Two following
     * calls to this method return the same result
     *
     * @return The next token
     */
    Token fetchToken() {
        if (nextToken == null) {
            previousPosition = position;
            final String tokenText = extractTokenAndMovePointer();
            position = skipSpaces(position);
            nextToken = new Token(findTokenType(tokenText), tokenText);
        }

        return nextToken;
    }

    private int skipSpaces(final int start) {
        int counter = 0;
        while (start + counter < query.length() && query.charAt(start + counter) == ' ') {
            counter++;
        }
        return start + counter;
    }

    private String extractTokenAndMovePointer() {
        switch (query.charAt(position)) {
            case '(':
            case ')':
                position++;
                return query.substring(position - 1, position);
            case '"':
                final int endQuotedPosition = positionNextQuote(position + 1);
                final String quoted = query.substring(position + 1, endQuotedPosition);
                position = endQuotedPosition + 1;
                return quoted;
            default:
                final int endTextPosition = positionAfterText(position);
                final String text = query.substring(position, endTextPosition);
                position = endTextPosition;
                return text;
        }
    }

    private int positionAfterText(final int startPosition) {
        int current = startPosition;
        while (current < query.length()) {
            if (RESERVED_CHARS.contains(query.charAt(current))) {
                return current;
            }

            current++;
        }

        return current;
    }

    private int positionNextQuote(final int startPosition) {
        int current = startPosition;
        while (current < query.length()) {
            if (query.charAt(current) == '"') {
                return current;
            }
            current++;
        }

        throw new MalformedQueryException("Unbalanced quotes");
    }

    private TokenType findTokenType(final String token) {
        switch (token) {
            case "(": return TokenType.OPEN_PARENTHESIS;
            case ")": return TokenType.CLOSE_PARENTHESIS;
            case "AND": return TokenType.AND;
            case "OR": return TokenType.OR;
            case "NOT": return TokenType.NOT;
            default: return TokenType.TEXT;
        }
    }
}
