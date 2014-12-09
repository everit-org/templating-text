package org.everit.templating.text.internal.res;

import java.util.Map;

import org.everit.expression.CompiledExpression;
import org.everit.templating.text.internal.CompilableNodeHelper;
import org.everit.templating.util.TemplateWriter;

public class ExpressionNode extends Node {
    private final CompiledExpression ce;

    public ExpressionNode(final int begin, final String name, final char[] template, final int start,
            final int end, final CompilableNodeHelper helper) {
        this.begin = begin;
        this.name = name;
        this.contents = template;
        this.cStart = start;
        this.cEnd = end - 1;
        this.end = end;
        ce = helper.getExpressionCompiler().compile(String.valueOf(template, cStart, cEnd - cStart),
                helper.generateParserConfiguration(cStart + 1));
    }

    @Override
    public boolean demarcate(final Node terminatingNode, final char[] template) {
        return false;
    }

    @Override
    public Object eval(final TemplateWriter appender, final Map<String, Object> vars) {
        appender.append(String.valueOf(ce.eval(vars)));
        return next != null ? next.eval(appender, vars) : null;
    }

    @Override
    public String toString() {
        return "ExpressionNode:" + name + "{" + (contents == null ? "" : new String(contents)) + "} (start=" + begin
                + ";end=" + end + ")";
    }
}
