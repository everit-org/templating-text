package org.everit.templating.text.internal.res;

import java.util.Map;

import org.everit.templating.text.internal.CompiledInline;
import org.everit.templating.text.internal.TemplateWriter;

public class EndNode extends Node {
    @Override
    public boolean demarcate(final Node terminatingNode, final char[] template) {
        return false;
    }

    @Override
    public Object eval(final CompiledInline runtie, final TemplateWriter appender, final Object ctx,
            final Map<String, Object> vars) {
        return appender.toString();
    }

    @Override
    public String toString() {
        return "EndNode";
    }
}
