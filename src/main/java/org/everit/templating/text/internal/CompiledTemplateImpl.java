package org.everit.templating.text.internal;

import java.io.Writer;
import java.util.Map;

import org.everit.templating.CompiledTemplate;
import org.everit.templating.TemplateConstants;
import org.everit.templating.text.internal.res.Node;
import org.everit.templating.util.InheritantMap;

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
    public void render(final Writer writer, final Map<String, Object> vars, final String bookmark) {
        TemplateWriter templateWriter = new TemplateWriter(writer);

        InheritantMap<String, Object> scopedVars = new InheritantMap<String, Object>(vars, false);
        Node node;
        String fragmentId;

        if (bookmark == null || TemplateConstants.FRAGMENT_ROOT.equals(bookmark)) {
            node = rootNode;
            fragmentId = TemplateConstants.FRAGMENT_ROOT;
        } else {
            fragmentId = bookmark;
            node = fragments.get(bookmark);
        }
        TemplateContextImpl templateContext = new TemplateContextImpl(fragmentId, fragments, this, scopedVars,
                templateWriter);

        scopedVars.putWithoutChecks(TemplateConstants.VAR_TEMPLATE_CONTEXT, templateContext);
        node.eval(this, templateWriter, null, vars);

    }

}
