package org.dynamos.compiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dynamos.structures.OpCode;
import org.dynamos.structures.Symbol;

public class FunctionCallNode extends NamedNode {
	
	private List<ASTNode> arguments = new ArrayList<ASTNode>();
	private FunctionCallNode chain;
	
	public List<ASTNode> getArguments() {
		return Collections.unmodifiableList(arguments);
	}

	public void addParameter(ASTNode node) {
		arguments.add(node);
	}
	
	public void setChain(FunctionCallNode chain) {
		this.chain = chain;
	}

	public FunctionCallNode getChain() {
		return chain;
	}
	
	public String toString() {
		return toString("");
	}
	
	public String toString(String indent) {
		String args = "";
		for(ASTNode argument : arguments) {
			args += argument.toString(indent + "  ");
		}
		String result = "(" + getName() + args + ")";
		
		if(chain != null) {
			return chain.toString(indent, result);
		}
		return result;
	}

	private String toString(String indent, String chaincalculation) {
		return "(" + chaincalculation + " " + toString(indent) + ")";
	}

	@Override
	public void compile(List<OpCode> opCodes, int tempNumber) {
		pushArguments(opCodes, tempNumber);
		callFunction(opCodes);
		
		if(chain != null) {
			callCompileChain(opCodes, tempNumber);
		}
	}

	public void compileChain(List<OpCode> opCodes, int tempNumber) {
		pushArguments(opCodes, tempNumber);
		opCodes.add(new OpCode.SetObject(Symbol.get("__temp" + tempNumber)));
		callFunction(opCodes);
		
		if(chain != null) {
			callCompileChain(opCodes, tempNumber);
		}
	}

	private void callCompileChain(List<OpCode> opCodes, int tempNumber) {
		opCodes.add(new OpCode.PushSymbol(Symbol.get("__temp" + (tempNumber + 1) )));
		opCodes.add(new OpCode.Push(Symbol.RESULT));
		opCodes.add(new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$));
		chain.compileChain(opCodes, tempNumber + 1);
	}

	private void callFunction(List<OpCode> opCodes) {
		opCodes.add(new OpCode.FunctionCall(getSymbol()));
	}
	
	private void pushArguments(List<OpCode> opCodes, int tempNumber) {
		List<SymbolNode> argumentSymbols = new ArrayList<SymbolNode>();
		int currentTempNumber = tempNumber;
		
		for(ASTNode argument : arguments) {
			if(argument instanceof SymbolNode) {
				argumentSymbols.add((SymbolNode) argument);
			} else if (argument instanceof FunctionCallNode) {
				currentTempNumber++;
				argument.compile(opCodes, currentTempNumber);
				opCodes.add(new OpCode.PushSymbol(Symbol.get("__temp" + currentTempNumber)));
				opCodes.add(new OpCode.Push(Symbol.RESULT));
				opCodes.add(new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$));

				argumentSymbols.add(new SymbolNode("__temp" + currentTempNumber));
			}
		}
		
		for(SymbolNode argument : argumentSymbols) {
			argument.compile(opCodes, tempNumber);
		}
	}
	
}
