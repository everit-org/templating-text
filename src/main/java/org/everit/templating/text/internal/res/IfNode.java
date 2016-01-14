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
import org.everit.templating.text.internal.TextTemplateUtil;
import org.everit.templating.util.TemplateWriter;

public class IfNode extends Node {

  private CompiledExpression ce;

  protected Node elseNode;

  protected Node trueNode;

  public IfNode(final int begin, final String name, final char[] template, final int start,
      final int end,
      final CompilableNodeHelper helper) {
    super(begin, name, template, start, end);
    while ((cEnd > cStart) && TextTemplateUtil.isWhitespace(template[cEnd])) {
      cEnd--;
    }

    while ((cEnd > cStart) && TextTemplateUtil.isWhitespace(template[cEnd])) {
      cEnd--;
    }
    if (cStart != cEnd) {
      ce = helper.compileExpression(template, cStart, cEnd - start);
    }
  }

  @Override
  public boolean demarcate(final Node terminatingNode, final char[] template) {
    trueNode = next;
    next = terminus;
    return true;
  }

  @Override
  public Object eval(final TemplateWriter appender, final Map<String, Object> vars) {
    if (evalCE(vars)) {
      return trueNode.eval(appender, vars);
    }
    return next != null ? next.eval(appender, vars) : null;
  }

  private boolean evalCE(final Map<String, Object> vars) {
    if (ce == null) {
      return true;
    }
    Object result = ce.eval(vars);
    if (result == null) {
      return false;
    }

    if (result instanceof Boolean) {
      return (Boolean) result;
    }

    return Boolean.valueOf(String.valueOf(result));
  }

  public Node getElseNode() {
    return elseNode;
  }

  public Node getTrueNode() {
    return trueNode;
  }

  public void setElseNode(final ExpressionNode elseNode) {
    this.elseNode = elseNode;
  }

  public void setTrueNode(final ExpressionNode trueNode) {
    this.trueNode = trueNode;
  }
}
