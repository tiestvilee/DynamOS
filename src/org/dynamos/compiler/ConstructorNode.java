package org.dynamos.compiler;

import java.util.List;

import org.dynamos.structures.OpCode;
import org.dynamos.structures.Symbol;



public class ConstructorNode extends MessageNode {
	public String type() {
		return "object";
	}


	public void compile(List<OpCode> opCodes, List<Symbol> arguments) {
		for(String argument : getArguments()) {
			arguments.add(Symbol.get(argument));
		}
		for(ASTNode node : statements) {
			node.compile(opCodes, 0);
		}
	}

	void createFunction(List<OpCode> opCodes) {
		opCodes.add(new OpCode.Push(Symbol.get("__argument_list"))); // create function
		opCodes.add(new OpCode.Push(Symbol.RESULT));
		opCodes.add(new OpCode.FunctionCall(Symbol.CREATE_CONSTRUCTOR_WITH_ARGUMENTS_$_OPCODES_$));
	}

}
