package org.everit.templating.text.internal.res;

import java.io.Writer;
import java.util.Map;

import org.everit.templating.text.internal.InlineRuntime;

public class TextNode extends Node {
    public TextNode(final int begin, final int end) {
        this.begin = begin;
        this.end = end;
    }

    public TextNode(final int begin, final int end, final ExpressionNode next) {
        this.begin = begin;
        this.end = end;
        this.next = next;
    }

    @Override
    public void calculateContents(final char[] template) {
    }

    @Override
    public boolean demarcate(final Node terminatingNode, final char[] template) {
        return false;
    }

    @Override
    public Object eval(final InlineRuntime runtime, final Writer appender, final Object ctx,
            final Map<String, Object> vars) {
        int len = end - begin;
        if (len != 0) {
            appender.append(new String(runtime.getTemplate(), begin, len));
        }
        return next != null ? next.eval(runtime, appender, ctx, vars) : null;
    }

    @Override
    public String toString() {
        return "TextNode(" + begin + "," + end + ")";
    }
}
