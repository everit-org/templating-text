package org.everit.templating.text.internal;

import org.everit.expression.ExpressionCompiler;
import org.everit.expression.ParserConfiguration;

public class CompilableNodeHelper {

    private final ExpressionCompiler expressionCompiler;

    private final int line;

    private ParserConfiguration originalConfig;

    public CompilableNodeHelper(final ParserConfiguration originalConfig, final ExpressionCompiler expressionCompiler,
            final int line) {
        this.originalConfig = originalConfig;
        this.expressionCompiler = expressionCompiler;
        this.line = line;
    }

    public ParserConfiguration generateParserConfiguration(final int column) {
        ParserConfiguration result = new ParserConfiguration(originalConfig);
        result.setLineNumber(line);
        if (line == 1) {
            result.setColumn(originalConfig.getColumn() + column);
        } else {
            result.setColumn(column);
        }
        return result;
    }

    public ExpressionCompiler getExpressionCompiler() {
        return expressionCompiler;
    }

}
