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

import java.util.Collection;

public class Foo {
    public static final Bar STATIC_BAR = new Bar();
    public String aValue = "";
    private Bar bar = new Bar();
    private boolean boolTest = true;
    public String bValue = "";
    private char[] charArray;
    private char[][] charArrayMulti;
    private char charTest;
    public char charTestFld;
    private Collection collectionTest;
    private int countTest = 0;
    private String name = "dog";
    public Bar publicBar = new Bar();

    public String register;

    private SampleBean sampleBean = new SampleBean();

    public void abc() {
    }

    @Override
    public boolean equals(final Object o) {
        return o instanceof Foo;
    }

    public Bar getBar() {
        return bar;
    }

    public char[] getCharArray() {
        return charArray;
    }

    public char[][] getCharArrayMulti() {
        return charArrayMulti;
    }

    public char getCharTest() {
        return charTest;
    }

    public Collection getCollectionTest() {
        return collectionTest;
    }

    public int getCountTest() {
        return countTest;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return 4;
    }

    public SampleBean getSampleBean() {
        return sampleBean;
    }

    public String happy() {
        return "happyBar";
    }

    public boolean isBoolTest() {
        return boolTest;
    }

    public void setBar(final Bar bar) {
        this.bar = bar;
    }

    public void setBoolTest(final boolean boolTest) {
        this.boolTest = boolTest;
    }

    public void setCharArray(final char[] charArray) {
        this.charArray = charArray;
    }

    public void setCharArrayMulti(final char[][] charArrayMulti) {
        this.charArrayMulti = charArrayMulti;
    }

    public void setCharTest(final char charTest) {
        this.charTest = charTest;
    }

    public void setCollectionTest(final Collection collectionTest) {
        this.collectionTest = collectionTest;
    }

    public void setCountTest(final int countTest) {
        this.countTest = countTest;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setSampleBean(final SampleBean sampleBean) {
        this.sampleBean = sampleBean;
    }

    public String toUC(final String s) {
        register = s;
        return s.toUpperCase();
    }
}
