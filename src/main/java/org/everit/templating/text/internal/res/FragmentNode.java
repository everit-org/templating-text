package org.everit.templating.text.internal.res;

import java.util.HashMap;
import java.util.Map;

import org.everit.expression.CompiledExpression;
import org.everit.templating.CompiledTemplate;
import org.everit.templating.text.internal.CompilableNodeHelper;
import org.everit.templating.text.internal.CompiledTemplateImpl;
import org.everit.templating.text.internal.TemplateWriter;
import org.everit.templating.util.InheritantMap;

public class FragmentNode extends Node {
    private final String fragmentName;
    private final Map<String, Node> fragments;
    private CompiledTemplate fragmentTemplate;
    private Node nestedNode;

    public FragmentNode(final int begin, final String name, final char[] template, final int start,
            final int end, final CompilableNodeHelper helper, final Map<String, Node> fragments) {
        this.begin = begin;
        this.name = name;
        this.contents = template;
        this.cStart = start;
        this.fragments = fragments;
        this.cEnd = end - 1;
        this.end = end;
        CompiledExpression ce = helper.getExpressionCompiler().compile(String.valueOf(template, cStart, cEnd - cStart),
                helper.generateParserConfiguration(cStart));

        Object fragmentNameObject = ce.eval(new HashMap<String, Object>());
        if (fragmentNameObject == null || !(fragmentNameObject instanceof String)) {
            // TODO throw nice exception
        }
        this.fragmentName = (String) fragmentNameObject;

    }

    @Override
    public boolean demarcate(final Node terminatingNode, final char[] template) {
        Node n = nestedNode = next;

        while (n.getNext() != null) {
            n = n.next;
        }

        n.next = new EndNode();

        next = terminus;

        if (fragments.containsKey(fragmentName)) {
            // TODO throw nice compile exception.
        }

        fragments.put(fragmentName, nestedNode);

        return false;
    }

    @Override
    public Object eval(final CompiledTemplateImpl runtime, final TemplateWriter appender, final Object ctx,
            final Map<String, Object> vars) {

        nestedNode.eval(runtime, appender, null, new InheritantMap<String, Object>(vars, true));
        return next != null ? next.eval(runtime, appender, ctx, vars) : null;
    }
}
