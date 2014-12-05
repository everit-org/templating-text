package org.everit.templating.text.internal;

import java.io.Writer;
import java.util.Map;

import org.everit.templating.CompiledTemplate;
import org.everit.templating.text.internal.res.Node;

/**
 * This is the root of the template runtime, and contains various utility methods for executing templates.
 */
public class CompiledInline implements CompiledTemplate {

    private final Node rootNode;

    public CompiledInline(final Node rootNode) {
        this.rootNode = rootNode;
    }

    @Override
    public void render(final Writer writer, final Map<String, Object> vars) {
        TemplateWriter templateWriter = new TemplateWriter(writer);
        rootNode.eval(this, templateWriter, null, vars);

    }

    @Override
    public void render(final Writer writer, final Map<String, Object> vars, final String bookmark) {
        if (bookmark == null) {
            render(writer, vars);
        } else {
            throw new UnsupportedOperationException("No bookmark support yet, sorry");
        }
    }

}
