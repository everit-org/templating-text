package org.everit.templating.text;

import static java.lang.String.copyValueOf;
import static org.mvel2.util.ParseTools.isWhitespace;
import static org.mvel2.util.ParseTools.repeatChar;

import java.util.Collections;
import java.util.List;

import org.mvel2.ErrorDetail;

/**
 * Standard exception thrown for all general compileShared and some runtime failures.
 */
public class CompileException extends RuntimeException {
    private int column = 0;

    private int cursor = 0;
    private List<ErrorDetail> errors;

    private char[] expr;
    private int lastLineStart = 0;

    private int lineNumber = 1;

    private int msgOffset = 0;

    public CompileException(final String message, final char[] expr, final int cursor) {
        super(message);
        this.expr = expr;
        this.cursor = cursor;
    }

    public CompileException(final String message, final char[] expr, final int cursor, final Throwable e) {
        super(message, e);
        this.expr = expr;
        this.cursor = cursor;
    }

    private void calcRowAndColumn() {
        if (lineNumber > 1 || column > 1) {
            return;
        }

        int row = 1;
        int col = 1;

        if ((lineNumber != 0 && column != 0) || expr == null || expr.length == 0) {
            return;
        }

        for (int i = 0; i < cursor && i < expr.length; i++) {
            switch (expr[i]) {
            case '\r':
                continue;
            case '\n':
                row++;
                col = 1;
                break;

            default:
                col++;
            }
        }

        this.lineNumber = row;
        this.column = col;
    }

    private String generateErrorMessage() {
        StringBuilder appender = new StringBuilder().append("[Error: " + super.getMessage() + "]\n");

        int offset = appender.length();

        appender.append("[Near : {... ");

        offset = appender.length() - offset;

        appender.append(showCodeNearError(expr, cursor))
                .append(" ....}]\n")
                .append(repeatChar(' ', offset));

        if (msgOffset < 0) {
            msgOffset = 0;
        }

        appender.append(repeatChar(' ', msgOffset)).append('^');

        calcRowAndColumn();

        if (lineNumber != -1) {
            appender.append('\n')
                    .append("[Line: " + lineNumber + ", Column: " + (column) + "]");
        }
        return appender.toString();
    }

    public CharSequence getCodeNearError() {
        return showCodeNearError(expr, cursor);
    }

    public int getColumn() {
        return column;
    }

    public int getCursor() {
        return cursor;
    }

    public int getCursorOffet() {
        return this.msgOffset;
    }

    public List<ErrorDetail> getErrors() {
        return errors != null ? errors : Collections.<ErrorDetail> emptyList();
    }

    public char[] getExpr() {
        return expr;
    }

    public int getLastLineStart() {
        return lastLineStart;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    @Override
    public String getMessage() {
        return generateErrorMessage();
    }

    public void setColumn(final int column) {
        this.column = column;
    }

    public void setCursor(final int cursor) {
        this.cursor = cursor;
    }

    public void setErrors(final List<ErrorDetail> errors) {
        this.errors = errors;
    }

    public void setExpr(final char[] expr) {
        this.expr = expr;
    }

    public void setLastLineStart(final int lastLineStart) {
        this.lastLineStart = lastLineStart;
    }

    public void setLineNumber(final int lineNumber) {
        this.lineNumber = lineNumber;
    }

    private CharSequence showCodeNearError(final char[] expr, final int cursor) {
        if (expr == null) {
            return "Unknown";
        }

        int start = cursor - 20;
        int end = (cursor + 30);

        if (end > expr.length) {
            end = expr.length;
            start -= 30;
        }

        if (start < 0) {
            start = 0;
        }

        String cs;

        int firstCr;
        int lastCr;

        try {
            cs = copyValueOf(expr, start, end - start);
        } catch (StringIndexOutOfBoundsException e) {
            throw e;
        }

        int matchStart = -1;
        int matchOffset = 0;
        String match = null;

        if (cursor < end) {
            matchStart = cursor;
            if (matchStart > 0) {
                while (matchStart > 0 && !isWhitespace(expr[matchStart - 1])) {
                    matchStart--;
                }
            }

            matchOffset = cursor - matchStart;

            match = new String(expr, matchStart, expr.length - matchStart);
            Makematch: for (int i = 0; i < match.length(); i++) {
                switch (match.charAt(i)) {
                case '\n':
                case ')':
                    match = match.substring(0, i);
                    break Makematch;
                }
            }

            if (match.length() >= 30) {
                match = match.substring(0, 30);
            }
        }

        do {
            firstCr = cs.indexOf('\n');
            lastCr = cs.lastIndexOf('\n');

            if (firstCr == -1) {
                break;
            }

            int matchIndex = match == null ? 0 : cs.indexOf(match);

            if (firstCr != -1 && firstCr == lastCr) {
                if (firstCr > matchIndex) {
                    cs = cs.substring(0, firstCr);
                }
                else if (firstCr < matchIndex) {
                    cs = cs.substring(firstCr + 1, cs.length());
                }
            }
            else if (firstCr < matchIndex) {
                cs = cs.substring(firstCr + 1, lastCr);
            }
            else {
                cs = cs.substring(0, firstCr);
            }
        } while (true);

        String trimmed = cs.trim();

        if (match != null) {
            msgOffset = trimmed.indexOf(match) + matchOffset;
        }
        else {
            msgOffset = cs.length() - (cs.length() - trimmed.length());
        }

        if (msgOffset == 0 && matchOffset == 0) {
            msgOffset = cursor;
        }

        return trimmed;
    }

    @Override
    public String toString() {
        return generateErrorMessage();
    }
}
