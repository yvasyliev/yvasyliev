package actions.github.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SudokuUtil {
    public static final int GRID_SIZE = 9;
    public static final int SUBGRID_SIZE = 3;
    public static final int CELL_MIN_VALUE = 1;
    public static final int CELL_MAX_VALUE = GRID_SIZE;
    public static final String ROW_NAMES = "123456789";
    public static final String COL_NAMES = "ABCDEFGHI";
    public static final String CELL_VALUES = ROW_NAMES;

    private static final List<Integer> INITIAL_ROW = IntStream.rangeClosed(CELL_MIN_VALUE, CELL_MAX_VALUE)
            .boxed()
            .collect(Collectors.toCollection(ArrayList::new));

    public static int[] randomRow() {
        Collections.shuffle(INITIAL_ROW);
        return INITIAL_ROW.stream().mapToInt(Integer::intValue).toArray();
    }

    public static boolean isCellValid(int[][] grid, int rowNumber, int colNumber) {
        return !hasDuplicates(grid[rowNumber])
                && !hasDuplicates(getColumn(grid, colNumber))
                && !hasDuplicates(getSubgrid(grid, rowNumber, colNumber));
    }

    public static boolean isRowValid(int[][] grid, int rowNumber) {
        for (var j = 0; j < grid[rowNumber].length; j++) {
            if (!isCellValid(grid, rowNumber, j)) {
                return false;
            }
        }
        return true;
    }

    public static int[][] randomGrid() {
        var grid = new int[GRID_SIZE][GRID_SIZE];
        for (var i = 0; i < GRID_SIZE; i++) {
            do {
                grid[i] = randomRow();
            } while (!isRowValid(grid, i));
        }
        return grid;
    }

    public static int[][] randomUnsolvedGrid(int emptyCellsAmount) {
        var grid = randomGrid();
        var random = ThreadLocalRandom.current();
        for (var i = 0; i < emptyCellsAmount; ) {
            var rowNumber = random.nextInt(0, GRID_SIZE);
            var columnNumber = random.nextInt(0, GRID_SIZE);
            if (grid[rowNumber][columnNumber] != 0) {
                grid[rowNumber][columnNumber] = 0;
                i++;
            }
        }
        return grid;
    }

    public static int[] getColumn(int[][] grid, int colNumber) {
        return Arrays.stream(grid)
                .map(row -> row[colNumber])
                .mapToInt(Integer::intValue)
                .toArray();
    }

    public static int[] getSubgrid(int[][] grid, int rowNumber, int colNumber) {
        var subgridValues = IntStream.of();
        var iOffset = (rowNumber / SUBGRID_SIZE) * SUBGRID_SIZE;
        var jOffset = (colNumber / SUBGRID_SIZE) * SUBGRID_SIZE;
        for (var i = 0; i < SUBGRID_SIZE; i++) {
            for (var j = 0; j < SUBGRID_SIZE; j++) {
                subgridValues = IntStream.concat(subgridValues, IntStream.of(grid[i + iOffset][j + jOffset]));
            }
        }
        return subgridValues.toArray();
    }

    public static boolean hasDuplicates(int[] row) {
        var existingValues = new HashSet<Integer>();
        for (var cellValue : row) {
            if (cellValue > 0 && !existingValues.add(cellValue)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isCellIdValid(String cellId) {
        return cellId.length() == 2
                && COL_NAMES.indexOf(cellId.charAt(0)) != -1
                && Character.isDigit(cellId.charAt(1));
    }

    public static boolean isCellValueValid(String cellValue) {
        return cellValue.length() == 1 && CELL_VALUES.indexOf(cellValue.charAt(0)) != -1;
    }

    public static int parseRowNumber(String cellId) {
        return isCellIdValid(cellId) ? ROW_NAMES.indexOf(cellId.charAt(1)) : -1;
    }

    public static int parseColumnNumber(String cellId) {
        return isCellIdValid(cellId) ? COL_NAMES.indexOf(cellId.charAt(0)) : -1;
    }

    public static int parseCellValue(String cellValue) {
        return isCellValueValid(cellValue) ? Integer.parseInt(cellValue) : -1;
    }
}
