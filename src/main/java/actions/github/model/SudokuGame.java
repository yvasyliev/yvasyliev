package actions.github.model;

import actions.github.utils.SudokuUtil;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

public record SudokuGame(int[][] grid, List<Move> recentMoves, Set<Map.Entry<String, Integer>> leaderboard) {
    public List<Cell> availableMoves() {
        return IntStream
                .range(0, SudokuUtil.GRID_SIZE)
                .mapToObj(columnNumber -> IntStream
                        .range(0, SudokuUtil.GRID_SIZE)
                        .mapToObj(rowNumber -> new Cell(
                                rowNumber,
                                columnNumber,
                                grid[rowNumber][columnNumber]
                        ))
                        .filter(move -> move.value() == 0)
                )
                .flatMap(row -> row)
                .toList();

    }

    public boolean isSolved() {
        return availableMoves().isEmpty();
    }

    public boolean record(Move move) {
        var cell = move.cell();
        if (grid[cell.rowNumber()][cell.columnNumber()] != 0) {
            return false;
        }

        grid[cell.rowNumber()][cell.columnNumber()] = cell.value();
        if (!SudokuUtil.isCellValid(grid, cell.rowNumber(), cell.columnNumber())) {
            grid[cell.rowNumber()][cell.columnNumber()] = 0;
            return false;
        }

        recentMoves.add(0, move);

        var newRecord = new AbstractMap.SimpleEntry<>(move.who(), availableMoves().size() + 1);
        for (var iterator = leaderboard.iterator(); iterator.hasNext(); ) {
            var record = iterator.next();
            if (record.getKey().equals(move.who())) {
                newRecord.setValue(record.getValue() + newRecord.getValue());
                iterator.remove();
                break;
            }
        }
        leaderboard.add(newRecord);

        return true;
    }
}
