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

import java.io.Writer;
import java.util.Map;

import org.everit.templating.CompiledTemplate;
import org.everit.templating.TemplateConstants;
import org.everit.templating.text.internal.res.Node;
import org.everit.templating.util.InheritantMap;
import org.everit.templating.util.TemplateWriter;

/**
 * This is the root of the template runtime, and contains various utility methods for executing templates.
 */
public class CompiledTemplateImpl implements CompiledTemplate {

    private final Map<String, Node> fragments;
    private final Node rootNode;

    public CompiledTemplateImpl(final Node rootNode, final Map<String, Node> fragments) {
        this.rootNode = rootNode;
        this.fragments = fragments;
    }

    @Override
    public void render(final Writer writer, final Map<String, Object> vars) {
        render(writer, vars, null);

    }

    @Override
    public void render(final Writer writer, final Map<String, Object> vars, final String fragmentId) {
        TemplateWriter templateWriter = new TemplateWriter(writer);

        InheritantMap<String, Object> scopedVars = new InheritantMap<String, Object>(vars, false);
        Node node;
        String tmpFragmentId;

        if (fragmentId == null || TemplateConstants.FRAGMENT_ROOT.equals(fragmentId)) {
            node = rootNode;
            tmpFragmentId = TemplateConstants.FRAGMENT_ROOT;
        } else {
            tmpFragmentId = fragmentId;
            node = fragments.get(fragmentId);
        }
        TemplateContextImpl templateContext = new TemplateContextImpl(tmpFragmentId, fragments, scopedVars);

        scopedVars.putWithoutChecks(TemplateConstants.VAR_TEMPLATE_CONTEXT, templateContext);
        node.eval(templateWriter, scopedVars);

    }

}
