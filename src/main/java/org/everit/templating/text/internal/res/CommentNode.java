package org.everit.templating.text.internal.res;

import java.util.Map;

import org.everit.templating.util.TemplateWriter;

public class CommentNode extends Node {
    public CommentNode() {
    }

    public CommentNode(final int begin, final String name, final char[] template, final int start, final int end) {
        this.name = name;
        this.end = this.cEnd = end;
    }

    public CommentNode(final int begin, final String name, final char[] template, final int start, final int end,
            final Node next) {
        this.begin = begin;
        this.end = this.cEnd = end;
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
