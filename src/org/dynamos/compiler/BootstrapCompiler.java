package org.dynamos.compiler;

import java.util.ArrayList;
import java.util.List;

import org.dynamos.structures.FunctionDOS;
import org.dynamos.structures.OpCode;
import org.dynamos.structures.Symbol;

public class BootstrapCompiler {

	public FunctionDOS compileFunction(String program) {
		List<Symbol> arguments = new ArrayList<Symbol>();
		List<OpCode> opCodes = new ArrayList<OpCode>();

		((AnonymousFunctionNode) transform(program).getStatements().get(0)).compile(opCodes, arguments);
		
		FunctionDOS function = constructFunction(arguments, opCodes);
		return function;
	}

	private StatementContainingNode transformStringToAST(String program) {
		return new TransformStringToAST().transform(program);
	}

	private FunctionDOS constructFunction(List<Symbol> arguments,
			List<OpCode> opcodes) {
		Symbol[] argumentsArray = arguments.toArray(new Symbol[] {});
		OpCode[] opCodeArray = opcodes.toArray(new OpCode[] {});
		
		System.out.println("*************** COMPILED *****************\n");
		System.out.println("args " + arguments);
		OpCode.printOpCodes(opCodeArray);
		
		
		return new FunctionDOS(argumentsArray, opCodeArray);
	}

	public StatementContainingNode transform(String program) {
		return transformStringToAST(program);
	}
}
