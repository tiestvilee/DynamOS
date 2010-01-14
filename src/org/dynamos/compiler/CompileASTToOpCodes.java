package org.dynamos.compiler;

import java.util.ArrayList;
import java.util.List;

import org.dynamos.structures.FunctionDOS;
import org.dynamos.structures.OpCode;
import org.dynamos.structures.Symbol;

public class CompileASTToOpCodes {

	public FunctionDOS compile(StatementContainingNode root) {
		List<Symbol> arguments = new ArrayList<Symbol>();
		List<OpCode> opCodes = new ArrayList<OpCode>();

		root.compile(opCodes, 0);
		
		return new FunctionDOS(arguments.toArray(new Symbol[] {}), opCodes.toArray(new OpCode[] {}));
	}

}
