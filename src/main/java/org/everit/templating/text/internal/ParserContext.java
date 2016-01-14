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

public class ParserContext {

  private int lineCount = 0;

  private int lineOffset;

  /**
   * Get total number of lines declared in the current context.
   *
   * @return int of lines
   */
  public int getLineCount() {
    return lineCount;
  }

  /**
   * Get the current line offset. This measures the number of cursor positions back to the beginning
   * of the line.
   *
   * @return int offset
   */
  public int getLineOffset() {
    return lineOffset;
  }

  /**
   * Increments the current line count by the specified amount
   *
   * @param increment
   *          The number of lines to increment
   * @return int of lines
   */
  public int incrementLineCount(final int increment) {
    return lineCount += increment;
  }

  /**
   * Sets the current line offset. (Generally only used by the compiler)
   *
   * @param lineOffset
   *          The offset amount
   */
  public void setLineOffset(final int lineOffset) {
    this.lineOffset = lineOffset;
  }
}
