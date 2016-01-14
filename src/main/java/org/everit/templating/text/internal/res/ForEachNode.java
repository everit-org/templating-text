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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.everit.expression.CompiledExpression;
import org.everit.templating.text.internal.CompilableNodeHelper;
import org.everit.templating.text.internal.TextTemplateUtil;
import org.everit.templating.util.InheritantMap;
import org.everit.templating.util.TemplateWriter;
import org.everit.templating.util.UniversalIterable;

public class ForEachNode extends Node {

  private CompiledExpression[] ce;

  private CompiledExpression cSepExpr;

  private final CompilableNodeHelper helper;

  private ContentRange[] item;

  public Node nestedNode;

  private char[] sepExpr;

  private final String templateFileName;

  public ForEachNode(final int begin, final String name, final char[] template, final int start,
      final int end, final CompilableNodeHelper helper, final String templateFileName) {
    super(begin, name, template, start, end);
    this.helper = helper;
    this.templateFileName = templateFileName;
    configure();
  }

  private void configure() {
    ArrayList<ContentRange> items = new ArrayList<ContentRange>();
    ArrayList<ContentRange> expr = new ArrayList<ContentRange>();

    int start = cStart;
    for (int i = start; i < cEnd; i++) {
      switch (contents[i]) {
        case '(':
        case '[':
        case '{':
        case '"':
        case '\'':
          i = TextTemplateUtil.balancedCapture(contents, i, contents[i], templateFileName);
          break;
        case ':':
          items.add(TextTemplateUtil.createStringTrimmed(contents, start, i - start));
          start = i + 1;
          break;
        case ',':
          if (expr.size() != (items.size() - 1)) {
            throw TextTemplateUtil.createCompileException(templateFileName,
                "unexpected character ',' in foreach tag", contents, cStart
                    + i,
                null);
          }
          expr.add(TextTemplateUtil.createStringTrimmed(contents, start, i - start));
          start = i + 1;
          break;
      }
    }

    if (start < cEnd) {
      if (expr.size() != (items.size() - 1)) {
        throw TextTemplateUtil.createCompileException(templateFileName,
            "expected character ':' in foreach tag",
            contents, cStart, null);
      }
      expr.add(TextTemplateUtil.createStringTrimmed(contents, start, cEnd - start));
    }

    item = new ContentRange[items.size()];
    int i = 0;
    for (ContentRange s : items) {
      item[i++] = s;
    }

    ce = new CompiledExpression[(new String[expr.size()]).length];
    i = 0;
    for (ContentRange s : expr) {
      ce[i++] = helper.compileExpression(contents, s.cStart, s.length);
    }
  }

  @Override
  public boolean demarcate(final Node terminatingnode, final char[] template) {
    nestedNode = next;
    next = terminus;

    sepExpr = terminatingnode.getContents();
    if (sepExpr.length == 0) {
      sepExpr = null;
    } else {
      TerminalNode terminalNode = (TerminalNode) terminatingnode;
      CompilableNodeHelper sepHelper = new CompilableNodeHelper(helper.getOriginalConfig(),
          helper.getExpressionCompiler(), terminalNode.getLine(), (terminalNode.cStart
              - terminalNode.getColumn()) + 1);

      cSepExpr = sepHelper.compileExpression(contents, terminatingnode.cStart,
          terminatingnode.end - terminatingnode.cStart);
    }

    return false;
  }

  @Override
  public Object eval(final TemplateWriter appender, final Map<String, Object> vars) {

    Iterator<?>[] iters = new Iterator[item.length];

    for (int i = 0; i < iters.length; i++) {
      Object o = ce[i].eval(vars);
      iters[i] = new UniversalIterable<Object>(o).iterator();
    }

    Map<String, Object> localVars = new InheritantMap<String, Object>(vars, true);

    int iterate = iters.length;

    while (true) {
      for (int i = 0; i < iters.length; i++) {
        if (!iters[i].hasNext()) {
          iterate--;
          localVars.put(String.valueOf(contents, item[i].cStart, item[i].length), "");
        } else {
          localVars.put(String.valueOf(contents, item[i].cStart, item[i].length), iters[i].next());
        }
      }
      if (iterate != 0) {
        nestedNode.eval(appender, localVars);

        if (sepExpr != null) {
          for (Iterator<?> it : iters) {
            if (it.hasNext()) {
              appender.append(String.valueOf(cSepExpr.eval(vars)));
              break;
            }
          }
        }
      } else {
        break;
      }
    }

    return next != null ? next.eval(appender, vars) : null;
  }

  public Node getNestedNode() {
    return nestedNode;
  }

  public void setNestedNode(final Node nestedNode) {
    this.nestedNode = nestedNode;
  }
}
