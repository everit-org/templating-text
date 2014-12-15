package org.everit.templating.text;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import org.everit.expression.ExpressionCompiler;
import org.everit.expression.ParserConfiguration;
import org.everit.expression.mvel.MvelExpressionCompiler;
import org.everit.templating.CompiledTemplate;
import org.junit.Test;

public class TextTemplatingTest {

    @Test
    public void testTemplating() {
        ExpressionCompiler expressionCompiler = new MvelExpressionCompiler();
        TextTemplateCompiler compiler = new TextTemplateCompiler(expressionCompiler);

        ParserConfiguration parserConfiguration = new ParserConfiguration(this.getClass().getClassLoader());
        parserConfiguration.setColumn(10);
        parserConfiguration.setLineNumber(100);

        CompiledTemplate compiledTemplate = compiler
                .compile(
                        "@{'a'.charAt(0)@foreach{index : ({1, 3aa, 2})}@{index}. haha@end{', '}",
                        parserConfiguration);

        OutputStreamWriter writer = new OutputStreamWriter(System.out);
        compiledTemplate.render(writer, new HashMap<String, Object>());
        try {
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
