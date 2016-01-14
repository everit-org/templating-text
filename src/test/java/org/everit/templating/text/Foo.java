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
