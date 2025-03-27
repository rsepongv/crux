package crux.ast.traversal;

import crux.ast.*;

public interface NodeVisitor<T> {
  T visit(ArrayAccess arrayAccess);

  T visit(ArrayDecl arrayDecl);

  T visit(Assignment assignment);

  T visit(Break brk);

  T visit(FuncCall funcCall);

  T visit(Continue cont);

  T visit(DeclList declList);

  T visit(FuncDef funcDef);

  T visit(IfElseBranch ifElseBranch);

  T visit(LiteralBool literalBool);

  T visit(LiteralInt literalInt);

  T visit(OpExpr operation);

  T visit(Return ret);

  T visit(StatementList statementList);

  T visit(VarAccess vaccess);

  T visit(VarDecl varDecl);

  T visit(Loop loop);
}
