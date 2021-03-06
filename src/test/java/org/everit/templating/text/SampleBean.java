/*
 * Copyright (C) 2011 Everit Kft. (http://www.everit.biz)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
