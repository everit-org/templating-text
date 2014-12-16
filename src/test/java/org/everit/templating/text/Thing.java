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

public class Thing {
    private MyEnum myEnum;

    private String name;

    public Thing(final String name) {
        this.name = name;
    }

    public MyEnum getMyEnum() {
        return myEnum;
    }

    public String getName() {
        return name;
    }

    public void setMyEnum(final MyEnum myEnum) {
        this.myEnum = myEnum;
    }

    public void setName(final String name) {
        this.name = name;
    }

}
