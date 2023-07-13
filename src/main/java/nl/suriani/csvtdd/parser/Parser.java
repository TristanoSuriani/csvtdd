package nl.suriani.csvtdd.parser;

import nl.suriani.csvtdd.model.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Parser {
    public CSV parse(String text) {
        // this is the minimum amount of code that passes the first test! Good luck from here!
        if (text == null) {
            throw new ParseException("null mag niet");
        }
        if (text.isEmpty()) {
            throw new MissingHeaderException();
        }

        var lines = splitTextInLines(text);
        var headerLine = lines.get(0);
        var header = headerLineToHeader(headerLine);

        var bodyLines = lines.stream()
                .skip(1)
                .filter(s -> !s.isEmpty())
                .toList();

        var body = bodyLinesToBody(bodyLines, header);
        return new CSV(header, body);
    }

    private List<String> splitTextInLines(String text) {
        return splitAndTrim(text, "\n");
    }

    private List<String> splitLineInCells(String text) {
        return splitAndTrim(text, ",");
    }

    private List<String> splitAndTrim(String text, String regex) {
        return Arrays.stream(text.split(regex, -1))
                .map(String::trim)
                .toList();
    }

    private Header headerLineToHeader(String headerLine) {
        var row = headerLineToRow(headerLine);
        return new Header(row);
    }

    private Row headerLineToRow(String headerLine) {
        List<String> stringCells = splitLineInCells(headerLine);

        if(stringCells.stream().anyMatch(String::isEmpty)) {
            throw new InvalidHeaderException();
        }

        List<Cell> cells = Stream.iterate(0, i -> i + 1)
                .limit(stringCells.size())
                .map(i -> new Cell(stringCells.get(i), new ColumnInfo(stringCells.get(i), i)))
                .toList();


        return new Row(cells);
    }

    private Body bodyLinesToBody(List<String> bodyLines, Header header) {
        var rows = bodyLines.stream()
                .map(bodyLine -> bodyLineToRow(bodyLine, header))
                .toList();

        return new Body(rows);
    }

    private Row bodyLineToRow(String bodyLine, Header header) {
        var stringCells = splitLineInCells(bodyLine);
        var cells = Stream.iterate(0, i -> i + 1)
                .limit(stringCells.size())
                .map(i -> new Cell(stringCells.get(i), new ColumnInfo(header.row().get(i).value(), i)))
                .toList();

        if(cells.size() != header.row().size()) {
            throw new InvalidNumberOfColumnsException();
        }
        return new Row(cells);
    }
}
