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

import org.everit.templating.text.internal.TextTemplateUtil;
import org.everit.templating.util.TemplateWriter;

public abstract class Node {
  protected int begin;
  protected int cEnd;
  protected char[] contents;
  protected int cStart;
  protected int end;
  protected String name;
  public Node next;
  protected TerminalNode terminus;

  public Node() {
  }

  public Node(final int begin, final String name, final char[] template, final int start,
      final int end) {
    this.begin = begin;
    cStart = start;
    cEnd = end - 1;
    this.end = end;
    this.name = name;
    contents = template;
    // this.contents = subset(template, this.cStart = start, (this.end = this.cEnd = end) - start -
    // 1);
  }

  public Node(final int begin, final String name, final char[] template, final int start,
      final int end,
      final Node next) {
    this.name = name;
    this.begin = begin;
    cStart = start;
    cEnd = end - 1;
    this.end = end;
    contents = template;
    // this.contents = subset(template, this.cStart = start, (this.end = this.cEnd = end) - start -
    // 1);
    this.next = next;
  }

  public void calculateContents(final char[] template) {
    contents = TextTemplateUtil.subset(template, cStart, end - cStart);
  }

  public abstract boolean demarcate(Node terminatingNode, char[] template);

  public abstract Object eval(TemplateWriter writer, Map<String, Object> vars);

  public int getBegin() {
    return begin;
  }

  public int getCEnd() {
    return cEnd;
  }

  public char[] getContents() {
    return contents;
  }

  public int getCStart() {
    return cStart;
  }

  public int getEnd() {
    return end;
  }

  public int getLength() {
    return end - begin;
  }

  public String getName() {
    return name;
  }

  public Node getNext() {
    return next;
  }

  public TerminalNode getTerminus() {
    return terminus;
  }

  public boolean isOpenNode() {
    return false;
  }

  public void setBegin(final int begin) {
    this.begin = begin;
  }

  public void setCEnd(final int cEnd) {
    this.cEnd = cEnd;
  }

  public void setContents(final char[] contents) {
    this.contents = contents;
  }

  public void setCStart(final int cStart) {
    this.cStart = cStart;
  }

  public void setEnd(final int end) {
    this.end = end;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public Node setNext(final Node next) {
    return this.next = next;
  }

  public void setTerminus(final TerminalNode terminus) {
    this.terminus = terminus;
  }
}
