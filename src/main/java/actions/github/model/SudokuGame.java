package actions.github.model;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class SudokuGame {
    public final int[][] grid;

    public final Set<List<Integer>> missingCells;

    public SudokuGame(int[][] grid, Set<List<Integer>> missingCells) {
        this.grid = grid;
        this.missingCells = missingCells;
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

        gameStr.append("]\nMissing cells:\n").append(missingCells);
        return gameStr.toString();
    }
}
