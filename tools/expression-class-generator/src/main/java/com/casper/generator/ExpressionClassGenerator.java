package com.casper.generator;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public final class ExpressionClassGenerator {

  private static final String EXPRESSION_CLASS_NAME = "Expression";

  // https://craftinginterpreters.com/representing-code.html#metaprogramming-the-trees
  public static void main(String[] args) throws IOException {
    defineAst(asList(
        "SourceCode          : Expression constantsBlock, Expression configBlockBody",
        "ConstantsBlock      : List<Expression> constantDeclarations",
        "ConstantDeclaration : Expression recordDeclaration",
        "ConfigBlockBody     : List<Expression> bodyExpressions",
        "ConfigBlock         : Expression identifier, Expression configBlockBody",
        "NameValuePair       : Expression recordDeclaration",
        "RecordDeclaration   : Expression identifier, Expression recordValue",
        "Identifier          : String text",
        "RecordValue         : Expression constantCall, Expression escapedSequence, Expression charactersSequence",
        "ConstantCall        : Expression identifier",
        "EscapedSequence     : Token escapedCharacter",
        "CharactersSequence  : String text"
    ));

    System.out.println(EXPRESSION_CLASS_NAME + ".java created!");
  }

  private static void defineAst(List<String> types) throws IOException {
    String path = EXPRESSION_CLASS_NAME + ".java";
    PrintWriter writer = new PrintWriter(path, StandardCharsets.UTF_8);

    writer.println("public interface " + EXPRESSION_CLASS_NAME + " {");
    writer.println();

    defineVisitor(writer, types);

    for (String type : types) {
      writer.println();
      String className = type.split(":")[0].trim();
      String fields = type.split(":")[1].trim();
      defineType(writer, className, fields);
    }

    // The base accept() method
    writer.println();
    writer.println("  <R> R accept(Visitor<R> visitor);");
    writer.println();
    writer.println("}");
    writer.close();
  }

  private static void defineVisitor(PrintWriter writer, List<String> types) {
    writer.println("  interface Visitor<R> {");

    for (String type : types) {
      String typeName = type.split(":")[0].trim();
      writer.println("    R visit" + typeName + EXPRESSION_CLASS_NAME + "(" +
          typeName + " " + EXPRESSION_CLASS_NAME.toLowerCase() + ");");
    }

    writer.println("  }");
  }

  private static void defineType(PrintWriter writer, String className, String fieldList) {
    writer.println("  static class " + className + " implements " + EXPRESSION_CLASS_NAME + " {");

    // Constructor
    writer.println("    " + className + "(" + fieldList + ") {");

    // Store parameters in fields
    String[] fields = fieldList.split(", ");
    for (String field : fields) {
      String name = field.split(" ")[1];
      writer.println("      this." + name + " = " + name + ";");
    }

    writer.println("    }");

    // Visitor pattern
    writer.println();
    writer.println("    @Override");
    writer.println("    <R> R accept(Visitor<R> visitor) {");
    writer.println("      return visitor.visit" +
        className + EXPRESSION_CLASS_NAME + "(this);");
    writer.println("    }");

    // Fields
    writer.println();
    for (String field : fields) {
      writer.println("    final " + field + ";");
    }

    writer.println("  }");
  }

}
