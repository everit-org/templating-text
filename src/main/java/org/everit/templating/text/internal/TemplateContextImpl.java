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
