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
package org.everit.templating.text.internal.res;

import java.util.Map;

import org.everit.templating.util.TemplateWriter;

public class CommentNode extends Node {
    public CommentNode() {
    }

    public CommentNode(final int begin, final String name, final char[] template, final int start, final int end) {
        this.name = name;
        this.end = this.cEnd = end;
    }

    public CommentNode(final int begin, final String name, final char[] template, final int start, final int end,
            final Node next) {
        this.begin = begin;
        this.end = this.cEnd = end;
        this.next = next;
    }

    @Override
    public boolean demarcate(final Node terminatingNode, final char[] template) {
        return false;
    }

    @Override
    public Object eval(final TemplateWriter appender, final Map<String, Object> vars) {
        if (next != null) {
            return next.eval(appender, vars);
        } else {
            return null;
        }
    }
}
