package nl.suriani.csvtdd.parser;

import nl.suriani.csvtdd.model.*;

import java.util.Arrays;
import java.util.List;

public class Parser {
    public CSV parse(String text) {
        // this is the minimum amount of code that passes the first test! Good luck from here!
        var header = new Header(new Row(List.of(new Cell("column1", new ColumnInfo("column1", 0)))));
        var body = new Body(List.of());
        return new CSV(header, body);
    }

    private List<String> splitTextInLines(String text) {
        return Arrays.asList(text.split("\\R"));
    }

    private List<String> splitLineInCells(String text) {
        return Arrays.asList(text.split(","));
    }
}
