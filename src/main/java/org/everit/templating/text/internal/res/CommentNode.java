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

public class CommentNode extends Node {
  public CommentNode() {
  }

  public CommentNode(final int begin, final String name, final char[] template, final int start,
      final int end) {
    this.name = name;
    this.end = cEnd = end;
  }

  public CommentNode(final int begin, final String name, final char[] template, final int start,
      final int end,
      final Node next) {
    this.begin = begin;
    this.end = cEnd = end;
    this.next = next;
  }

  @Override
  public boolean demarcate(final Node terminatingNode, final char[] template) {
    return false;
  }

  @Override
  public Object eval(final TemplateWriter appender, final Map<String, Object> vars) {
    if (next != null) {
      return next.eval(appender, vars);
    } else {
      return null;
    }
  }
}
