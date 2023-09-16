package actions.github;

import actions.github.exceptions.SudokuGameException;
import actions.github.model.Cell;
import actions.github.model.Move;
import actions.github.model.SudokuGame;
import actions.github.service.reader.MarkdownSudokuGameReader;
import actions.github.service.writer.MarkdownSudokuGameWriter;
import actions.github.utils.Reason;
import actions.github.utils.SudokuStatus;
import actions.github.utils.SudokuUtil;

import java.util.List;

public class Main {
    private static final int EMPTY_CELLS_AMOUNT = Integer.parseInt(System.getenv("EMPTY_CELLS_AMOUNT"));
    private static final String TARGET_FILE = "README.md";

    public static void main(String[] args) {
        try {
            if (args.length == 0) {
                throw new SudokuGameException(SudokuStatus.FAILED, Reason.NO_ARGS);
            }

            switch (args[0].toLowerCase()) {
                case "new" -> {
                    var sudokuGame = new MarkdownSudokuGameReader(TARGET_FILE).read();
                    sudokuGame = new SudokuGame(SudokuUtil.randomUnsolvedGrid(EMPTY_CELLS_AMOUNT), List.of(), sudokuGame.leaderboard());
                    new MarkdownSudokuGameWriter(TARGET_FILE).write(sudokuGame);
                    printResult(SudokuStatus.EXECUTED, Reason.SUDOKU_CREATED);
                }
                case "fill" -> {
                    if (args.length < 4) {
                        throw new SudokuGameException(SudokuStatus.FAILED, Reason.NOT_ENOUGH_ARG.apply(args));
                    }

                    var cellId = args[1].toUpperCase();

                    var rowNumber = SudokuUtil.parseRowNumber(cellId);
                    var columnNumber = SudokuUtil.parseColumnNumber(cellId);
                    if (rowNumber == -1 || columnNumber == -1) {
                        throw new SudokuGameException(SudokuStatus.FAILED, Reason.INVALID_CELL_ID.apply(args[1]));
                    }

                    var cellValue = SudokuUtil.parseCellValue(args[2]);
                    if (cellValue == -1) {
                        throw new SudokuGameException(SudokuStatus.FAILED, Reason.INVALID_CELL_VALUE.apply(args[2]));
                    }

                    var sudokuGame = new MarkdownSudokuGameReader(TARGET_FILE).read();
                    if (!sudokuGame.record(new Move(new Cell(rowNumber, columnNumber, cellValue), args[3]))) {
                        throw new SudokuGameException(SudokuStatus.FAILED, Reason.INCORRECT_MOVE.apply(cellId, cellValue));
                    }
                    var reason = Reason.CELL_FILLED.apply(cellId, cellValue);

                    if (sudokuGame.isSolved()) {
                        sudokuGame = new SudokuGame(SudokuUtil.randomUnsolvedGrid(EMPTY_CELLS_AMOUNT), List.of(), sudokuGame.leaderboard());
                        reason = Reason.SUDOKU_SOLVED;
                    }
                    new MarkdownSudokuGameWriter(TARGET_FILE).write(sudokuGame);
                    printResult(SudokuStatus.EXECUTED, reason);
                }
            }
        } catch (SudokuGameException e) {
            printResult(e.getStatus(), e.getMessage());
        }
    }

    private static void printResult(SudokuStatus status, String reason) {
        System.out.println("status=" + status);
        System.out.println("reason=" + reason);
    }

}
