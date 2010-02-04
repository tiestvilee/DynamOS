package org.dynamos.compiler;

import java.util.List;

import org.dynamos.structures.OpCode;
import org.dynamos.structures.Symbol;

public abstract class NamedFunctionNode extends AnonymousFunctionNode {

	private boolean isPrivate = false;
	
	public Boolean isPrivate() {
		return isPrivate;
	}
	
	public void isPrivate(boolean isPrivateValue) {
		this.isPrivate = isPrivateValue ;
	}
	
	public abstract String type();
	
	public String toString() {		
		return toString("");
	}

	public String toString(String indent) {		
		String statementsAsString = "";
		for(ASTNode statement: statements) {
			statementsAsString += statement.toString(indent + "  ") + " ";
		}
		return indent + "(" + type() + " " + getName() + "{\n" + indent + "  " + statementsAsString + "\n" + indent + "})\n";
	}

	protected void assignToSlotsOrFunctions(List<OpCode> opCodes, int tempNumber) {
		if(isPrivate) {
			opCodes.add(new OpCode.PushSymbol(Symbol.get(getName()))); // save into functions with correct name
			opCodes.add(new OpCode.Push(Symbol.RESULT));
			opCodes.add(new OpCode.FunctionCall(Symbol.SET_SLOT_$_TO_$));
		} else {
			opCodes.add(new OpCode.PushSymbol(Symbol.get(getName()))); // save into functions with correct name
			opCodes.add(new OpCode.Push(Symbol.RESULT));
			opCodes.add(new OpCode.FunctionCall(Symbol.SET_LOCAL_FUNCTION_$_TO_$));
		}
	}


}
