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
