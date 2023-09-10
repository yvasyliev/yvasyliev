package actions.github.service;

import actions.github.model.SudokuGame;
import actions.github.utils.SudokuUtil;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;

public class MarkdownSudokuGameSupplier implements Supplier<SudokuGame> {
    @Override
    public SudokuGame get() {
        try {
            var markdown = Jsoup.parse(new File("README.md"));

            var grid = new int[SudokuUtil.GRID_SIZE][SudokuUtil.GRID_SIZE];
            var emptyCells = new HashSet<List<Integer>>();
            for (var i = 0; i < SudokuUtil.GRID_SIZE; i++) {
                for (var j = 0; j < SudokuUtil.GRID_SIZE; j++) {
                    var cell = markdown.selectFirst("tr." + i + " > td." + j);
                    if (cell != null) {
                        if (cell.hasText()) {
                            grid[i][j] = Integer.parseInt(cell.text());
                        } else {
                            grid[i][j] = 0;
                            emptyCells.add(List.of(i, j));
                        }
                    }
                }
            }

            return new SudokuGame(grid, emptyCells);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
