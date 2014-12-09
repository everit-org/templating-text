package org.everit.templating.text.internal.res;

import java.util.HashMap;
import java.util.Map;

import org.everit.expression.CompileException;
import org.everit.expression.CompiledExpression;
import org.everit.expression.ParserConfiguration;
import org.everit.templating.text.internal.CompilableNodeHelper;
import org.everit.templating.util.InheritantMap;
import org.everit.templating.util.TemplateWriter;

public class FragmentNode extends Node {
    private final String fragmentName;
    private final Map<String, Node> fragments;
    private Node nestedNode;
    private final ParserConfiguration parserConfiguration;

    public FragmentNode(final int begin, final String name, final char[] template, final int start,
            final int end, final CompilableNodeHelper helper, final Map<String, Node> fragments) {
        this.begin = begin;
        this.name = name;
        this.contents = template;
        this.cStart = start;
        this.fragments = fragments;
        this.cEnd = end - 1;
        this.end = end;
        parserConfiguration = helper.generateParserConfiguration(cStart);
        CompiledExpression ce = helper.getExpressionCompiler().compile(template, cStart, cEnd - cStart,
                parserConfiguration);

        Object fragmentNameObject = ce.eval(new HashMap<String, Object>());
        if (fragmentNameObject == null || !(fragmentNameObject instanceof String)) {
            CompileException e = new CompileException("Fragment id must be a constant String: " + fragmentNameObject,
                    template, cStart);

            e.setColumn(parserConfiguration.getColumn());
            e.setLineNumber(parserConfiguration.getLineNumber());
            throw e;
        }
        this.fragmentName = (String) fragmentNameObject;

        if (fragments.containsKey(fragmentName)) {
            CompileException e = new CompileException("Duplicate fragment id: " + fragmentName,
                    template, cStart);

            e.setColumn(parserConfiguration.getColumn());
            e.setLineNumber(parserConfiguration.getLineNumber());
            throw e;
        }

    }

    @Override
    public boolean demarcate(final Node terminatingNode, final char[] template) {
        Node n = nestedNode = next;

        while (n.getNext() != null) {
            n = n.next;
        }

        n.next = new EndNode();

        next = terminus;

        fragments.put(fragmentName, nestedNode);

        return false;
    }

    @Override
    public Object eval(final TemplateWriter appender, final Map<String, Object> vars) {

        nestedNode.eval(appender, new InheritantMap<String, Object>(vars, true));
        return next != null ? next.eval(appender, vars) : null;
    }
}
