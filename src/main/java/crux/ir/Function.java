package crux.ir;

import crux.ast.FuncDef;
import crux.ast.types.FuncType;
import crux.ast.types.Type;

import java.util.*;
import java.util.stream.Collectors;

/**
 * A function, which is the lowered version of a {@link FuncDef}. The difference to the AST version
 * is, that the body of the function does not consist of a list of statements, but instead it is a
 * graph, in which instructions are nodes, and the control flow are the edges.
 */
@SuppressWarnings("UnstableApiUsage")
public final class Function implements Formattable, java.io.Serializable {
  static final long serialVersionUID = 12022L;
  private String mFuncName;
  private List<LocalVar> mArgs;
  private FuncType mFuncType;

  private static final int FUNC_FORMAT_INDENT = 2;
  private int mTempVarCounter, mTempAddressVarCounter;
  private Instruction startInstruction;

  public Function(String name, FuncType funcType) {
    mFuncName = name;
    mFuncType = funcType;
    mTempVarCounter = 0;
    mTempAddressVarCounter = 0;
  }

  public void setArguments(List<LocalVar> args) {
    mArgs = List.copyOf(args);
  }

  public List<LocalVar> getArguments() {
    return List.copyOf(mArgs);
  }

  public String getName() {
    return mFuncName;
  }

  public FuncType getFuncType() {
    return mFuncType;
  }

  public LocalVar getTempVar(Type type, String prefix) {
    var name = String.format("%s%d", prefix, mTempVarCounter++);
    return new LocalVar(type, name);
  }

  public LocalVar getTempVar(Type type) {
    var name = String.format("t%d", mTempVarCounter++);
    return new LocalVar(type, name);
  }

  public int getNumTempVars() {
    return mTempVarCounter;
  }

  public AddressVar getTempAddressVar(Type type) {
    var name = String.format("av%d", mTempAddressVarCounter++);
    return new AddressVar(type, name);
  }

  public int getNumTempAddressVars() {
    return mTempAddressVarCounter;
  }

  public Instruction getStart() {
    return startInstruction;
  }

  public void setStart(Instruction inst) {
    startInstruction = inst;
  }

  /**
   * This function assigns labels to Instruction objects in the CFG that will need them. The method
   * is intended for generating assembly code. Pass in a 1 element array that contains the start
   * number for the labels, This element will be updated to record the start number for the next
   * function. The method returns a HashMap that maps Instructions to labels if they need one.
   */

  public HashMap<Instruction, String> assignLabels(int count[]) {
    HashMap<Instruction, String> labelMap = new HashMap<>();
    Stack<Instruction> tovisit = new Stack<>();
    HashSet<Instruction> discovered = new HashSet<>();
    if (getStart() != null)
      tovisit.push(getStart());
    while (!tovisit.isEmpty()) {
      Instruction inst = tovisit.pop();

      for (int childIdx = 0; childIdx < inst.numNext(); childIdx++) {
        Instruction child = inst.getNext(childIdx);
        if (discovered.contains(child)) {
          // Found the node for a second time...need a label for merge points
          if (!labelMap.containsKey(child)) {
            labelMap.put(child, "L" + (++count[0]));
          }
        } else {
          discovered.add(child);
          tovisit.push(child);
          // Need a label for jump targets also
          if (childIdx == 1 && !labelMap.containsKey(child)) {
            labelMap.put(child, "L" + (++count[0]));
          }
        }
      }
    }
    return labelMap;
  }

  @Override
  public String format(java.util.function.Function<Value, String> valueFormatter) {
    var funcName = getName();
    var funcDotBuilder = new StringBuilder();
    var indent = FUNC_FORMAT_INDENT;
    funcDotBuilder.append(" ".repeat(indent)).append("subgraph cluster_").append(funcName)
        .append(" {\n");
    indent *= 2;

    // Styles
    funcDotBuilder.append(" ".repeat(indent)).append("style=filled;").append("color=lightgrey;")
        .append("node [style=filled, color=white];").append("\n");

    final var funcType = "function %%%s(%s) -> %s";
    var argStrStream = mArgs.stream().map(valueFormatter).collect(Collectors.toList());
    var argStr = String.join(",", argStrStream);
    var funcHeader = String.format(funcType, getName(), argStr, getFuncType().getRet());
    funcDotBuilder.append(" ".repeat(indent)).append(String.format("label=\"%s\";\n", funcHeader));

    // Print nodes
    int nodeCounter = 0;
    final var nodePrefix = funcName + "_n";
    Map<Instruction, String> nodeIdMap = new HashMap<>();
    // Only print edge labels for nodes that have multiple (out) edges
    Instruction start = getStart();
    Stack<Instruction> tovisit = new Stack<>();

    tovisit.add(start);
    nodeIdMap.put(start, nodePrefix + (nodeCounter++));

    while (!tovisit.isEmpty()) {
      Instruction inst = tovisit.pop();
      String srcId = nodeIdMap.get(inst);

      funcDotBuilder.append(" ".repeat(indent)).append(srcId).append(" [label=\"");
      funcDotBuilder.append(inst.format(valueFormatter)).append("\"];\n");

      for (int i = 0; i < inst.numNext(); i++) {
        Instruction dst = inst.getNext(i);
        if (!nodeIdMap.containsKey(dst)) {
          nodeIdMap.put(dst, nodePrefix + (nodeCounter++));
          tovisit.push(dst);
        }
        String dstId = nodeIdMap.get(dst);
        funcDotBuilder.append(" ".repeat(indent)).append(srcId).append(" -> ").append(dstId);
        if (inst.numNext() == 2) {
          funcDotBuilder.append(" [label=\"  ");
          if (i == 0)
            funcDotBuilder.append("False");
          else
            funcDotBuilder.append("True");
          funcDotBuilder.append("  \"]");
        }
        funcDotBuilder.append(";\n");
      }
    }

    // End
    indent /= 2;
    funcDotBuilder.append(" ".repeat(indent)).append("}\n");

    return funcDotBuilder.toString();
  }
}
