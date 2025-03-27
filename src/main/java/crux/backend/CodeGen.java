package crux.backend;

import crux.ast.SymbolTable.Symbol;
import crux.ir.*;
import crux.ir.insts.*;
import crux.printing.IRValueFormatter;

import java.util.*;

/**
 * Convert the CFG into Assembly Instructions
 */
public final class CodeGen extends InstVisitor {
  private final IRValueFormatter irFormat = new IRValueFormatter();
  private final boolean isMacOS = false;

  private final String[] callingRegs = {"rdi", "rsi", "rdx", "rcx", "r8", "r9"};
  private final Program p;
  private final CodePrinter out;

  public CodeGen(Program p) {
    this.p = p;
    out = new CodePrinter("a.s");
  }

  /**
   * It should allocate space for globals call genCode for each Function
   */
  public void genCode() {
    // TODO: implement this function

    out.close();
  }

  private String getSymbol(Symbol var) {
    if (isMacOS)
      return "_" + var.getName();
    else
      return var.getName();
  }

  /**
   * It should generate code for function, use count as an int reference
   */
  private void genCode(Function f, int[] count) {
    currLabelMap = f.assignLabels(count);

    // TODO: implement this function
  }

  private HashMap<Instruction, String> currLabelMap;
  private Function currFunc;

  private void outputCode(Function f) {
    currFunc = f;
    Stack<Instruction> tovisit = new Stack<>();
    HashSet<Instruction> discovered = new HashSet<>();
    if (f.getStart() != null)
      tovisit.push(f.getStart());
    else
      processReturn(null);
    while (!tovisit.isEmpty()) {
      Instruction inst = tovisit.pop();
      if (currLabelMap.containsKey(inst)) {
        out.printLabel(currLabelMap.get(inst) + ":");
      }
      // Generate code for this instruction
      inst.accept(this);

      // Visit next instructions
      Instruction first = inst.getNext(0);
      Instruction second = inst.getNext(1);

      if (second != null && !discovered.contains(second)) {
        tovisit.push(second);
        discovered.add(second);
      }
      if (first != null && !discovered.contains(first)) {
        tovisit.push(first);
        discovered.add(first);
      } else if (first != null) {
        // Handle merge point
        if (tovisit.isEmpty() || first != tovisit.peek())
          out.printCode("jmp " + currLabelMap.get(first));
      } else {
        processReturn(null);
      }
    }
  }

  private HashMap<Variable, Integer> varIndexMap = new HashMap<>();
  private int varIndex = 0;

  private void resetVarIndexMap() {
    varIndexMap.clear();
    varIndex = 0;
  }

  private int getNumVars() {
    return varIndex;
  }

  private int getVariableIndex(Variable v) {
    if (!varIndexMap.containsKey(v)) {
      varIndexMap.put(v, ++varIndex);
    }
    return varIndexMap.get(v);
  }

  private String getVariableExpression(Variable v) {
    int offset = -8 * getVariableIndex(v);
    return offset + "(%rbp)";
  }

  private void moveVarToReg(Variable v, String reg) {
    out.printCode("movq " + getVariableExpression(v) + ", %" + reg);
  }

  private void moveIntToReg(Long v, String reg) {
    out.printCode("movq $" + v + ", %" + reg);
  }

  private void moveIntToReg(Integer v, String reg) {
    out.printCode("movq $" + v + ", %" + reg);
  }

  private void moveRegToVar(String reg, Variable v) {
    out.printCode("movq %" + reg + ", " + getVariableExpression(v));
  }

  private void processReturn(LocalVar var) {
    if (var != null) {
      moveVarToReg(var, "rax");
    }
    out.printCode("leave");
    out.printCode("ret");
  }

  public void visit(AddressAt i) {

  }

  public void visit(BinaryOperator i) {

  }

  public void visit(CompareInst i) {

  }

  public void visit(CopyInst i) {

  }

  public void visit(JumpInst i) {

  }

  public void visit(LoadInst i) {

  }

  public void visit(NopInst i) {

  }

  public void visit(StoreInst i) {

  }

  public void visit(ReturnInst i) {

  }

  public void visit(CallInst i) {

  }

  public void visit(UnaryNotInst i) {

  }

  private void printInstructionInfo(Instruction i) {
    var info = String.format("/* %s */", i.format(irFormat));
    out.printCode(info);
  }
}
