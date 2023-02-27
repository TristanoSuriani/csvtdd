package nl.suriani.csvtdd.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserTest {
    private final Parser parser = new Parser();

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