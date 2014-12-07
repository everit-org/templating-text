package org.everit.templating.text.internal;

import java.io.IOException;
import java.io.Writer;

public class TemplateWriter {

    private final Writer writer;

    public TemplateWriter(final Writer writer) {
        this.writer = writer;
    }

    public TemplateWriter append(final String text) {
        try {
            this.writer.write(text);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return this;
    }

    public Writer getWrapped() {
        return writer;
    }

}
