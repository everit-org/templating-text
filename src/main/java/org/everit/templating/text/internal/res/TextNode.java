package org.everit.templating.text.internal.res;

import java.util.Map;

import org.everit.templating.util.TemplateWriter;

public class TextNode extends Node {
    private final String text;

    public TextNode(final String text, final int begin, final int end) {
        this.text = text;
        this.begin = begin;
        this.end = end;
    }

    @Override
    public void calculateContents(final char[] template) {
    }

    @Override
    public boolean demarcate(final Node terminatingNode, final char[] template) {
        return false;
    }

    @Override
    public Object eval(final TemplateWriter appender, final Map<String, Object> vars) {
        int len = end - begin;
        if (len != 0) {
            appender.append(text);
        }
        return next != null ? next.eval(appender, vars) : null;
    }

    @Override
    public String toString() {
        return "TextNode(" + begin + "," + end + ")";
    }
}
