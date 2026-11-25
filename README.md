![E](http://www.everit.org/fonts/sbz_e.svg)

# Templating Text

## Introduction

Templating implementation to generate simple text output. The origin of the solution is the source of MVEL. The source is modified in the way that

- the solution implements [templating-api](https://github.com/everit-org/templating-api)
- any expression language that is bridged with Everit [expression-api](https://github.com/everit-org/expression-api) can be used within the templates
- have a very small binary (&lt;50k without the expression language)

---

## Usage

```java
// Get a template from somewhere (e.g.: from a file)
String text = "Hello @{name}!";

// Instantiate any expression compiler (e.g.: MVEL)
ExpressionCompiler expressionCompiler = new MvelExpressionCompiler();

// Instantiate template compiler and pass the expression compiler
TextTemplateCompiler compiler = new TextTemplateCompiler(expressionCompiler);

// Get a classloader that can be used to compile the template if necessary
ClassLoader cl = this.getClass().getClassLoader();

// Instantiate a ParserConfiguration
ParserConfiguration configuration = new ParserConfiguration(cl);

// Compile the template
CompiledTemplate template = compiler.compile(text, configuration);

/////////////////////////////////////////////////////////////////////////
// Rendering the template
/////////////////////////////////////////////////////////////////////////

// Create a map for the variables that can be used within the template
Map<String, Object> vars = new HashMap<>();
vars.put("name", "John Doe");

// Get a Writer from somewhere (e.g.: a ResponseWriter or a StringWriter)
Writer writer = new StringWriter();

// Render the template to the writer
template.render(writer, vars);

// In this example we write out the result to the standard output
System.out.println(writer.toString());
```

---

## Templating guide

### Template context

There is a special variable called *template_ctx*. That variable contains information about the ongoing render process. The *template_ctx* variable cannot be re-assigned or re-declared otherwise an exception will be thrown. See examples in the *Fragments* chapter below!

### Orb tags

In the text of templating, several tags can be inserted. E.g.:

```text
@tagName{expression}
```

The *@* tag can be escaped with an additional *@* character. E.g.:

```text
@@someText
```

### Expressions

Expressions are Orb tags where there is no name. The result of the expression is written to the output. E.g.:

```text
@{expression}
```

Any expression language can be used here that is configured for the templating engine.

### Code tags

Code blocks can be used to run statements and to declare / assign variables.

```text
@code{name = 'John'}
```

### If-else tags

Example:

```text
@if{foo != bar}
  Foo not a bar!
@else{bar != cat}
  Bar is not a cat!
@else{}
  Foo may be a Bar or a Cat!
@end{}
```

### Foreach

Foreach allows you to iterate through Iterable objects, Object arrays and primitive arrays. E.g.:

```text
@foreach{item : products}
  - @{item.serialNumber}
@end{}
```

You can also do multi-iteration:

```text
@foreach{var1 : set1, var2 : set2}
  @{var1}-@{var2}
@end{}
```

The end tag of the foreach may contain an expression. The result of the expression will be rendered to the end of each iteration except the last one:

```text
@foreach{item : people}@{item.name}@end{', '}
```

### Comments

```text
@comment{
  This is a comment
}
```

It might happen that a `}` character must be inserted into a comment. Make sure that it is either opened with a `{` character within a comment or it is written as a String (within apostrophes or quotes). E.g.:

```text
@comment{This is my special {} comment}
@comment{This is my other special '}' comment}
@comment{'This is my third special } comment'}
```

### Fragments

It is possible to define fragments within the template:

```text
@fragment{'fragmentId'}
  This is the content of the fragment
@end{}
```

You can render one of the fragments of the template only by passing the fragment id to the render method:

```java
template.render(writer, vars, fragmentId);
```

The fragment of a template can be rendered any time by using the template context variable:

```text
@{template_ctx.renderFragment('fragmentId')}
```

Variables can be defined that will be available during rendering the fragment. To do that a map must be passed as a second parameter for renderFragment method. Both the existing and the new variables will be accessible within the fragment:

```text
@{template_ctx.renderFragment('fragmentId', ['var1' : val1, 'var2', val2]);
```

Please note that the example above was written using MVEL. By using other expression language, the example might look different.

Fragments are always rendered, even if they were not called directly. To exclude the fragments when they are not directly called, place an if statement within them that is true when rendering was called with the specific fragment id:

```text
@fragment{'myFragment'}
  @if{template_ctx.fragmentId == 'myFragment'}
    This is only rendered, when the fragment is called
  @end{}
@{end}
```

---

## Download

The binaries with all of its dependencies are available at [Maven Central](http://search.maven.org/#artifactdetails%7Corg.everit.templating%7Corg.everit.templating.text%7C1.0.0%7Cbundle).

---
