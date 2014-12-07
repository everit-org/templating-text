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

        CompiledTemplate compiledTemplate = compiler.compile("hahaha@{'hehe'}@code{a=1}@{\naxx}",
                new ParserConfiguration(
                        this.getClass().getClassLoader()));

        OutputStreamWriter writer = new OutputStreamWriter(System.out);
        compiledTemplate.render(writer, new HashMap<String, Object>());
        try {
            writer.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
