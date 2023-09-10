package actions.github.service;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Consumer;

public class ReadmeConsumer implements Consumer<int[][]> {
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
    public void accept(int[][] grid) {
        context.setVariable("grid", grid);

        try (var writer = new FileWriter("README.md")) {
            templateEngine.process("README", context, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
