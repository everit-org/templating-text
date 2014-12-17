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
package org.everit.templating.text;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.everit.expression.ExpressionCompiler;
import org.everit.expression.ParserConfiguration;
import org.everit.expression.mvel.MvelExpressionCompiler;
import org.everit.templating.CompiledTemplate;
import org.everit.templating.util.InheritantMap;
import org.junit.Assert;
import org.junit.Test;

public class TextTemplatingTest {

    private static final Map<String, Object> TEST_MAP;

    static {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("_foo_", "Foo");
        map.put("_bar_", "Bar");

        ArrayList<String> list = new ArrayList<String>(3);
        list.add("Jane");
        list.add("John");
        list.add("Foo");

        map.put("arrayList", list);

        Foo foo = new Foo();
        foo.setBar(new Bar());
        map.put("foo", foo);
        map.put("a", null);
        map.put("b", null);
        map.put("c", "cat");
        map.put("BWAH", "");

        map.put("pi", "3.14");
        map.put("hour", "60");
        map.put("zero", 0);

        List<Thing> things = new ArrayList<Thing>();
        things.add(new Thing("Bob"));
        things.add(new Thing("Smith"));
        things.add(new Thing("Cow"));

        map.put("things", things);

        // noinspection UnnecessaryBoxing
        map.put("doubleTen", new Double(10));

        map.put("variable_with_underscore", "HELLO");

        map.put("testImpl",
                new TestInterface() {

                    @Override
                    public String getName() {
                        return "FOOBAR!";
                    }

                    @Override
                    public boolean isFoo() {
                        return true;
                    }
                });
        TEST_MAP = map;
    }

    @Test
    public void _001_testPassThru() {
        String s = "foobar!";
        Assert.assertEquals("foobar!", test(s));
    }

    @Test
    public void _002_testBasicParsing() {
        String s = "foo: @{_foo_}--@{_bar_}!";
        Assert.assertEquals("foo: Foo--Bar!", test(s));
    }

    @Test
    public void _003_testIfStatement() {
        String s = "@if{_foo_=='Foo'}Hello@end{}";
        Assert.assertEquals("Hello", test(s));
    }

    @Test
    public void _004_testIfStatement2() {
        String s = "@if{_foo_=='Bar'}Hello@else{_foo_=='Foo'}Goodbye@end{}";
        Assert.assertEquals("Goodbye", test(s));
    }

    @Test
    public void _005_testIfStatement3() {
        String s = "@if{_foo_=='Bar'}Hello@else{_foo_=='foo'}Goodbye@else{}Nope@end{}";
        Assert.assertEquals("Nope", test(s));
    }

    @Test
    public void _006_testIfStatement4() {
        String s = "@if{_foo_=='Foo'}Hello@else{_foo_=='foo'}Goodbye@else{}Nope@end{}End";
        Assert.assertEquals("HelloEnd", test(s));
    }

    @Test
    public void _007_testIfStatement5() {
        String s = "@if{_foo_=='foo'}Hello@end{}Goodbye";
        Assert.assertEquals("Goodbye", test(s));
    }

    @Test
    public void _008_testIfNesting() {
        String s = "@if{_foo_=='Foo'}Hello@if{_bar_=='Bar'}Bar@end{}@else{_foo_=='foo'}Goodbye@else{}Nope@end{}";
        Assert.assertEquals("HelloBar", test(s));
    }

    @Test
    public void _009_testForEach() {
        String s = "List:@foreach{item : arrayList}@{item}@end{}";
        Assert.assertEquals("List:JaneJohnFoo", test(s));
    }

    @Test
    public void _010_testForEachMulti() {
        String s = "Multi:@foreach{item : arrayList, item2 : arrayList}@{item}-@{item2}@end{','}:Multi";
        Assert.assertEquals("Multi:Jane-Jane,John-John,Foo-Foo:Multi", test(s));
    }

    @Test
    public void _011_testComplexTemplate() {
        String s = "@foreach{item : arrayList}@if{item[0] == 'J'}@{item}@end{}@end{}";
        Assert.assertEquals("JaneJohn", test(s));
    }

