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
    public CompiledTemplate compile(final char[] document, final int templateStart, final int templateLength,
            final ParserConfiguration parserConfiguration) {
        return new TextCompiler(document, templateStart, templateLength, expressionCompiler, parserConfiguration)
                .compile();
    }

    @Override
    public CompiledTemplate compile(final String template, final ParserConfiguration parserConfiguration) {
        return this.compile(template.toCharArray(), 0, template.length(), parserConfiguration);
    }
}
