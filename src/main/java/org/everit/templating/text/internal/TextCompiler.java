/**
 * This file is part of Everit - Templating Text.
 *
 * Everit - Templating Text is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Everit - Templating Text is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Everit - Templating Text.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.everit.templating.text.internal;

import java.util.HashMap;
import java.util.Map;

import org.everit.expression.ExpressionCompiler;
import org.everit.expression.ParserConfiguration;
import org.everit.templating.text.internal.res.CodeNode;
import org.everit.templating.text.internal.res.CommentNode;
import org.everit.templating.text.internal.res.EndNode;
import org.everit.templating.text.internal.res.ExpressionNode;
import org.everit.templating.text.internal.res.ForEachNode;
import org.everit.templating.text.internal.res.FragmentNode;
import org.everit.templating.text.internal.res.IfNode;
import org.everit.templating.text.internal.res.Node;
import org.everit.templating.text.internal.res.Opcodes;
import org.everit.templating.text.internal.res.TerminalExpressionNode;
import org.everit.templating.text.internal.res.TerminalNode;
import org.everit.templating.text.internal.res.TextNode;
import org.everit.templating.util.CompileException;

public class TextCompiler {
    private static final Map<String, Integer> OPCODES = new HashMap<String, Integer>();

    static {
        OPCODES.put("if", Opcodes.IF);
        OPCODES.put("else", Opcodes.ELSE);
        OPCODES.put("elseif", Opcodes.ELSE);
        OPCODES.put("end", Opcodes.END);
        OPCODES.put("foreach", Opcodes.FOREACH);
        OPCODES.put("fragment", Opcodes.FRAGMENT);

        OPCODES.put("comment", Opcodes.COMMENT);
        OPCODES.put("code", Opcodes.CODE);
    }

    public static boolean isIdentifierPart(final int c) {
        return ((c > 96 && c < 123)
                || (c > 64 && c < 91) || (c > 47 && c < 58) || (c == '_') || (c == '$')
                || Character.isJavaIdentifierPart(c));
    }

    private static boolean isWhitespace(final char c) {
        return c < '\u0020' + 1;
    }

    private int colStart;

    private int cursor;

    private final int end;

    private final ExpressionCompiler expressionCompiler;

    private final Map<String, Node> fragments = new HashMap<String, Node>();

    private int lastTextRangeEnding;

    private int line;

    private final ParserConfiguration parserConfiguration;

    private int start;

    private final char[] template;

    public TextCompiler(final char[] document, final int templateStart, final int templateLength,
            final ExpressionCompiler expressionCompiler,
            final ParserConfiguration parserConfiguration) {
        this.expressionCompiler = expressionCompiler;
        this.parserConfiguration = parserConfiguration;
        this.template = document;
        this.end = templateStart + templateLength;
        this.cursor = templateStart;
    }

    private int balancedCaptureWithLineAccounting(final char[] chars, int start, final int end, final char type,
            final ParserContext pCtx) {
        int depth = 1;
        int st = start;
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
            for (start++; start != end; start++) {
                if (chars[start] == type) {
                    return start;
                }
            }
        }
        else {
            int lines = 0;
            for (start++; start < end; start++) {
                if (isWhitespace(chars[start])) {
                    switch (chars[start]) {
                    case '\r':
                        continue;
                    case '\n':
                        if (pCtx != null) {
                            pCtx.setLineOffset((short) start);
                        }
                        lines++;
                    }
                }
                else if (start < end && chars[start] == '/') {
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
                        Skiploop: while (start != end) {
                            switch (chars[start]) {
                            case '*':
                                if (start + 1 < end && chars[start + 1] == '/') {
                                    break Skiploop;
                                }
                            case '\r':
                            case '\n':
                                if (pCtx != null) {
                                    pCtx.setLineOffset((short) start);
                                }
                                lines++;
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
                    if (pCtx != null) {
                        pCtx.incrementLineCount(lines);
                    }
                    return start;
                }
            }
        }

        switch (type) {
        case '[':
            throw new CompileException(getTemplateFileName() + "unbalanced braces [ ... ]", chars, st);
        case '{':
            throw new CompileException(getTemplateFileName() + "unbalanced braces { ... }", chars, st);
        case '(':
            throw new CompileException(getTemplateFileName() + "unbalanced braces ( ... )", chars, st);
        default:
            throw new CompileException(getTemplateFileName() + "unterminated string literal", chars, st);
        }
    }

    private char[] capture() {
        char[] newChar = new char[cursor - start];
        for (int i = 0; i < newChar.length; i++) {
            newChar[i] = template[i + start];
        }
        return newChar;
    }

    private int captureOrbInternal() {
        ParserContext pCtx = new ParserContext();
        cursor = balancedCaptureWithLineAccounting(template, start = cursor, end, '{', pCtx);
        line += pCtx.getLineCount();
        int ret = start + 1;
        start = cursor + 1;
        return ret;
    }

    private int captureOrbToken() {
        int newStart = ++cursor;
        while ((cursor != end) && isIdentifierPart(template[cursor])) {
            cursor++;
        }
        if (cursor != end) {
            if (template[cursor] == '{') {
                return newStart;
            } else if (template[cursor] == '\n') {
                line++;
                colStart = cursor + 1;
            }
        }
        return -1;
    }

    private int captureStringLiteral(final char type, final char[] expr, int cursor, final int end) {
        while (++cursor < end && expr[cursor] != type) {
            if (expr[cursor] == '\\') {
                cursor++;
            }
        }

        if (cursor >= end || expr[cursor] != type) {
            throw new CompileException(getTemplateFileName() + "unterminated string literal", expr, cursor);
        }

        return cursor;
    }

    public CompiledTemplateImpl compile() {
        return new CompiledTemplateImpl(compileFrom(null, new ExecutionStack()), fragments);
    }

    public Node compileFrom(Node root, final ExecutionStack stack) {
        line = parserConfiguration.getStartRow();
        colStart = cursor - parserConfiguration.getStartColumn() + 1;

        Node n = root;
        if (root == null) {
            n = root = new TextNode(template, cursor, cursor);
        }

        IfNode last;
        Integer opcode;
        String name;
        int x;

        while (cursor < end) {
            switch (template[cursor]) {
            case '\n':
                line++;
                colStart = cursor + 1;
                break;
            case '@':
            case '$':
                if (isNext(template[cursor])) {
                    start = ++cursor;
                    (n = markTextNode(n)).setEnd(n.getEnd() + 1);
                    start = lastTextRangeEnding = ++cursor;

                    continue;
                }
                if ((x = captureOrbToken()) != -1) {
                    start = x;
                    switch ((opcode = OPCODES.get(name = new String(capture()))) == null ? 0 : opcode) {
                    case Opcodes.IF:
                        /**
                         * Capture any residual text node, and push the if statement on the nesting stack.
                         */
                        stack.push(n = markTextNode(n).next =
                                new IfNode(start, name, template, captureOrbInternal(), start,
                                        createNodeHelper()));

                        n.setTerminus(new TerminalNode());

                        break;

                    case Opcodes.ELSE:
                        if (!stack.isEmpty() && stack.peek() instanceof IfNode) {
                            markTextNode(n).next = (last = (IfNode) stack.pop()).getTerminus();

                            last.demarcate(last.getTerminus(), template);
                            last.next = n = new IfNode(start, name, template,
                                    captureOrbInternal(), start, createNodeHelper());
                            n.setTerminus(last.getTerminus());

                            stack.push(n);
                        }
                        break;

                    case Opcodes.FOREACH:
                        stack.push(
                                n = markTextNode(n).next = new ForEachNode(start, name,
                                        template, captureOrbInternal(), start, createNodeHelper(),
                                        getTemplateFileName()));

                        n.setTerminus(new TerminalNode());

                        break;

                    case Opcodes.CODE:
                        n = markTextNode(n)
                                .next = new CodeNode(start, name, template,
                                        captureOrbInternal(), start = cursor + 1, createNodeHelper());
                        break;

                    case Opcodes.COMMENT:
                        n = markTextNode(n)
                                .next = new CommentNode(start, name, template, captureOrbInternal(),
                                        start = cursor + 1);

                        break;

                    case Opcodes.FRAGMENT:
                        stack.push(n = markTextNode(n).next = new FragmentNode(start, name, template,
                                captureOrbInternal(), start = cursor + 1, createNodeHelper(), fragments,
                                getTemplateFileName()));
                        n.setTerminus(new TerminalNode());

                        break;
                    case Opcodes.END:
                        n = markTextNode(n);

                        Node end = (Node) stack.pop();
                        TerminalNode terminal = end.getTerminus();

                        terminal.setLine(line);
                        int tmpColStart = colStart;
                        terminal.setCStart(captureOrbInternal());
                        terminal.setColumn(terminal.getCStart() - tmpColStart + 1);
                        terminal.setEnd((lastTextRangeEnding = start) - 1);
                        terminal.calculateContents(template);

                        if (end.demarcate(terminal, template)) {
                            n = n.next = terminal;
                        } else {
                            n = terminal;
                        }

                        break;

                    default:
                        if (name.length() == 0) {
                            n = markTextNode(n).next =
                                    new ExpressionNode(start, name, template, captureOrbInternal(),
                                            start = cursor + 1, createNodeHelper());
                        } else {
                            CompileException e = new CompileException(getTemplateFileName() + "unknown token type: " + name, template, start);
                            e.setLineNumber(line);
                            e.setColumn(start - colStart + 1);
                            throw e;

                        }
                    }
                }

                break;
            }
            cursor++;
        }

        if (!stack.isEmpty()) {
            CompileException ce = new CompileException(getTemplateFileName() + "unclosed @"
                    + ((Node) stack.peek()).getName()
                    + "{} block. expected @end{}", template, cursor);
            ce.setColumn(cursor - colStart + 1);
            ce.setLineNumber(line);
            throw ce;
        }

        if (start < end) {
            n = n.next = new TextNode(template, start, end);
        }
        n.next = new EndNode();

        n = root;
        do {
            if (n.getLength() != 0) {
                break;
            }
        } while ((n = n.getNext()) != null);

        if (n != null && n.getLength() == end - 1) {
            if (n instanceof ExpressionNode) {
                return new TerminalExpressionNode(n, createNodeHelper());
            }
            else {
                return n;
            }
        }

        return root;
    }

    private CompilableNodeHelper createNodeHelper() {
        return new CompilableNodeHelper(parserConfiguration,
                expressionCompiler, line, colStart);
    }

    private String getTemplateFileName(){
      return "[Name: " + parserConfiguration.getName() + "] ";
    }

    private boolean isNext(final char c) {
        return cursor != end && template[cursor + 1] == c;
    }

    private Node markTextNode(final Node n) {
        int s = (n.getEnd() > lastTextRangeEnding ? n.getEnd() : lastTextRangeEnding);

        if (s < start) {
            lastTextRangeEnding = start - 1;
            return n.next = new TextNode(template, s, lastTextRangeEnding);
        }
        return n;
    }
}
