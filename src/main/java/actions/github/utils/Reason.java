package actions.github.utils;

import java.util.function.BiFunction;
import java.util.function.Function;

public class Reason {
    private static final String RETURN_URL = System.getenv("RETURN_URL");
    public static final String NO_ARGS = "🙅 Cannot perform action without args.";
    public static final Function<String[], String> NOT_ENOUGH_ARG = args -> "🙅 Not enough args for the command: `%s`".formatted(String.join(" ", args));
    public static final Function<String, String> INVALID_CELL_ID = "🙅 CellId is not valid: `%s`"::formatted;
    public static final Function<String, String> INVALID_CELL_VALUE = "🙅 Cell value is not valid: `%s`"::formatted;
    public static final BiFunction<String, Integer, String> INCORRECT_MOVE = (cellId, cellValue) -> "🙅 `%s -> %d` - is an incorrect move. Try different one: %s".formatted(cellId, cellValue, RETURN_URL);
    public static final BiFunction<String, Integer, String> CELL_FILLED = (cellId, cellValue) -> "✍️ `%s -> %d` - cell is filled. Try to fill the others: %s".formatted(cellId, cellValue, RETURN_URL);
    public static final String SUDOKU_SOLVED = "🎉 Congratulations! You solved the sudoku. Try to solve the new one: %s<br/><br/>PS: If you liked it, please star the repo and share with your friends 🙄👉👈".formatted(RETURN_URL);
    public static final String SUDOKU_CREATED = "✔️ New sudoku has been created: %s".formatted(RETURN_URL);
}
