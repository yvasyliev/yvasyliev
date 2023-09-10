package actions.github.model;

import java.util.Arrays;
import java.util.List;

public class SudokuGame {
    public final int[][] grid;

    public final List<List<Integer>> emptyCells;

    public SudokuGame(int[][] grid, List<List<Integer>> emptyCells) {
        this.grid = grid;
        this.emptyCells = emptyCells;
    }

    @Override
    public String toString() {
        var gameStr = new StringBuilder("Grid:\n[");

        for (int i = 0; i < grid.length; i++) {
            if (i > 0) {
                gameStr.append(" ");
            }
            gameStr.append(Arrays.toString(grid[i]));
            if (i + 1 < grid.length) {
                gameStr.append(",\n");
            }
        }

        gameStr.append("]\nMissing cells:\n").append(emptyCells);
        return gameStr.toString();
    }
}
