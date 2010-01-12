package org.dynamos.compiler;

import java.util.List;

import org.dynamos.structures.OpCode;
import org.dynamos.structures.Symbol;



public class FunctionNode extends MessageNode {

	public String type() {
		return "function";
	}
	
	void assignToPublicInterface(List<OpCode> opCodes) {
		opCodes.add(new OpCode.PushSymbol(Symbol.get(getName()))); // save into functions with correct name
		opCodes.add(new OpCode.Push(Symbol.RESULT));
		opCodes.add(new OpCode.FunctionCall(Symbol.SET_LOCAL_FUNCTION_$_TO_$));
	}

	void createFunction(List<OpCode> opCodes) {
		opCodes.add(new OpCode.Push(Symbol.get("__argument_list"))); // create function
		opCodes.add(new OpCode.Push(Symbol.RESULT));
		opCodes.add(new OpCode.FunctionCall(Symbol.CREATE_FUNCTION_WITH_ARGUMENTS_$_OPCODES_$));
	}
}
