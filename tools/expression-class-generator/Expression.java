public interface Expression {

  interface Visitor<R> {
    R visitSourceCodeExpression(SourceCode expression);
    R visitConstantsBlockExpression(ConstantsBlock expression);
    R visitConstantDeclarationExpression(ConstantDeclaration expression);
    R visitConfigBlockBodyExpression(ConfigBlockBody expression);
    R visitConfigBlockExpression(ConfigBlock expression);
    R visitRecordDeclarationExpression(RecordDeclaration expression);
    R visitIdentifierExpression(Identifier expression);
    R visitRecordValueExpression(RecordValue expression);
    R visitConstantCallExpression(ConstantCall expression);
    R visitEscapedSequenceExpression(EscapedSequence expression);
    R visitCharactersSequenceExpression(CharactersSequence expression);
  }

  static class SourceCode implements Expression {
    SourceCode(Expression constantsBlock, Expression configBlockBody) {
      this.constantsBlock = constantsBlock;
      this.configBlockBody = configBlockBody;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitSourceCodeExpression(this);
    }

    final Expression constantsBlock;
    final Expression configBlockBody;
  }

  static class ConstantsBlock implements Expression {
    ConstantsBlock(List<Expression> constantDeclarations) {
      this.constantDeclarations = constantDeclarations;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitConstantsBlockExpression(this);
    }

    final List<Expression> constantDeclarations;
  }

  static class ConstantDeclaration implements Expression {
    ConstantDeclaration(Expression recordDeclaration) {
      this.recordDeclaration = recordDeclaration;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitConstantDeclarationExpression(this);
    }

    final Expression recordDeclaration;
  }

  static class ConfigBlockBody implements Expression {
    ConfigBlockBody(List<Expression> bodyExpressions) {
      this.bodyExpressions = bodyExpressions;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitConfigBlockBodyExpression(this);
    }

    final List<Expression> bodyExpressions;
  }

  static class ConfigBlock implements Expression {
    ConfigBlock(Expression identifier, Expression configBlockBody) {
      this.identifier = identifier;
      this.configBlockBody = configBlockBody;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitConfigBlockExpression(this);
    }

    final Expression identifier;
    final Expression configBlockBody;
  }

  static class RecordDeclaration implements Expression {
    RecordDeclaration(Expression identifier, Expression recordValue) {
      this.identifier = identifier;
      this.recordValue = recordValue;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitRecordDeclarationExpression(this);
    }

    final Expression identifier;
    final Expression recordValue;
  }

  static class Identifier implements Expression {
    Identifier(String text) {
      this.text = text;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitIdentifierExpression(this);
    }

    final String text;
  }

  static class RecordValue implements Expression {
    RecordValue(Expression constantCall, Expression escapedSequence, Expression charactersSequence) {
      this.constantCall = constantCall;
      this.escapedSequence = escapedSequence;
      this.charactersSequence = charactersSequence;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitRecordValueExpression(this);
    }

    final Expression constantCall;
    final Expression escapedSequence;
    final Expression charactersSequence;
  }

  static class ConstantCall implements Expression {
    ConstantCall(Expression identifier) {
      this.identifier = identifier;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitConstantCallExpression(this);
    }

    final Expression identifier;
  }

  static class EscapedSequence implements Expression {
    EscapedSequence(String text) {
      this.text = text;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitEscapedSequenceExpression(this);
    }

    final String text;
  }

  static class CharactersSequence implements Expression {
    CharactersSequence(String text) {
      this.text = text;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitCharactersSequenceExpression(this);
    }

    final String text;
  }

  <R> R accept(Visitor<R> visitor);

}
