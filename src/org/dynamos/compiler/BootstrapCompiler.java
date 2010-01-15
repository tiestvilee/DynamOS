package org.dynamos.compiler;

import java.util.ArrayList;
import java.util.List;

import org.dynamos.structures.ConstructorDOS;
import org.dynamos.structures.FunctionDOS;
import org.dynamos.structures.OpCode;
import org.dynamos.structures.Symbol;

public class BootstrapCompiler {

	public ConstructorDOS compile(String program) {
		StatementContainingNode ast = transformStringToAST(program);
		return transformASTToFunctionDefinition(ast);
	}

	private StatementContainingNode transformStringToAST(String program) {
		return new TransformStringToAST().transform(program);
	}
	
	private ConstructorDOS transformASTToFunctionDefinition(StatementContainingNode ast) {
		List<Symbol> arguments = new ArrayList<Symbol>();
		List<OpCode> opCodes = new ArrayList<OpCode>();

		((ConstructorNode) ast.getStatements().get(0)).compile(opCodes, arguments);
		
		return constructFunctionDefinition(arguments, opCodes);
	}

	private ConstructorDOS constructFunctionDefinition(List<Symbol> arguments, List<OpCode> opcodes) {
		Symbol[] argumentsArray = arguments.toArray(new Symbol[] {});
		OpCode[] opCodeArray = opcodes.toArray(new OpCode[] {});
		
		OpCode.printOpCodes(opCodeArray);
		
		return new ConstructorDOS(new FunctionDOS(argumentsArray, opCodeArray));
	}
}
