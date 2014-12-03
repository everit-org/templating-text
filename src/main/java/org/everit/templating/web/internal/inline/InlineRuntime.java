/**
 * This file is part of Everit - Web Templating.
 *
 * Everit - Web Templating is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Everit - Web Templating is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Everit - Web Templating.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.everit.templating.web.internal.inline;

import java.io.Writer;
import java.util.Map;

import org.everit.templating.web.internal.TemplateContextImpl;
import org.everit.templating.web.internal.inline.res.Node;

/**
 * This is the root of the template runtime, and contains various utility methods for executing templates.
 */
public class InlineRuntime {

    public static Object execute(final Node root, final char[] template,
            final TemplateContextImpl templateContext) {

        return new InlineRuntime(template, root, ".").execute(templateContext);
    }

    private final String baseDir;

    private ExecutionStack relPath;

    private Node rootNode;

    private char[] template;

    public InlineRuntime(final char[] template, final Node rootNode, final String baseDir) {
        this.template = template;
        this.rootNode = rootNode;
        this.baseDir = baseDir;
    }

    public Object execute(final Writer writer, final Map<String, Object> vars) {
        return rootNode.eval(this, stream, null, vars);
    }

    public ExecutionStack getRelPath() {
        if (relPath == null) {
            relPath = new ExecutionStack();
            relPath.push(baseDir);
        }
        return relPath;
    }

    public Node getRootNode() {
        return rootNode;
    }

    public char[] getTemplate() {
        return template;
    }

    public void setRootNode(final Node rootNode) {
        this.rootNode = rootNode;
    }

    public void setTemplate(final char[] template) {
        this.template = template;
    }
}
