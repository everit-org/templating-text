package org.everit.templating.text.internal.res;

import java.util.Map;

import org.everit.expression.CompiledExpression;
import org.everit.templating.text.internal.CompilableNodeHelper;
import org.everit.templating.util.TemplateWriter;

public class CodeNode extends Node {
    private CompiledExpression ce;

    public CodeNode() {
    }

    public CodeNode(final int begin, final String name, final char[] template, final int start, final int end,
            final CompilableNodeHelper helper) {
        this.begin = begin;
        this.name = name;
        this.contents = template;
        this.cStart = start;
        this.cEnd = end - 1;
        this.end = end;

        ce = helper.compileExpression(template, start, cEnd - start);
    }

    @Override
    public boolean demarcate(final Node terminatingNode, final char[] template) {
        return false;
    }

    @Override
    public Object eval(final TemplateWriter appender, final Map<String, Object> vars) {
        ce.eval(vars);
        return next != null ? next.eval(appender, vars) : null;
    }

    @Override
    public String toString() {
        return "CodeNode:" + name + "{" + (contents == null ? "" : new String(contents)) + "} (start=" + begin
                + ";end=" + end + ")";
    }
}
