package org.dynamos.compiler;

import java.util.List;

import org.dynamos.structures.OpCode;
import org.dynamos.structures.Symbol;



public class ConstructorNode extends MessageNode {
	// a cosntructor node is a message node
	
	public String type() {
		return "object";
	}
	
	void assignToPublicInterface(List<OpCode> opCodes) {
		// not sure I want to do this as the default...
	}

	void createFunction(List<OpCode> opCodes) {
		opCodes.add(new OpCode.Push(Symbol.get("__argument_list"))); // create function
		opCodes.add(new OpCode.Push(Symbol.RESULT));
		opCodes.add(new OpCode.FunctionCall(Symbol.CREATE_CONSTRUCTOR_WITH_ARGUMENTS_$_OPCODES_$));
	}

}
