package nl.suriani.csvtdd.parser;

import nl.suriani.csvtdd.model.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Parser {
    public CSV parse(String text) {
        if (text == null) {
            throw new ParseException("text cannot be null");
        }

        if (text.trim().isEmpty()) {
            throw new MissingHeaderException();
        }

        var lines = splitTextInLines(text);
        var headerLine = lines.get(0);
        var header = headerLineToHeader(headerLine);
        var body = new Body(List.of());
        return new CSV(header, body);
    }

    private List<String> splitTextInLines(String text) {
        return splitAndTrim(text, "//R");
    }

    private List<String> splitLineInCells(String text) {
        return splitAndTrim(text, ",");
    }

    private static List<String> splitAndTrim(String text, String regex) {
        return Arrays.stream(text.split(regex))
                .map(String::trim)
                .toList();
    }

    private Header headerLineToHeader(String headerLine) {
        var stringCells = splitLineInCells(headerLine);
        var cells = headerStringCellsToCells(stringCells);
        var row = new Row(cells);
        return new Header(row);
    }

    private static List<Cell> headerStringCellsToCells(List<String> stringCells) {
        return Stream.iterate(0, i -> i + 1)
                .limit(stringCells.size())
                .map(i -> new Cell(stringCells.get(i), new ColumnInfo(stringCells.get(i), i)))
                .toList();
    }
}
