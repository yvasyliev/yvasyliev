package actions.github.service;

import actions.github.model.SudokuGame;
import actions.github.utils.SudokuUtil;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Consumer;

public class ReadmeConsumer implements Consumer<SudokuGame> {
    private final Context context = new Context();
    private final TemplateEngine templateEngine;

    public ReadmeConsumer() {
        var resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setPrefix("/templates/");
        resolver.setSuffix(".html");

        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);
    }


    @Override
    public void accept(SudokuGame sudokuGame) {
        var nextMoves = sudokuGame.emptyCells
                .stream()
                .map(cell -> String.valueOf(SudokuUtil.COL_NAMES.charAt(cell.get(1))) + SudokuUtil.ROW_NAMES.charAt(cell.get(0)))
                .toList();

        context.setVariable("grid", sudokuGame.grid);
        context.setVariable("nextMoves", nextMoves);

        try (var writer = new FileWriter("README.md")) {
            templateEngine.process("README", context, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
