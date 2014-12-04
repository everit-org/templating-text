package org.everit.templating.text.internal;

public class StackElement {

    public StackElement next;

    public Object value;

    public StackElement(final StackElement next, final Object value) {
        this.next = next;
        this.value = value;
    }
}