    @Test
    public void _012_testForEachException1() {
        String s = "<<@foreach{arrayList}@{item}@end{}>>";
        try {
            test(s);
        } catch (Exception e) {
            System.out.println(e.toString());
            return;
        }
        Assert.assertTrue(false);
    }

    @Test
    public void _013_testForEachException2() {
        String s = "<<@foreach{item:arrayList}@{item}>>";
        try {
            test(s);
        } catch (Exception e) {
            System.out.println(e.toString());
            return;
        }
        Assert.assertTrue(false);
    }

    @Test
    public void _014_testCode() {
        String s = "@code{a = 'foo'; b = 'bar'}@{a}@{b}";
        Assert.assertEquals("foobar", test(s));
    }

    @Test
    public void _015_testComments() {
        Assert.assertEquals("Foo", test("@comment{ This section is commented }@{_foo_}"));
    }

    @Test
    public void _016_testPassThru2() {
        Assert.assertEquals("foo@bar.com", test("foo@bar.com"));
    }

    @Test
    public void _017_testMethodOnValue() {
        Assert.assertEquals("DOG", test("@{foo.bar.name.toUpperCase()}"));
    }

    @Test
    public void _018_testSimpleProperty() {
        Assert.assertEquals("dog", test("@{foo.bar.name}"));
    }

    @Test
    public void _019_testBooleanOperator() {
        Assert.assertEquals(true, Boolean.valueOf(test("@{foo.bar.woof == true}")));
    }

    @Test
    public void _020_testBooleanOperator2() {
        Assert.assertEquals(false, Boolean.valueOf(test("@{foo.bar.woof == false}")));
    }

    @Test
    public void _021_testTextComparison() {
        Assert.assertEquals(true, Boolean.valueOf(test("@{foo.bar.name == 'dog'}")));
    }

    @Test
    public void _022_testChor() {
        Assert.assertEquals("cat", test("@{a or b or c}"));
    }

    @Test
    public void _023_testTemplating() {
        Assert.assertEquals("dogDOGGIE133.5", test("@{foo.bar.name}DOGGIE@{hour*2.225+1-1}"));
    }

    @Test
    public void _024_testIfStatement6() {
        Assert.assertEquals("sarah", test("@if{'fun' == 'fun'}sarah@end{}"));
    }

    @Test
    public void _025_testIfStatement7() {
        Assert.assertEquals("poo", test("@if{'fun' == 'bar'}sarah@else{}poo@end{}"));
    }

    @Test
    public void _026_testIfLoopInTemplate() {
        Assert.assertEquals(
                "ONETWOTHREE",
                test("@foreach{item :things}@if{item.name=='Bob'}ONE@elseif{item.name=='Smith'}TWO@elseif{item.name=='Cow'}THREE@end{}@end{}"));
    }

    @Test
    public void _027_testStringEscaping() {
        Assert.assertEquals("\"Mike Brock\"", test("@{\"\\\"Mike Brock\\\"\"}"));
    }

    @Test
    public void _028_testNestedAtSymbol() {
        Assert.assertEquals("email:foo@foo.com", test("email:@{'foo@foo.com'}"));
    }

    @Test
    public void _029_testEscape() {
        Assert.assertEquals("foo@foo.com", test("foo@@@{'foo.com'}"));
    }

    private CompiledTemplate compileExpression(final String template) {
        ExpressionCompiler expressionCompiler = new MvelExpressionCompiler();
        TextTemplateCompiler compiler = new TextTemplateCompiler(expressionCompiler);

        ParserConfiguration parserConfiguration = createParserConfiguration(1, 1);

        CompiledTemplate compiledTemplate = compiler.compile(template, parserConfiguration);
        return compiledTemplate;
    }

    private ParserConfiguration createParserConfiguration(final int lineNumber, final int column) {
        ParserConfiguration parserConfiguration = new ParserConfiguration(this.getClass().getClassLoader());
        parserConfiguration.setStartColumn(column);
        parserConfiguration.setStartRow(lineNumber);
        return parserConfiguration;
    }

    private String test(final String template) {
        CompiledTemplate compiledTemplate = compileExpression(template);
        StringWriter writer = new StringWriter();
        compiledTemplate.render(writer, new InheritantMap<String, Object>(TEST_MAP, false));
        return writer.toString();
    }
}
