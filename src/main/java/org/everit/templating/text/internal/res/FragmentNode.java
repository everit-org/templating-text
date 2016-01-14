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

import java.util.HashMap;
import java.util.Map;

import org.everit.expression.CompiledExpression;
import org.everit.templating.text.internal.CompilableNodeHelper;
import org.everit.templating.text.internal.TextTemplateUtil;
import org.everit.templating.util.CompileException;
import org.everit.templating.util.InheritantMap;
import org.everit.templating.util.TemplateWriter;

public class FragmentNode extends Node {
  private final String fragmentName;
  private final Map<String, Node> fragments;
  private Node nestedNode;

  public FragmentNode(final int begin, final String name, final char[] template, final int start,
      final int end, final CompilableNodeHelper helper, final Map<String, Node> fragments,
      final String templateFileName) {
    this.begin = begin;
    this.name = name;
    contents = template;
    cStart = start;
    this.fragments = fragments;
    cEnd = end - 1;
    this.end = end;
    CompiledExpression ce = helper.compileExpression(template, cStart, cEnd - cStart);

    Object fragmentNameObject = ce.eval(new HashMap<String, Object>());
    if ((fragmentNameObject == null) || !(fragmentNameObject instanceof String)) {
      CompileException e = TextTemplateUtil.createCompileException(
          templateFileName, "Fragment id must be a constant String: " + fragmentNameObject,
          template, cStart, null);

      e.setColumn((cStart - helper.getLineStart()) + 1);
      e.setLineNumber(helper.getLine());
      throw e;
    }
    fragmentName = (String) fragmentNameObject;

    if (fragments.containsKey(fragmentName)) {
      CompileException e =
          TextTemplateUtil.createCompileException(templateFileName,
              "Duplicate fragment id: " + fragmentName,
              template, cStart, null);

      e.setColumn((cStart - helper.getLineStart()) + 1);
      e.setLineNumber(helper.getLine());
      throw e;
    }

  }

  @Override
  public boolean demarcate(final Node terminatingNode, final char[] template) {
    Node n = nestedNode = next;

    while (n.getNext() != null) {
      n = n.next;
    }

    n.next = new EndNode();

    next = terminus;

    fragments.put(fragmentName, nestedNode);

    return false;
  }

  @Override
  public Object eval(final TemplateWriter appender, final Map<String, Object> vars) {

    nestedNode.eval(appender, new InheritantMap<String, Object>(vars, true));
    return next != null ? next.eval(appender, vars) : null;
  }
}
