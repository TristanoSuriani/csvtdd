package nl.suriani.csvtdd.parser;

import nl.suriani.csvtdd.model.MissingHeaderException;
import nl.suriani.csvtdd.model.ParseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {
    private final Parser parser = new Parser();

    @Test
    void nullCsv_boom() {
        assertThrows(ParseException.class, () -> parser.parse(null));
        // oepsie, this doesn't work... (not implemented yet)
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
        System.out.println(csv);

        assertEquals(1, csv.header().row().size());
        assertEquals("column1", csv.header().row().get(0).value());
        assertTrue(csv.body().isEmpty());
    }
}