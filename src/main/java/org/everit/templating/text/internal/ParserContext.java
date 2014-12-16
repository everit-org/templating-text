/**
 * This file is part of Everit - Templating Text.
 *
 * Everit - Templating Text is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Everit - Templating Text is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Everit - Templating Text.  If not, see <http://www.gnu.org/licenses/>.
 */
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
