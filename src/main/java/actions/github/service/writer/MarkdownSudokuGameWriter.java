package actions.github.service.writer;

import actions.github.exceptions.SudokuGameException;
import actions.github.model.SudokuGame;
import actions.github.utils.SudokuStatus;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class MarkdownSudokuGameWriter implements SudokuGameWriter {
    private final Context context = new Context();
    private final TemplateEngine templateEngine;
    private final String destination;

    public MarkdownSudokuGameWriter(String destination) {
        this.destination = destination;

        var resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        resolver.setPrefix("/templates/");
        resolver.setSuffix(".html");

        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);
    }

    @Override
    public void write(SudokuGame sudokuGame) throws SudokuGameException {
        context.setVariable("sudokuGame", sudokuGame);

        var readme = templateEngine
                .process("index", context)
                .lines()
                .filter(line -> !line.isBlank())
                .collect(Collectors.joining("\n"));

        try (var writer = new FileWriter(destination)) {
            writer.write(readme);
            writer.flush();
        } catch (IOException e) {
            throw new SudokuGameException(SudokuStatus.FAILED, e.getMessage());
        }
    }
}
