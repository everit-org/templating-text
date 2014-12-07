package org.everit.templating.text.internal;

public class ParserContext {

    private int lineCount = 0;

    private int lineOffset;

    /**
     * Get total number of lines declared in the current context.
     *
     * @return int of lines
     */
    public int getLineCount() {
        return lineCount;
    }

    /**
     * Get the current line offset. This measures the number of cursor positions back to the beginning of the line.
     *
     * @return int offset
     */
    public int getLineOffset() {
        return lineOffset;
    }

    /**
     * Increments the current line count by the specified amount
     *
     * @param increment
     *            The number of lines to increment
     * @return int of lines
     */
    public int incrementLineCount(final int increment) {
        return this.lineCount += increment;
    }

    /**
     * Sets the current line offset. (Generally only used by the compiler)
     *
     * @param lineOffset
     *            The offset amount
     */
    public void setLineOffset(final int lineOffset) {
        this.lineOffset = lineOffset;
    }
}
