package nl.suriani.csvtdd.parser;

import nl.suriani.csvtdd.model.*;
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
        // oepsie, this doesn't work... (not implemented yet)
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
    void twoColumnsHeaderr_emptyBody_ok() {
        var text = """
                column1,column2""";

        var csv = parser.parse(text);

        assertHeaderLooksLikeThis(csv, "column1", "column2");
        assertTrue(csv.body().isEmpty());
    }

    @Test
    void twoColumnsHeader_eenLineBody_ok() {
        var text = """
                naam,achternaam
                piet,suriani""";

        var csv = parser.parse(text);

        assertHeaderLooksLikeThis(csv, "column1", "column2");
        assertFalse(csv.body().isEmpty());
    }

    @Test
    void csvDeserialiserenNaarObject() {
        var text = """
            street,houseNumber,houseNumberSuffix,zipCode,location
            straatlaan,1,,10123,Lelijkstad
            laanpad,10,A,213AS4,Amsterdaamseveen""";

        Function<Row, Address> mapper = row ->
                new Address(row.valueOf("street"),
                        row.valueOf("houseNumber"),
                        row.valueOf("houseNumberSuffix"),
                        row.valueOf("zipCode"),
                        row.valueOf("location"));

        var csv = parser.parse(text);

        assertHeaderLooksLikeThis(csv, "street", "houseNumber", "houseNumberSuffix", "zipCode", "location");
        var address = csv.map(mapper);

        System.out.println(address);
    }

    @Test
    void csvDeserialiserenNaarObjectMetLegeRegel() {
        var text = """
            street,houseNumber,houseNumberSuffix,zipCode,location
            straatlaan,1,,10123,Lelijkstad
            
            laanpad,10,A,213AS4,Amsterdaamseveen""";

        Function<Row, Address> mapper = row ->
                new Address(row.valueOf("street"),
                        row.valueOf("houseNumber"),
                        row.valueOf("houseNumberSuffix"),
                        row.valueOf("zipCode"),
                        row.valueOf("location"));

        var csv = parser.parse(text);

        assertHeaderLooksLikeThis(csv, "street", "houseNumber", "houseNumberSuffix", "zipCode", "location");
        var address = csv.map(mapper);

        System.out.println(address);
    }

    @Test
    void csvDeserialiserenRegelMet1Spatie() {
        var text = """
            street,houseNumber,houseNumberSuffix,zipCode,location
            ,,,,
            laanpad,10,A,213AS4,Amsterdaamseveen""";

        Function<Row, Address> mapper = row ->
                new Address(row.valueOf("street"),
                        row.valueOf("houseNumber"),
                        row.valueOf("houseNumberSuffix"),
                        row.valueOf("zipCode"),
                        row.valueOf("location"));

        var csv = parser.parse(text);

        assertHeaderLooksLikeThis(csv, "street", "houseNumber", "houseNumberSuffix", "zipCode", "location");
        var address = csv.map(mapper);

        System.out.println(address);
    }

    @Test
    void csvDeserialiserenRegelNietPassendeKolommen() {
        var text = """
            street,houseNumber,houseNumberSuffix,zipCode,location
            ,,,
            laanpad,10,A,213AS4,Amsterdaamseveen""";

        Function<Row, Address> mapper = row ->
                new Address(row.valueOf("street"),
                        row.valueOf("houseNumber"),
                        row.valueOf("houseNumberSuffix"),
                        row.valueOf("zipCode"),
                        row.valueOf("location"));

        assertThrows(InvalidNumberOfColumnsException.class, () -> {
            parser.parse(text);
        });
    }


    @Test
    void csvDeserialiserenLegeHeaderCell() {
        var text = """
            street,,houseNumberSuffix,zipCode,location
            laanpad,10,A,213AS4,Amsterdaamseveen""";

        Function<Row, Address> mapper = row ->
                new Address(row.valueOf("street"),
                        row.valueOf("houseNumber"),
                        row.valueOf("houseNumberSuffix"),
                        row.valueOf("zipCode"),
                        row.valueOf("location"));

        assertThrows(InvalidHeaderException.class, () -> {
            parser.parse(text);
        });
    }

    private void assertHeaderLooksLikeThis(CSV csv, String... columns) {
        assertEquals(columns.length, csv.header().row().size());
    }
}