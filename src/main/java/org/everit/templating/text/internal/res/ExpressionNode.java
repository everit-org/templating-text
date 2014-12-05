package org.everit.templating.text.internal.res;

import java.util.Map;

import org.everit.expression.CompiledExpression;
import org.everit.expression.ExpressionCompiler;
import org.everit.templating.text.internal.CompiledInline;
import org.everit.templating.text.internal.TemplateWriter;

public class ExpressionNode extends Node {
    private final CompiledExpression ce;

    public ExpressionNode(final int begin, final String name, final char[] template, final int start,
            final int end, final ExpressionCompiler compiler) {
        this.begin = begin;
        this.name = name;
        this.contents = template;
        this.cStart = start;
        this.cEnd = end - 1;
        this.end = end;
        ce = compiler.compile(String.valueOf(template, cStart, cEnd - cStart));
    }

    @Override
    public boolean demarcate(final Node terminatingNode, final char[] template) {
        return false;
    }

    @Override
    public Object eval(final CompiledInline runtime, final TemplateWriter appender, final Object ctx,
            final Map<String, Object> vars) {
        appender.append(String.valueOf(ce.eval(vars)));
        return next != null ? next.eval(runtime, appender, ctx, vars) : null;
    }

    @Override
    public String toString() {
        return "ExpressionNode:" + name + "{" + (contents == null ? "" : new String(contents)) + "} (start=" + begin
                + ";end=" + end + ")";
    }
}
