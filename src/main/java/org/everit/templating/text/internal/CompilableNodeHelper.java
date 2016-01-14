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
package org.everit.templating.text.internal;

import org.everit.expression.CompiledExpression;
import org.everit.expression.ExpressionCompiler;
import org.everit.expression.ParserConfiguration;

/**
 * Helper class that contains context data for a node.
 */
public class CompilableNodeHelper {

  private final ExpressionCompiler expressionCompiler;

  private final int line;

  private final int lineStart;

  private final ParserConfiguration originalConfig;

  /**
   * Constructor.
   *
   * @param originalConfig
   *          The original parser configuration that was used.
   * @param expressionCompiler
   *          The compiler of the expressions.
   * @param line
   *          The line where the expression is in the template.
   * @param lineStart
   *          The column of the expression.
   */
  public CompilableNodeHelper(final ParserConfiguration originalConfig,
      final ExpressionCompiler expressionCompiler,
      final int line, final int lineStart) {
    this.originalConfig = originalConfig;
    this.expressionCompiler = expressionCompiler;
    this.line = line;
    this.lineStart = lineStart;
  }

  /**
   * Compiles the expression contained by the node.
   *
   * @param template
   *          The template that contains the node.
   * @param start
   *          The starting position of the expression within the template.
   * @param length
   *          The length of the expression.
   * @return The compiled expression.
   */
  public CompiledExpression compileExpression(final char[] template, final int start,
      final int length) {
    ParserConfiguration parserConfiguration = new ParserConfiguration(originalConfig);

    parserConfiguration.setStartRow(line);
    parserConfiguration.setStartColumn((start - lineStart) + 1);

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
