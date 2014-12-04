package org.everit.templating.text.internal.res;

import java.io.Writer;
import java.util.Map;

import org.everit.expression.CompiledExpression;
import org.everit.expression.ExpressionCompiler;
import org.everit.templating.text.internal.InlineRuntime;

public class TerminalExpressionNode extends Node {
    private final CompiledExpression ce;

    public TerminalExpressionNode(final Node node, final ExpressionCompiler expressionCompiler) {
        this.begin = node.begin;
        this.name = node.name;
        ce = expressionCompiler.compile(String.valueOf(node.contents, node.cStart, node.cEnd - node.cStart));
    }

    @Override
    public boolean demarcate(final Node terminatingNode, final char[] template) {
        return false;
    }

    @Override
    public Object eval(final InlineRuntime runtime, final Writer appender, final Object ctx,
            final Map<String, Object> vars) {
        return ce.eval(vars);
    }
}
