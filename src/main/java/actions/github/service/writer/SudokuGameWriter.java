package actions.github.service.writer;

import actions.github.exceptions.SudokuGameException;
import actions.github.model.SudokuGame;

public interface SudokuGameWriter {
    void write(SudokuGame sudokuGame) throws SudokuGameException;
}
