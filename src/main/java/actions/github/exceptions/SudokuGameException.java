package actions.github.exceptions;

import actions.github.utils.SudokuStatus;

public class SudokuGameException extends Exception {
    private final SudokuStatus status;

    public SudokuGameException(SudokuStatus status, String message) {
        super(message);
        this.status = status;
    }

    public SudokuStatus getStatus() {
        return status;
    }
}
