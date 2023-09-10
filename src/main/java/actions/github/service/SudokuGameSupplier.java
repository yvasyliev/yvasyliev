package actions.github.service;

import actions.github.model.SudokuGame;
import actions.github.utils.SudokuUtil;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

public class SudokuGameSupplier implements Supplier<SudokuGame> {
    @Override
    public SudokuGame get() {
        var grid = new int[SudokuUtil.GRID_SIZE][SudokuUtil.GRID_SIZE];

        for (var i = 0; i < grid.length; i++) {
            do {
                grid[i] = SudokuUtil.RANDOM_ROW_SUPPLIER.get();
            } while (!SudokuUtil.isRowValid(grid, i));
        }

        var removedCells = new HashSet<List<Integer>>();
        var random = ThreadLocalRandom.current();
        do {
            removedCells.add(List.of(
                    random.nextInt(0, SudokuUtil.GRID_SIZE),
                    random.nextInt(0, SudokuUtil.GRID_SIZE)
            ));
        } while (removedCells.size() < 5); // TODO: 9/9/2023 replace with System.getEnv

        removedCells.forEach(removedCell -> grid[removedCell.get(0)][removedCell.get(1)] = 0);

        return new SudokuGame(grid, removedCells);
    }
}
