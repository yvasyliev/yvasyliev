package actions.github.service.reader;

import actions.github.exceptions.SudokuGameException;
import actions.github.model.Cell;
import actions.github.model.Move;
import actions.github.model.SudokuGame;
import actions.github.utils.SudokuStatus;
import actions.github.utils.SudokuUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

public class MarkdownSudokuGameReader implements SudokuGameReader {
    private static final String CELL_SELECTOR = "table.grid tr.%d td.%d";
    private static final Comparator<Map.Entry<String, Integer>> RECORD_COMPARATOR =
            Collections
                    .reverseOrder(
                            Comparator.comparingInt((ToIntFunction<Map.Entry<String, Integer>>) Map.Entry::getValue)
                    )
                    .thenComparing(Map.Entry::getKey);
    private final File source;

    public MarkdownSudokuGameReader(File source) {
        this.source = source;
    }

    public MarkdownSudokuGameReader(String source) {
        this(new File(source));
    }

    @Override
    public SudokuGame read() throws SudokuGameException {
        try {
            var markdown = Jsoup.parse(source);
            return new SudokuGame(readGrid(markdown), readRecentMoves(markdown), readLeaderboard(markdown));
        } catch (IOException e) {
            throw new SudokuGameException(SudokuStatus.FAILED, e.getMessage());
        }
    }

    private int[][] readGrid(Document markdown) {
        var grid = new int[SudokuUtil.GRID_SIZE][SudokuUtil.GRID_SIZE];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                var cellElement = markdown.selectFirst(CELL_SELECTOR.formatted(i, j));
                if (cellElement != null && cellElement.hasText()) {
                    grid[i][j] = Integer.parseInt(cellElement.text());
                }
            }
        }
        return grid;
    }

    private List<Move> readRecentMoves(Document markdown) {
        return markdown
                .select("table.recentMoves .move")
                .stream()
                .map(move -> {
                    var cellElement = move.selectFirst(".cell");
                    if (cellElement == null || !cellElement.hasText()) {
                        return null;
                    }

                    var cell = cellElement.text().split(" -> ");
                    if (cell.length != 2) {
                        return null;
                    }

                    var rowNumber = SudokuUtil.parseRowNumber(cell[0]);
                    var columnNumber = SudokuUtil.parseColumnNumber(cell[0]);
                    var cellValue = SudokuUtil.parseCellValue(cell[1]);
                    if (rowNumber == -1 || columnNumber == -1 || cellValue == -1) {
                        return null;
                    }

                    var whoElement = move.selectFirst(".who");
                    if (whoElement == null || !whoElement.hasText()) {
                        return null;
                    }

                    var who = whoElement.text();

                    return new Move(new Cell(rowNumber, columnNumber, cellValue), who);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private Set<Map.Entry<String, Integer>> readLeaderboard(Document markdown) {
        return markdown
                .select("table.leaderboard tr.record")
                .stream()
                .map(record -> {
                    var whoElement = record.selectFirst("td.who");
                    if (whoElement == null || !whoElement.hasText()) {
                        return null;
                    }

                    var who = whoElement.text();

                    var scoreElement = record.selectFirst("td.score");
                    if (scoreElement == null || !scoreElement.hasText()) {
                        return null;
                    }

                    var score = Integer.parseInt(scoreElement.text());
                    return new AbstractMap.SimpleEntry<>(who, score);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(() -> new TreeSet<>(RECORD_COMPARATOR)));
    }
}
