package ch.lavanchy.recipes.query;

import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * Single token which contains a type and its textual representation.
 *
 * @since 1.0.2
 */
class Token {
    private final TokenType tokenType;
    private final String tokenText;

    public Token(final TokenType tokenType, final String tokenText) {
        this.tokenType = tokenType;
        this.tokenText = tokenText;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getTokenText() {
        return tokenText;
    }

    @Override
    public boolean equals(final Object other) {
        if (other == null || !(other instanceof Token)) {
            return false;
        } else if (other == this) {
            return true;
        }

        final Token that = (Token) other;
        return new EqualsBuilder()
                .append(tokenType, that.tokenType)
                .append(tokenText, that.tokenText)
                .build();
    }

    @Override
    public String toString() {
        return String.format("Token[type=%s, text=%s]", tokenType, tokenText);
    }
}
