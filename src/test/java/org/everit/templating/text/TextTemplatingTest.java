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

        CompiledTemplate compiledTemplate = compiler
                .compile(
                        "\n\n  @foreach{index : ({1, 3, 2})}@{index}. haha@end{aa + ', \n'}",
                        new ParserConfiguration(this.getClass().getClassLoader()));

        OutputStreamWriter writer = new OutputStreamWriter(System.out);
        compiledTemplate.render(writer, new HashMap<String, Object>());
        try {
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
