package ch.lavanchy.recipes.query;

import org.apache.commons.lang3.StringUtils;

import static ch.lavanchy.recipes.query.TokenType.*;

/**
 * Create the QueryOperation tree based on a query string
 *
 * @since 1.0.2
 */
public class QueryOperationFactory {
    public QueryOperation createQueryOperation(final String query) {
        if (StringUtils.isEmpty(query)) {
            return new EmptyOp();
        } else {
            final QueryTokenizer tokenizer = new QueryTokenizer(query);
            return parse(tokenizer);
        }
    }

    private QueryOperation parse(final QueryTokenizer queryTokenizer) {
        QueryOperation acc = parseBlock(queryTokenizer);

        while (queryTokenizer.hasMoreToken()) {
            switch (queryTokenizer.fetchToken().getTokenType()) {
                case OR:
                    consume(queryTokenizer, OR);
                    acc = new OrOp(acc, parseBlock(queryTokenizer));
                    break;
                case AND:
                    consume(queryTokenizer, AND);
                    acc = new AndOp(acc, parseBlock(queryTokenizer));
                    break;
                case OPEN_PARENTHESIS:
                case TEXT:
                case NOT:
                    acc = new AndOp(acc, parseBlock(queryTokenizer));
                    break;
                case CLOSE_PARENTHESIS:
                    return acc;
            }
        }

        return acc;
    }

    private QueryOperation parseBlock(final QueryTokenizer queryTokenizer) {
        final QueryOperation queryOperation;
        switch (queryTokenizer.fetchToken().getTokenType()) {
            case TEXT:
                queryOperation = parseText(queryTokenizer);
                break;
            case OPEN_PARENTHESIS:
                queryOperation = parseParenthesiedBlock(queryTokenizer);
                break;
            case NOT:
                queryOperation = parseNot(queryTokenizer);
                break;
            default:
                queryOperation = new EmptyOp();
        }
        return queryOperation;
    }

    private QueryOperation parseParenthesiedBlock(final QueryTokenizer queryTokenizer) {
        consume(queryTokenizer, OPEN_PARENTHESIS);
        final QueryOperation queryOperation = parse(queryTokenizer);
        consume(queryTokenizer, CLOSE_PARENTHESIS);
        return queryOperation;
    }

    private QueryOperation parseNot(final QueryTokenizer queryTokenizer) {
        consume(queryTokenizer, NOT);
        final QueryOperation subQuery = parseBlock(queryTokenizer);
        if (subQuery.isEmpty()) {
            throw createMalformedException(queryTokenizer.fetchToken().getTokenType(), NOT, TEXT, OPEN_PARENTHESIS);
        } else {
            return new NotOp(subQuery);
        }
    }

    private QueryOperation parseText(final QueryTokenizer queryTokenizer) {
        final Token token = consume(queryTokenizer, TEXT);
        return new TextFilterOp(token.getTokenText());
    }

    private Token consume(final QueryTokenizer queryTokenizer, final TokenType expected) {
        if (queryTokenizer.hasMoreToken()) {
            final Token received = queryTokenizer.nextToken();
            if (received.getTokenType().equals(expected)) {
                return received;
            } else {
                throw createMalformedException(received.getTokenType(), expected);
            }
        } else {
            throw new MalformedQueryException(String.format("Expected one of %s but none received", expected));
        }
    }

    private MalformedQueryException createMalformedException(final TokenType received, final TokenType... expected) {
        return new MalformedQueryException(String.format("Unexpected token. Expected one of %s received %s", expected, received));
    }
}
