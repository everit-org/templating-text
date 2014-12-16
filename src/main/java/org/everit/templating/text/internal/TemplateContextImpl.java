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
package org.everit.templating.text.internal;

import java.io.StringWriter;
import java.util.Map;

import org.everit.templating.text.internal.res.Node;
import org.everit.templating.util.AbstractTemplateContext;
import org.everit.templating.util.TemplateWriter;

public class TemplateContextImpl extends AbstractTemplateContext {

    private final Map<String, Node> fragments;

    public TemplateContextImpl(final String fragment, final Map<String, Node> fragments, final Map<String, Object> vars) {
        super(fragment, vars);
        this.fragments = fragments;
    }

    @Override
    protected String renderFragmentInternal(final String fragmentId, final Map<String, Object> vars) {
        Node fragmentNode = fragments.get(fragmentId);
        if (fragmentNode == null) {
            throw new IllegalArgumentException("Unkown fragment: " + fragmentId);
        }

        StringWriter stringWriter = new StringWriter();
        fragmentNode.eval(new TemplateWriter(stringWriter), vars);

        return stringWriter.toString();
    }
}
