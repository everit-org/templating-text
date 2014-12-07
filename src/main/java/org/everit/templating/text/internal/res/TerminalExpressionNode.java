package org.everit.templating.text.internal.res;

import java.util.Map;

import org.everit.expression.CompiledExpression;
import org.everit.templating.text.internal.CompilableNodeHelper;
import org.everit.templating.text.internal.CompiledTemplateImpl;
import org.everit.templating.text.internal.TemplateWriter;

public class TerminalExpressionNode extends Node {
    private final CompiledExpression ce;

    public TerminalExpressionNode(final Node node, final CompilableNodeHelper helper) {
        this.begin = node.begin;
        this.name = node.name;
        ce = helper.getExpressionCompiler().compile(
                String.valueOf(node.contents, node.cStart, node.cEnd - node.cStart),
                helper.generateParserConfiguration(cStart + 1));
    }

    @Override
    public boolean demarcate(final Node terminatingNode, final char[] template) {
        return false;
    }

    @Override
    public Object eval(final CompiledTemplateImpl runtime, final TemplateWriter appender, final Object ctx,
            final Map<String, Object> vars) {
        return ce.eval(vars);
    }
}
