package org.everit.templating.text;

import org.everit.expression.ExpressionCompiler;
import org.everit.expression.ParserConfiguration;
import org.everit.templating.CompiledTemplate;
import org.everit.templating.TemplateCompiler;
import org.everit.templating.text.internal.TextCompiler;

public class TextTemplateCompiler implements TemplateCompiler {

    private final ExpressionCompiler expressionCompiler;

    public TextTemplateCompiler(final ExpressionCompiler expressionCompiler) {
        this.expressionCompiler = expressionCompiler;
    }

    @Override
    public CompiledTemplate compile(final String template, final ParserConfiguration parserConfiguration) {
        return new TextCompiler(template, expressionCompiler, parserConfiguration).compile();
    }

}
