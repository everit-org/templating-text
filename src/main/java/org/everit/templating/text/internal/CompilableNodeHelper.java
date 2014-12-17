/**
 * This file is part of Everit - Templating Text.
 *
 * Everit - Templating Text is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Everit - Templating Text is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Everit - Templating Text.  If not, see <http://www.gnu.org/licenses/>.
 */
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

        parserConfiguration.setStartRow(line);
        parserConfiguration.setStartColumn(start - lineStart + 1);

        String expression = String.valueOf(template, start, length);

        return expressionCompiler.compile(expression, parserConfiguration);
    }

    public ExpressionCompiler getExpressionCompiler() {
        return expressionCompiler;
    }

    public int getLine() {
        return line;
    }

    public int getLineStart() {
        return lineStart;
    }

    public ParserConfiguration getOriginalConfig() {
        return originalConfig;
    }
}
