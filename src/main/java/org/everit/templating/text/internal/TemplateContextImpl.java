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
package org.everit.templating.text.internal;

import java.io.StringWriter;
import java.util.Map;

import org.everit.templating.text.internal.res.Node;
import org.everit.templating.util.AbstractTemplateContext;
import org.everit.templating.util.TemplateWriter;

public class TemplateContextImpl extends AbstractTemplateContext {

  private final Map<String, Node> fragments;

  public TemplateContextImpl(final String fragment, final Map<String, Node> fragments,
      final Map<String, Object> vars) {
    super(fragment, vars);
    this.fragments = fragments;
  }

  @Override
  protected String renderFragmentInternal(final String fragmentId, final Map<String, Object> vars) {
    Node fragmentNode = fragments.get(fragmentId);
    if (fragmentNode == null) {
      throw new IllegalArgumentException("Unkown fragment: " + fragmentId);
    }

    StringWriter stringWriter = new StringWriter();
    fragmentNode.eval(new TemplateWriter(stringWriter), vars);

    return stringWriter.toString();
  }
}
