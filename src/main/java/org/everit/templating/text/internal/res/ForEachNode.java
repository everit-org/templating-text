package org.everit.templating.text.internal.res;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.everit.expression.CompileException;
import org.everit.expression.CompiledExpression;
import org.everit.templating.text.internal.CompilableNodeHelper;
import org.everit.templating.text.internal.CompiledTemplateImpl;
import org.everit.templating.text.internal.InheritantMap;
import org.everit.templating.text.internal.TemplateWriter;
import org.everit.templating.text.internal.TextTemplateUtil;
import org.everit.templating.text.internal.UniversalIterable;

public class ForEachNode extends Node {
    private CompiledExpression[] ce;

    private CompiledExpression cSepExpr;

    private CompilableNodeHelper helper;

    private String[] item;

    public Node nestedNode;

    private char[] sepExpr;

    public ForEachNode(final int begin, final String name, final char[] template, final int start,
            final int end, final CompilableNodeHelper helper) {
        super(begin, name, template, start, end);
        this.helper = helper;
        configure();
    }

    private void configure() {
        ArrayList<String> items = new ArrayList<String>();
        ArrayList<String> expr = new ArrayList<String>();

        int start = cStart;
        for (int i = start; i < cEnd; i++) {
            switch (contents[i]) {
            case '(':
            case '[':
            case '{':
            case '"':
            case '\'':
                i = TextTemplateUtil.balancedCapture(contents, i, contents[i]);
                break;
            case ':':
                items.add(TextTemplateUtil.createStringTrimmed(contents, start, i - start));
                start = i + 1;
                break;
            case ',':
                if (expr.size() != (items.size() - 1)) {
                    throw new CompileException("unexpected character ',' in foreach tag", contents, cStart
                            + i);
                }
                expr.add(TextTemplateUtil.createStringTrimmed(contents, start, i - start));
                start = i + 1;
                break;
            }
        }

        if (start < cEnd) {
            if (expr.size() != (items.size() - 1)) {
                throw new CompileException("expected character ':' in foreach tag", contents, cEnd);
            }
            expr.add(TextTemplateUtil.createStringTrimmed(contents, start, cEnd - start));
        }

        item = new String[items.size()];
        int i = 0;
        for (String s : items) {
            item[i++] = s;
        }

        String[] expression;
        ce = new CompiledExpression[(expression = new String[expr.size()]).length];
        i = 0;
        for (String s : expr) {
            ce[i] = helper.getExpressionCompiler().compile(expression[i++] = s,
                    helper.generateParserConfiguration(cStart + 1));
        }
    }

    @Override
    public boolean demarcate(final Node terminatingnode, final char[] template) {
        nestedNode = next;
        next = terminus;

        sepExpr = terminatingnode.getContents();
        if (sepExpr.length == 0) {
            sepExpr = null;
        }
        else {
            cSepExpr = helper.getExpressionCompiler().compile(String.valueOf(sepExpr),
                    helper.generateParserConfiguration(cStart + 1));
        }

        return false;
    }

    @Override
    public Object eval(final CompiledTemplateImpl runtime, final TemplateWriter appender, final Object ctx,
            final Map<String, Object> vars) {

        Iterator<?>[] iters = new Iterator[item.length];

        for (int i = 0; i < iters.length; i++) {
            Object o = ce[i].eval(vars);
            iters[i] = new UniversalIterable<Object>(o).iterator();
        }

        Map<String, Object> localVars = new InheritantMap<String, Object>(vars, true);

        int iterate = iters.length;

        while (true) {
            for (int i = 0; i < iters.length; i++) {
                if (!iters[i].hasNext()) {
                    iterate--;
                    localVars.put(item[i], "");
                }
                else {
                    localVars.put(item[i], iters[i].next());
                }
            }
            if (iterate != 0) {
                nestedNode.eval(runtime, appender, ctx, localVars);

                if (sepExpr != null) {
                    for (Iterator<?> it : iters) {
                        if (it.hasNext()) {
                            appender.append(String.valueOf(cSepExpr.eval(vars)));
                            break;
                        }
                    }
                }
            } else {
                break;
            }
        }

        return next != null ? next.eval(runtime, appender, ctx, vars) : null;
    }

    public Node getNestedNode() {
        return nestedNode;
    }

    public void setNestedNode(final Node nestedNode) {
        this.nestedNode = nestedNode;
    }
}
