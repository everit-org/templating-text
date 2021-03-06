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

import java.io.Writer;
import java.util.Map;

import org.everit.expression.ParserConfiguration;
import org.everit.templating.CompiledTemplate;
import org.everit.templating.TemplateConstants;
import org.everit.templating.text.internal.res.Node;
import org.everit.templating.util.InheritantMap;
import org.everit.templating.util.TemplateWriter;

/**
 * This is the root of the template runtime, and contains various utility methods for executing
 * templates.
 */
public class CompiledTemplateImpl implements CompiledTemplate {

  private final Map<String, Node> fragments;

  private final ParserConfiguration parserConfiguration;

  private final Node rootNode;

  public CompiledTemplateImpl(final Node rootNode, final Map<String, Node> fragments,
      final ParserConfiguration parserConfiguration) {
    this.rootNode = rootNode;
    this.fragments = fragments;
    this.parserConfiguration = parserConfiguration;
  }

  @Override
  public void render(final Writer writer, final Map<String, Object> vars) {
    render(writer, vars, null);

  }

  @Override
  public void render(final Writer writer, final Map<String, Object> vars, final String fragmentId) {
    TemplateWriter templateWriter = new TemplateWriter(writer);

    InheritantMap<String, Object> scopedVars = new InheritantMap<String, Object>(vars, false);
    Node node;
    String tmpFragmentId;

    if ((fragmentId == null) || TemplateConstants.FRAGMENT_ROOT.equals(fragmentId)) {
      node = rootNode;
      tmpFragmentId = TemplateConstants.FRAGMENT_ROOT;
    } else {
      tmpFragmentId = fragmentId;
      node = fragments.get(fragmentId);
    }
    TemplateContextImpl templateContext =
        new TemplateContextImpl(tmpFragmentId, fragments, scopedVars, parserConfiguration);

    scopedVars.putWithoutChecks(TemplateConstants.VAR_TEMPLATE_CONTEXT, templateContext);
    node.eval(templateWriter, scopedVars);

  }

}
