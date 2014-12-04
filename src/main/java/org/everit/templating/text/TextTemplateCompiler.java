package org.everit.templating.text;

import java.io.Reader;

import org.everit.expression.ExpressionCompiler;
import org.everit.expression.ParserConfiguration;
import org.everit.templating.CompiledTemplate;
import org.everit.templating.TemplateCompiler;

public class TextTemplateCompiler implements TemplateCompiler {

    private final ExpressionCompiler expressionCompiler;

    public TextTemplateCompiler(final ExpressionCompiler expressionCompiler) {
        this.expressionCompiler = expressionCompiler;
    }

    @Override
    public CompiledTemplate compile(final Reader template) {
        return compile(template, null);
    }

    @Override
    public CompiledTemplate compile(final Reader template, final ParserConfiguration parserConfiguration) {
        // TODO Auto-generated method stub
        return null;
    }

}
