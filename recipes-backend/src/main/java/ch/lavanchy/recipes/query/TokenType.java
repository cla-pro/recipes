package ch.lavanchy.recipes.query;

/**
 * Different type of tokens which can be given back by the tokenizer.
 *
 * @since 1.0.2
 */
enum TokenType {
    OPEN_PARENTHESIS,
    CLOSE_PARENTHESIS,
    TEXT,
    AND,
    OR,
    NOT
}
