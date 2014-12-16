/**
 * This file is part of Everit - Templating Text.
 *
 * Everit - Templating Text is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Everit - Templating Text is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Everit - Templating Text.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.everit.templating.text.internal.res;

import java.util.HashMap;
import java.util.Map;

import org.everit.expression.CompiledExpression;
import org.everit.templating.text.CompileException;
import org.everit.templating.text.internal.CompilableNodeHelper;
import org.everit.templating.util.InheritantMap;
import org.everit.templating.util.TemplateWriter;

public class FragmentNode extends Node {
    private final String fragmentName;
    private final Map<String, Node> fragments;
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
        CompiledExpression ce = helper.compileExpression(template, cStart, cEnd - cStart);

        Object fragmentNameObject = ce.eval(new HashMap<String, Object>());
        if (fragmentNameObject == null || !(fragmentNameObject instanceof String)) {
            CompileException e = new CompileException("Fragment id must be a constant String: " + fragmentNameObject,
                    template, cStart);

            e.setColumn(cStart - helper.getLineStart() + 1);
            e.setLineNumber(helper.getLine());
            throw e;
        }
        this.fragmentName = (String) fragmentNameObject;

        if (fragments.containsKey(fragmentName)) {
            CompileException e = new CompileException("Duplicate fragment id: " + fragmentName,
                    template, cStart);

            e.setColumn(cStart - helper.getLineStart() + 1);
            e.setLineNumber(helper.getLine());
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
