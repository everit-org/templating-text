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

import java.util.HashMap;
import java.util.Map;

public class SampleBean {
    private final Map<String, Object> map = new HashMap<String, Object>();
    private Map<String, Integer> map2 = new HashMap<String, Integer>();

    public SampleBean() {
        map.put("bar", new Bar());
    }

    public Map<String, Integer> getMap2() {
        return map2;
    }

    public Object getProperty(final String name) {
        return map.get(name);
    }

    public void setMap2(final Map<String, Integer> map2) {
        this.map2 = map2;
    }

    public Object setProperty(final String name, final Object value) {
        map.put(name, value);
        return value;
    }
}
