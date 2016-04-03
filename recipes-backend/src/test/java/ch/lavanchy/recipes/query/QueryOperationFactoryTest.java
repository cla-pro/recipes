package ch.lavanchy.recipes.query;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class QueryOperationFactoryTest {
    @Test
    public void testEmptyQuery() {
        assertThat(new QueryOperationFactory().createQueryOperation(null)).isEqualTo(new EmptyOp());
        assertThat(new QueryOperationFactory().createQueryOperation("")).isEqualTo(new EmptyOp());
    }

    @Test
    public void testSingleTextQuery() {
        final QueryOperation queryOperation = new QueryOperationFactory().createQueryOperation("bla");
        assertThat(queryOperation).isEqualTo(new TextFilterOp("bla"));
    }

    @Test
    public void testImplicitAndQuery() {
        final QueryOperation queryOperation = new QueryOperationFactory().createQueryOperation("bla blou");
        assertThat(queryOperation).isEqualTo(
                new AndOp(
                        new TextFilterOp("bla"),
                        new TextFilterOp("blou")));
    }

    @Test
    public void ExplicitAndQuery() {
        final QueryOperation queryOperation = new QueryOperationFactory().createQueryOperation("bla AND blou");
        assertThat(queryOperation).isEqualTo(
                new AndOp(
                        new TextFilterOp("bla"),
                        new TextFilterOp("blou")));
    }

    @Test
    public void testOrQuery() {
        final QueryOperation queryOperation = new QueryOperationFactory().createQueryOperation("bla OR blou");
        assertThat(queryOperation).isEqualTo(
                new OrOp(
                        new TextFilterOp("bla"),
                        new TextFilterOp("blou")));
    }

    @Test
    public void testNotQuery() {
        final QueryOperation queryOperation = new QueryOperationFactory().createQueryOperation("NOT bla");
        assertThat(queryOperation).isEqualTo(new NotOp(new TextFilterOp("bla")));
    }

    @Test
    public void testParenthesiedSingleFilterQuery() {
        final QueryOperation queryOperation = new QueryOperationFactory().createQueryOperation("(bla)");
        assertThat(queryOperation).isEqualTo(new TextFilterOp("bla"));
    }

    @Test
    public void testParenthesiedImplicitAndQuery() {
        final QueryOperation queryOperation = new QueryOperationFactory().createQueryOperation("(bla blou)");
        assertThat(queryOperation).isEqualTo(
                new AndOp(
                        new TextFilterOp("bla"),
                        new TextFilterOp("blou")));
    }

    @Test
    public void testComplex1() {
        final QueryOperation queryOperation = new QueryOperationFactory().createQueryOperation("NOT (bla blou) bli");
        assertThat(queryOperation).isEqualTo(
                new AndOp(
                        new NotOp(
                                new AndOp(
                                        new TextFilterOp("bla"),
                                        new TextFilterOp("blou"))),
                        new TextFilterOp("bli")));
    }

    @Test
    public void testComplex2() {
        final QueryOperation queryOperation = new QueryOperationFactory().createQueryOperation(" (bla blou) (bli blu)");
        assertThat(queryOperation).isEqualTo(
                new AndOp(
                        new AndOp(
                                new TextFilterOp("bla"),
                                new TextFilterOp("blou")),

                        new AndOp(
                                new TextFilterOp("bli"),
                                new TextFilterOp("blu"))));
    }
}