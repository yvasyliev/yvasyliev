package actions.github;

import actions.github.service.MarkdownSudokuGameSupplier;
import actions.github.service.ReadmeConsumer;
import actions.github.service.SudokuGameSupplier;
import actions.github.utils.SudokuStatus;
import actions.github.utils.SudokuUtil;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            printResult(SudokuStatus.SKIPPED, "No args passed.");
        }

        switch (args[0]) {
            case "fill" -> {
                if (args.length < 3) {
                    printResult(SudokuStatus.SKIPPED, "Not enough args.");
                } else {
                    fillCell(args[1], args[2]);
                }
            }
            case "new" -> {
                new ReadmeConsumer().accept(new SudokuGameSupplier().get().grid);
                printResult(SudokuStatus.EXECUTED, "New sudoku is waiting for you: https://github.com/yvasyliev");
            }
            default -> printResult(SudokuStatus.SKIPPED, "Unknown command: " + args[0]);
        }
    }

    private static void printResult(SudokuStatus status, String reason) {
        System.out.println("status=" + status);
        System.out.println("reason=" + reason);
    }

    private static void fillCell(String cell, String value) {
        if (!SudokuUtil.isInputCellValid(cell)) {
            printResult(SudokuStatus.SKIPPED, "Cell should match [0-9][a-zA-Z]: " + cell);
            return;
        }

        if (!SudokuUtil.isInputValueValid(value)) {
            printResult(SudokuStatus.SKIPPED, "Cell value should match [0-9]: " + value);
            return;
        }

        var row = SudokuUtil.ROW_NAMES.indexOf(cell.charAt(0));
        var col = SudokuUtil.COL_NAMES.indexOf(cell.charAt(1));
        var cellValue = Integer.parseInt(value);

        var sudokuGame = new MarkdownSudokuGameSupplier().get();
        sudokuGame.grid[row][col] = cellValue;

        if (!SudokuUtil.isCellValid(sudokuGame.grid, row, col)) {
            printResult(SudokuStatus.FAILED, cellValue + " is incorrect value. Try different value: https://github.com/yvasyliev");
            return;
        }

        if (sudokuGame.missingCells.isEmpty()) {
            sudokuGame = new SudokuGameSupplier().get();
            printResult(SudokuStatus.EXECUTED, "Congratulations! You've just solved a sudoku. Try a new one: https://github.com/yvasyliev");
        }

        new ReadmeConsumer().accept(sudokuGame.grid);
    }
}
