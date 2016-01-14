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

import org.everit.templating.text.internal.res.ContentRange;
import org.everit.templating.util.CompileException;

public class TextTemplateUtil {

  /**
   * This is an important aspect of the core parser tools. This method is used throughout the core
   * parser and sub-lexical parsers to capture a balanced capture between opening and terminating
   * tokens such as: <em>( [ { ' " </em> <br>
   * <br>
   * For example: ((foo + bar + (bar - foo)) * 20;<br>
   * <br>
   * <p>
   * If a balanced capture is performed from position 2, we get "(foo + bar + (bar - foo))" back.
   * <br>
   * If a balanced capture is performed from position 15, we get "(bar - foo)" back.<br>
   * Etc.
   *
   * @param chars
   *          -
   * @param start
   *          -
   * @param type
   *          -
   * @param templateFileName
   *          -
   * @return -
   */
  public static int balancedCapture(final char[] chars, final int start, final char type,
      final String templateFileName) {
    return TextTemplateUtil.balancedCapture(chars, start, chars.length, type, templateFileName);
  }

  public static int balancedCapture(final char[] chars, int start, final int end, final char type,
      final String templateFileName) {
    int depth = 1;
    char term = type;
    switch (type) {
      case '[':
        term = ']';
        break;
      case '{':
        term = '}';
        break;
      case '(':
        term = ')';
        break;
    }

    if (type == term) {
      for (start++; start < end; start++) {
        if (chars[start] == type) {
          return start;
        }
      }
    } else {
      for (start++; start < end; start++) {
        if ((start < end) && (chars[start] == '/')) {
          if ((start + 1) == end) {
            return start;
          }
          if (chars[start + 1] == '/') {
            start++;
            while ((start < end) && (chars[start] != '\n')) {
              start++;
            }
          } else if (chars[start + 1] == '*') {
            start += 2;
            SkipComment: while (start < end) {
              switch (chars[start]) {
                case '*':
                  if (((start + 1) < end) && (chars[start + 1] == '/')) {
                    break SkipComment;
                  }
                case '\r':
                case '\n':

                  break;
              }
              start++;
            }
          }
        }
        if (start == end) {
          return start;
        }
        if ((chars[start] == '\'') || (chars[start] == '"')) {
          start = TextTemplateUtil.captureStringLiteral(chars[start], chars, start, end,
              templateFileName);
        } else if (chars[start] == type) {
          depth++;
        } else if ((chars[start] == term) && (--depth == 0)) {
          return start;
        }
      }
    }

    switch (type) {
      case '[':
        throw TextTemplateUtil.createCompileException(templateFileName, "unbalanced braces [ ... ]",
            chars, start, null);
      case '{':
        throw TextTemplateUtil.createCompileException(templateFileName, "unbalanced braces { ... }",
            chars, start, null);
      case '(':
        throw TextTemplateUtil.createCompileException(templateFileName, "unbalanced braces ( ... )",
            chars, start, null);
      default:
        throw TextTemplateUtil.createCompileException(templateFileName,
            "unterminated string literal", chars, start, null);
    }
  }

  public static int captureStringLiteral(final char type, final char[] expr, int cursor,
      final int end,
      final String templateFileName) {
    while ((++cursor < end) && (expr[cursor] != type)) {
      if (expr[cursor] == '\\') {
        cursor++;
      }
    }

    if ((cursor >= end) || (expr[cursor] != type)) {
      throw TextTemplateUtil.createCompileException(templateFileName, "unterminated string literal",
          expr, cursor, null);
    }

    return cursor;
  }

  /**
   * Instantiates a new {@link CompileException} based on the parameters.
   *
   * @param templateName
   *          The name of the template or <code>null</code> if no template name is available. If
   *          defined, the template name will be concatenated with the message.
   * @param message
   *          The message of the exception.
   * @param expr
   *          The expression that caused the exception
   * @param cursor
   *          The cursor within the expression character array.
   * @param cause
   *          The cause of the exception.
   * @return The instantiated {@link CompileException}.
   */
  public static CompileException createCompileException(final String templateName,
      final String message,
      final char[] expr, final int cursor, final Throwable cause) {

    StringBuilder sb = new StringBuilder();

    if (templateName != null) {
      sb.append("[Name: ").append(templateName).append("] ");
    }
    sb.append(message);

    if (cause == null) {
      return new CompileException(sb.toString(), expr, cursor);
    } else {
      return new CompileException(sb.toString(), expr, cursor, cause);
    }
  }

  public static ContentRange createStringTrimmed(final char[] s, int start, int length) {
    if ((length = start + length) > s.length) {
      return new ContentRange(start, length);
    }
    while ((start != length) && (s[start] < ('\u0020' + 1))) {
      start++;
    }
    while ((length != start) && (s[length - 1] < ('\u0020' + 1))) {
      length--;
    }
    return new ContentRange(start, length - start);
  }

  public static boolean isWhitespace(final char c) {
    return c < ('\u0020' + 1);
  }

  public static char[] subset(final char[] array, final int start, final int length) {

    char[] newArray = new char[length];

    for (int i = 0; i < newArray.length; i++) {
      newArray[i] = array[i + start];
    }

    return newArray;
  }
}
