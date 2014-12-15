package org.everit.templating.text.internal;

import org.everit.expression.CompiledExpression;
import org.everit.expression.ExpressionCompiler;
import org.everit.expression.ParserConfiguration;

public class CompilableNodeHelper {

    private final ExpressionCompiler expressionCompiler;

    private final int line;

    private final int lineStart;

    private final ParserConfiguration originalConfig;

    public CompilableNodeHelper(final ParserConfiguration originalConfig, final ExpressionCompiler expressionCompiler,
            final int line, final int lineStart) {
        this.originalConfig = originalConfig;
        this.expressionCompiler = expressionCompiler;
        this.line = line;
        this.lineStart = lineStart;
    }

    public CompiledExpression compileExpression(final char[] template, final int start, final int length) {
        ParserConfiguration parserConfiguration = new ParserConfiguration(originalConfig);

        parserConfiguration.setLineNumber(line);
        parserConfiguration.setColumn(start - lineStart + 1);

        String expression = String.valueOf(template, start, length);

        return expressionCompiler.compile(expression, parserConfiguration);
    }

    public int getLine() {
        return line;
    }

    public int getLineStart() {
        return lineStart;
    }
}
