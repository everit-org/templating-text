package org.everit.templating.text.internal.res;

import java.io.Writer;
import java.util.Map;

import org.everit.templating.text.internal.InlineRuntime;

public class TerminalNode extends Node {
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
    public Object eval(final InlineRuntime runtime, final Writer appender, final Object ctx,
            final Map<String, Object> vars) {
        return next != null ? next.eval(runtime, appender, ctx, vars) : null;
    }
}
