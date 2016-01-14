/*
 * Copyright (C) 2011 Everit Kft. (http://www.everit.biz)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.everit.templating.text;

import org.everit.expression.ExpressionCompiler;
import org.everit.expression.ParserConfiguration;
import org.everit.templating.CompiledTemplate;
import org.everit.templating.TemplateCompiler;
import org.everit.templating.text.internal.TextCompiler;

/**
 * Compiler for text based templates.
 */
public class TextTemplateCompiler implements TemplateCompiler {

  private final ExpressionCompiler expressionCompiler;

  public TextTemplateCompiler(final ExpressionCompiler expressionCompiler) {
    this.expressionCompiler = expressionCompiler;
  }

  @Override
  public CompiledTemplate compile(final char[] document, final int templateStart,
      final int templateLength,
      final ParserConfiguration parserConfiguration) {
    return new TextCompiler(document, templateStart, templateLength, expressionCompiler,
        parserConfiguration)
            .compile();
  }

  @Override
  public CompiledTemplate compile(final String template,
      final ParserConfiguration parserConfiguration) {
    return this.compile(template.toCharArray(), 0, template.length(), parserConfiguration);
  }
}
