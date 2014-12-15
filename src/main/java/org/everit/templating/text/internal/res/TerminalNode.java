package org.everit.templating.text.internal.res;

import java.util.Map;

import org.everit.templating.util.TemplateWriter;

public class TerminalNode extends Node {
    private int column;

    private int line;

    public TerminalNode() {
    }

    public TerminalNode(final int begin, final int end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    public boolean demarcate(final Node terminatingNode, final char[] template) {
        return false;
    }

    @Override
    public Object eval(final TemplateWriter appender, final Map<String, Object> vars) {
        return next != null ? next.eval(appender, vars) : null;
    }

    public int getColumn() {
        return column;
    }

    public int getLine() {
        return line;
    }

    public void setColumn(final int column) {
        this.column = column;
    }

    public void setLine(final int line) {
        this.line = line;
    }
}
