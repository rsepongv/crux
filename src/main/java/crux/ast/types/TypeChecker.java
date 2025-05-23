package crux.ast.types;

import crux.ast.SymbolTable.Symbol;
import crux.ast.*;
import crux.ast.traversal.NullNodeVisitor;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class will associate types with the AST nodes from Stage 2
 */
public final class TypeChecker {
  private final ArrayList<String> errors = new ArrayList<>();

  public ArrayList<String> getErrors() {
    return errors;
  }

  public void check(DeclList ast) {
    var inferenceVisitor = new TypeInferenceVisitor();
    inferenceVisitor.visit(ast);
  }

  /**
   * Helper function, should be used to add error into the errors array
   */
  private void addTypeError(Node n, String message) {
    errors.add(String.format("TypeError%s[%s]", n.getPosition(), message));
  }

  /**
   * Helper function, should be used to record Types if the Type is an ErrorType then it will call
   * addTypeError
   */
  private void setNodeType(Node n, Type ty) {
    ((BaseNode) n).setType(ty);
    if (ty.getClass() == ErrorType.class) {
      var error = (ErrorType) ty;
      addTypeError(n, error.getMessage());
    }
  }

  /**
   * Helper to retrieve Type from the map
   */
  public Type getType(Node n) {
    return ((BaseNode) n).getType();
  }


  /**
   * This calls will visit each AST node and try to resolve it's type with the help of the
   * symbolTable.
   */
  private final class TypeInferenceVisitor extends NullNodeVisitor<Void> {
    private Symbol currentFunctionSymbol;
    private Type currentFunctionReturnType;

    private boolean hasBreak;
    private boolean lastStatementReturns;

    @Override
    public Void visit(VarAccess vaccess) {
      return null;
    }

    @Override
    public Void visit(ArrayDecl arrayDecl) {
      return null;
    }

    @Override
    public Void visit(Assignment assignment) {
      return null;
    }

    @Override
    public Void visit(Break brk) {
      return null;
    }

    @Override
    public Void visit(FuncCall funcCall) {
      return null;
    }

    @Override
    public Void visit(Continue cont) {
      return null;
    }

    @Override
    public Void visit(DeclList declList) {
      return null;
    }

    @Override
    public Void visit(FuncDef funcDef) {
      return null;
    }

    @Override
    public Void visit(IfElseBranch ifElseBranch) {
      return null;
    }

    @Override
    public Void visit(ArrayAccess access) {
      return null;
    }

    @Override
    public Void visit(LiteralBool literalBool) {
      return null;
    }

    @Override
    public Void visit(LiteralInt literalInt) {
      return null;
    }

    @Override
    public Void visit(Loop loop) {
      return null;
    }

    @Override
    public Void visit(OpExpr op) {
      return null;
    }

    @Override
    public Void visit(Return ret) {
      return null;
    }

    @Override
    public Void visit(StatementList statementList) {
      return null;
    }

    @Override
    public Void visit(VarDecl varDecl) {
      return null;
    }

    private Type inferType(Node node) {
      node.accept(this);
      return ((BaseNode) node).getType();
    }
  }
}
