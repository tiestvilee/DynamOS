package org.dynamos.compiler;

import java.util.ArrayList;
import java.util.List;

import org.dynamos.OpCodeInterpreter;
import org.dynamos.structures.FunctionDOS;
import org.dynamos.structures.OpCode;
import org.dynamos.structures.Symbol;

public class BootstrapCompiler {
	
	/*
			"(function test: someParameter\n" +
			"  result: someParameter\n" +
			")";
	 */

	public FunctionDOS compile(OpCodeInterpreter interpreter, String program) {
		ASTNode ast = transformStringToAST(program);
		return transformASTToFunctionDefinition(interpreter, ast);
	}

	private ASTNode transformStringToAST(String program) {
		return new TransformStringToAST().transform(program);
	}
	
	private FunctionDOS transformASTToFunctionDefinition(OpCodeInterpreter interpreter, ASTNode ast) {
		List<Symbol> arguments = new ArrayList<Symbol>();
		List<OpCode> opcodes = new ArrayList<OpCode>();

		arguments.add(Symbol.get("p"));
		
		opcodes.add(new OpCode.PushSymbol(Symbol.get("p")));
		opcodes.add(new OpCode.FunctionCall(Symbol.GET_SLOT_$));
		
		return constructFunctionDefinition(interpreter, arguments, opcodes);
	}

	private FunctionDOS constructFunctionDefinition(OpCodeInterpreter interpreter, List<Symbol> arguments, List<OpCode> opcodes) {
		Symbol[] argumentsArray = arguments.toArray(new Symbol[] {});
		OpCode[] opCodeArray = opcodes.toArray(new OpCode[] {});
		return new FunctionDOS(argumentsArray, opCodeArray);
	}
}
