package org.dynamos.compiler;

import java.util.ArrayList;
import java.util.List;

import org.dynamos.OpCodeInterpreter;
import org.dynamos.structures.FunctionDefinitionDOS;
import org.dynamos.structures.OpCode;
import org.dynamos.structures.Symbol;

public class BootstrapCompiler {
	
	/*
			"(function test: someParameter\n" +
			"  result: someParameter\n" +
			")";
	 */

	public FunctionDefinitionDOS compile(OpCodeInterpreter interpreter, String program) {
		ASTNode ast = transformStringToAST(program);
		return transformASTToFunctionDefinition(interpreter, ast);
	}

	private ASTNode transformStringToAST(String program) {
		return new TransformStringToAST().transform(program);
	}
	
	private FunctionDefinitionDOS transformASTToFunctionDefinition(OpCodeInterpreter interpreter, ASTNode ast) {
		List<Symbol> arguments = new ArrayList<Symbol>();
		List<Symbol> locals = new ArrayList<Symbol>();
		List<OpCode> opcodes = new ArrayList<OpCode>();

		arguments.add(Symbol.get("p"));
		
		opcodes.add(new OpCode.Push(Symbol.get("p")));
		opcodes.add(new OpCode.FunctionCall(Symbol.RESULT_$));
		
		return constructFunctionDefinition(interpreter, arguments, locals, opcodes);
	}

	private FunctionDefinitionDOS constructFunctionDefinition(OpCodeInterpreter interpreter, List<Symbol> arguments, List<Symbol> locals, List<OpCode> opcodes) {
		Symbol[] argumentsArray = arguments.toArray(new Symbol[] {});
		Symbol[] localsArray = locals.toArray(new Symbol[] {});
		OpCode[] opCodeArray = opcodes.toArray(new OpCode[] {});
		return new FunctionDefinitionDOS(interpreter, argumentsArray, localsArray, opCodeArray);
	}
}
