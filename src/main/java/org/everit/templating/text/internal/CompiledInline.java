package org.everit.templating.text.internal;

import org.everit.templating.text.internal.res.Node;

public class CompiledInline {
    private Node root;
    private char[] template;

    public CompiledInline(final char[] template, final Node root) {
        this.template = template;
        this.root = root;
    }

    public Node getRoot() {
        return root;
    }

    public char[] getTemplate() {
        return template;
    }

    public void setRoot(final Node root) {
        this.root = root;
    }

    public void setTemplate(final char[] template) {
        this.template = template;
    }
}
