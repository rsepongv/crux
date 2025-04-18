package crux.ast.traversal;

import crux.ast.*;

public abstract class DefaultNodeVisitor<T> implements NodeVisitor<T> {
  protected abstract T visitDefault(Node node);

  @Override
  public T visit(ArrayAccess access) {
    return visitDefault(access);
  }

  @Override
  public T visit(ArrayDecl arrayDecl) {
    return visitDefault(arrayDecl);
  }

  @Override
  public T visit(Assignment assignment) {
    return visitDefault(assignment);
  }

  @Override
  public T visit(Break brk) {
    return visitDefault(brk);
  }

  @Override
  public T visit(FuncCall funcCall) {
    return visitDefault(funcCall);
  }

  @Override
  public T visit(Continue cont) {
    return visitDefault(cont);
  }

  @Override
  public T visit(DeclList declList) {
    return visitDefault(declList);
  }

  @Override
  public T visit(FuncDef funcDef) {
    return visitDefault(funcDef);
  }

  @Override
  public T visit(IfElseBranch ifElseBranch) {
    return visitDefault(ifElseBranch);
  }

  @Override
  public T visit(LiteralBool literalBool) {
    return visitDefault(literalBool);
  }

  @Override
  public T visit(LiteralInt literalInt) {
    return visitDefault(literalInt);
  }

  @Override
  public T visit(OpExpr op) {
    return visitDefault(op);
  }

  @Override
  public T visit(Return ret) {
    return visitDefault(ret);
  }

  @Override
  public T visit(StatementList statementList) {
    return visitDefault(statementList);
  }

  @Override
  public T visit(VarAccess vaccess) {
    return visitDefault(vaccess);
  }

  @Override
  public T visit(VarDecl varDecl) {
    return visitDefault(varDecl);
  }

  @Override
  public T visit(Loop loop) {
    return visitDefault(loop);
  }
}
