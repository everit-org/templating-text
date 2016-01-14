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

public class ExecutionStack {

  private StackElement element;

  private int size = 0;

  public int deepCount() {
    int count = 0;

    if (element == null) {
      return 0;
    } else {
      count++;
    }

    StackElement element = this.element;
    while ((element = element.next) != null) {
      count++;
    }
    return count;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public Object peek() {
    if (size == 0) {
      return null;
    } else {
      return element.value;
    }
  }

  public Object pop() {
    if (size == 0) {
      return null;
    }
    try {
      size--;
      return element.value;
    } finally {
      element = element.next;
      assert size == deepCount();
    }
  }

  public void push(final Object o) {
    size++;
    element = new StackElement(element, o);
    assert size == deepCount();

  }

  @Override
  public String toString() {
    StackElement el = element;

    if (element == null) {
      return "<EMPTY>";
    }

    StringBuilder appender = new StringBuilder().append("[");
    do {
      appender.append(String.valueOf(el.value));
      if (el.next != null) {
        appender.append(", ");
      }
    } while ((el = el.next) != null);

    appender.append("]");

    return appender.toString();
  }
}
