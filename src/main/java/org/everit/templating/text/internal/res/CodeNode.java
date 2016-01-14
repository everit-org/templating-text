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

import org.everit.expression.CompiledExpression;
import org.everit.templating.text.internal.CompilableNodeHelper;
import org.everit.templating.util.TemplateWriter;

public class CodeNode extends Node {
  private CompiledExpression ce;

  public CodeNode() {
  }

  public CodeNode(final int begin, final String name, final char[] template, final int start,
      final int end,
      final CompilableNodeHelper helper) {
    this.begin = begin;
    this.name = name;
    contents = template;
    cStart = start;
    cEnd = end - 1;
    this.end = end;

    ce = helper.compileExpression(template, start, cEnd - start);
  }

  @Override
  public boolean demarcate(final Node terminatingNode, final char[] template) {
    return false;
  }

  @Override
  public Object eval(final TemplateWriter appender, final Map<String, Object> vars) {
    ce.eval(vars);
    return next != null ? next.eval(appender, vars) : null;
  }

  @Override
  public String toString() {
    return "CodeNode:" + name + "{" + (contents == null ? "" : new String(contents)) + "} (start="
        + begin
        + ";end=" + end + ")";
  }
}
