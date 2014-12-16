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

import java.util.ArrayList;
import java.util.List;

public class Bar {
    public static int staticMethod() {
        return 1;
    }

    private int age = 14;
    private String assignTest = "";
    private Integer[] intarray = new Integer[1];
    private String name = "dog";
    private List<Integer> testList = new ArrayList<Integer>();

    private boolean woof = true;

    @Override
    public boolean equals(final Object o) {
        return o instanceof Bar;
    }

    public int getAge() {
        return age;
    }

    public String getAssignTest() {
        return assignTest;
    }

    public Integer[] getIntarray() {
        return intarray;
    }

    public String getName() {
        return name;
    }

    public List<Integer> getTestList() {
        return testList;
    }

    public String happy() {
        return "happyBar";
    }

    public boolean isFoo(final Object obj) {
        return obj instanceof Foo;
    }

    public boolean isWoof() {
        return woof;
    }

    public void setAge(final int age) {
        this.age = age;
    }

    public void setAssignTest(final String assignTest) {
        this.assignTest = assignTest;
    }

    public void setIntarray(final Integer[] intarray) {
        this.intarray = intarray;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setTestList(final List<Integer> testList) {
        this.testList = testList;
    }

    public void setWoof(final boolean woof) {
        this.woof = woof;
    }
}
