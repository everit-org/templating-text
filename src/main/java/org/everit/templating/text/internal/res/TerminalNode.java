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
package org.everit.templating.text.internal.res;

import java.util.Map;

import org.everit.templating.util.TemplateWriter;

public class TerminalNode extends Node {
  private int column;

  private int line;

  public TerminalNode() {
  }

  public TerminalNode(final int begin, final int end) {
    this.begin = begin;
    this.end = end;
  }

  @Override
  public boolean demarcate(final Node terminatingNode, final char[] template) {
    return false;
  }

  @Override
  public Object eval(final TemplateWriter appender, final Map<String, Object> vars) {
    return next != null ? next.eval(appender, vars) : null;
  }

  public int getColumn() {
    return column;
  }

  public int getLine() {
    return line;
  }

  public void setColumn(final int column) {
    this.column = column;
  }

  public void setLine(final int line) {
    this.line = line;
  }
}
