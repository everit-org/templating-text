package org.everit.templating.text.internal;

public class TextTemplateUtil {

    /**
     * This is an important aspect of the core parser tools. This method is used throughout the core parser and
     * sub-lexical parsers to capture a balanced capture between opening and terminating tokens such as:
     * <em>( [ { ' " </em> <br>
     * <br>
     * For example: ((foo + bar + (bar - foo)) * 20;<br>
     * <br>
     * <p/>
     * If a balanced capture is performed from position 2, we get "(foo + bar + (bar - foo))" back.<br>
     * If a balanced capture is performed from position 15, we get "(bar - foo)" back.<br>
     * Etc.
     *
     * @param chars
     *            -
     * @param start
     *            -
     * @param type
     *            -
     * @return -
     */
    public static int balancedCapture(final char[] chars, final int start, final char type) {
        return balancedCapture(chars, start, chars.length, type);
    }

    public static int balancedCapture(final char[] chars, int start, final int end, final char type) {
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
        }
        else {
            for (start++; start < end; start++) {
                if (start < end && chars[start] == '/') {
                    if (start + 1 == end) {
                        return start;
                    }
                    if (chars[start + 1] == '/') {
                        start++;
                        while (start < end && chars[start] != '\n') {
                            start++;
                        }
                    }
                    else if (chars[start + 1] == '*') {
                        start += 2;
                        SkipComment: while (start < end) {
                            switch (chars[start]) {
                            case '*':
                                if (start + 1 < end && chars[start + 1] == '/') {
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
                if (chars[start] == '\'' || chars[start] == '"') {
                    start = captureStringLiteral(chars[start], chars, start, end);
                }
                else if (chars[start] == type) {
                    depth++;
                }
                else if (chars[start] == term && --depth == 0) {
                    return start;
                }
            }
        }

        throw new RuntimeException();
        // TODO throw nice exception.
        // switch (type) {
        // case '[':
        // throw new AbstractExpressionException("unbalanced braces [ ... ]", chars, start);
        // case '{':
        // throw new AbstractExpressionException("unbalanced braces { ... }", chars, start);
        // case '(':
        // throw new AbstractExpressionException("unbalanced braces ( ... )", chars, start);
        // default:
        // throw new AbstractExpressionException("unterminated string literal", chars, start);
        // }
    }

    public static int captureStringLiteral(final char type, final char[] expr, int cursor, final int end) {
        while (++cursor < end && expr[cursor] != type) {
            if (expr[cursor] == '\\') {
                cursor++;
            }
        }

        if (cursor >= end || expr[cursor] != type) {
            // TODO throw nice exception
            throw new RuntimeException();
            // throw new AbstractExpressionException("unterminated string literal", expr, cursor);
        }

        return cursor;
    }

    public static String createStringTrimmed(final char[] s, int start, int length) {
        if ((length = start + length) > s.length) {
            return new String(s);
        }
        while (start != length && s[start] < '\u0020' + 1) {
            start++;
        }
        while (length != start && s[length - 1] < '\u0020' + 1) {
            length--;
        }
        return new String(s, start, length - start);
    }

    public static boolean isWhitespace(final char c) {
        return c < '\u0020' + 1;
    }

    public static char[] subset(final char[] array, final int start, final int length) {

        char[] newArray = new char[length];

        for (int i = 0; i < newArray.length; i++) {
            newArray[i] = array[i + start];
        }

        return newArray;
    }
}