package org.everit.templating.text.internal;

import java.io.Writer;
import java.util.Map;

import org.everit.templating.text.internal.res.Node;

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
