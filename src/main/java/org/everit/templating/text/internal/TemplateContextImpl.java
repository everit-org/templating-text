package org.everit.templating.text.internal;

import java.util.Map;

import org.everit.templating.text.internal.res.Node;
import org.everit.templating.util.AbstractTemplateContextImpl;

public class TemplateContextImpl extends AbstractTemplateContextImpl {

    private final Map<String, Node> fragments;

    private final CompiledTemplateImpl runtime;

    private final TemplateWriter writer;

    public TemplateContextImpl(final String fragment, final Map<String, Node> fragments,
            final CompiledTemplateImpl runtime,
            final Map<String, Object> vars, final TemplateWriter writer) {
        super(fragment, vars);
        this.fragments = fragments;
        this.runtime = runtime;
        this.writer = writer;
    }

    @Override
    protected void renderFragmentInternal(final String fragmentId, final Map<String, Object> vars) {
        Node fragmentNode = fragments.get(fragmentId);
        if (fragmentNode == null) {
            // TODO throw nice excepion
        }

        fragmentNode.eval(runtime, writer, null, vars);

    }
}
