package actions.github.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SudokuUtil {
    public static final int GRID_SIZE = 9;
    public static final int SUBGRID_SIZE = 3;
    public static final int CELL_MIN_VALUE = 1;
    public static final int CELL_MAX_VALUE = GRID_SIZE;
    public static final String ROW_NAMES = "123456789";
    public static final String COL_NAMES = "ABCDEFGHI";

    private static final List<Integer> INITIAL_ROW = IntStream.rangeClosed(CELL_MIN_VALUE, CELL_MAX_VALUE)
            .boxed()
            .collect(Collectors.toCollection(ArrayList::new));

    public static final Supplier<int[]> RANDOM_ROW_SUPPLIER = () -> {
        Collections.shuffle(INITIAL_ROW);
        return INITIAL_ROW.stream().mapToInt(Integer::intValue).toArray();
    };

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

    public static boolean isRowValid(int[][] grid, int rowNumber) {
        for (var j = 0; j < grid[rowNumber].length; j++) {
            if (!isCellValid(grid, rowNumber, j)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isCellValid(int[][] grid, int rowNumber, int colNumber) {
        return !hasDuplicates(grid[rowNumber])
                && !hasDuplicates(getColumn(grid, colNumber))
                && !hasDuplicates(getSubgrid(grid, rowNumber, colNumber));
    }

    public static boolean isInputCellValid(String cell) {
        return Pattern.compile("[a-iA-I][1-9]")
                .matcher(cell)
                .matches();
    }

    public static boolean isInputValueValid(String cellValue) {
        return Pattern.compile("[1-9]")
                .matcher(cellValue)
                .matches();
    }
}
