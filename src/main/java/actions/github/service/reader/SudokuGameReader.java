package actions.github.service.reader;

import actions.github.exceptions.SudokuGameException;
import actions.github.model.SudokuGame;

public interface SudokuGameReader {
    SudokuGame read() throws SudokuGameException;
}
