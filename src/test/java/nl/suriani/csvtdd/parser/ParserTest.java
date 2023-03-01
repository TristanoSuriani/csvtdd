package nl.suriani.csvtdd.parser;

import nl.suriani.csvtdd.model.CSV;
import nl.suriani.csvtdd.model.MissingHeaderException;
import nl.suriani.csvtdd.model.ParseException;
import nl.suriani.csvtdd.model.Row;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {
    private final Parser parser = new Parser();

    @Test
    void nullCsv_boom() {
        assertThrows(ParseException.class, () -> parser.parse(null));
    }

    @Test
    void empty_noHeader_boom() {
        assertThrows(MissingHeaderException.class, () -> parser.parse(""));
    }

    @Test
    void oneColumnHeader_emptyBody_ok() {
        var text = """
                column1""";

        var csv = parser.parse(text);

        assertHeaderLooksLikeThis(csv, "column1");
        assertTrue(csv.body().isEmpty());
    }

    @Test
    void twoColumnsHeader_emptyBody_ok() {
        var text = """
                column1, column2""";

        var csv = parser.parse(text);

        assertHeaderLooksLikeThis(csv, "column1", "column2");

        assertTrue(csv.body().isEmpty());
    }

    @Test
    void headerWithEmptyField1_emptyBody_boom() {
        var text = """
                , column2""";

        assertThrows(ParseException.class, () -> parser.parse(text));
    }

    @Test
    void headerWithEmptyField2_emptyBody_boom() {
        var text = """
                column1,""";

        assertThrows(ParseException.class, () -> parser.parse(text));
    }

    @Test
    void oneColumnCsv_oneRowBody_ok() {
        var text = """
                col1,col2
                bla,blue""";

        var csv = parser.parse(text);

        assertHeaderLooksLikeThis(csv, "col1", "col2");

        assertFalse(csv.body().isEmpty());
        assertEquals("bla", csv.body().rows().get(0).get(0).value());
        assertEquals("blue", csv.body().rows().get(0).get(1).value());
    }

    @Test
    void addressCsv_ok() {
        var text = """
            street,houseNumber,houseNumberSuffix,zipCode,location
            straatlaan,1,,10123,Lelijkstad
            laanpad,10,A,213AS4,Amsterdaamseveen""";

        var csv = parser.parse(text);

        assertHeaderLooksLikeThis(csv, "street", "houseNumber", "houseNumberSuffix", "zipCode", "location");

        Function<Row, Address> mapper = row ->
                new Address(row.valueOf("street"),
                        row.valueOf("houseNumber"),
                        row.valueOf("houseNumberSuffix"),
                        row.valueOf("zipCode"),
                        row.valueOf("location"));

        var addresses = csv.map(mapper);
        System.out.println(addresses);
    }

    private void assertHeaderLooksLikeThis(CSV csv, String... columns) {
        assertEquals(columns.length, csv.header().row().size());
    }


}