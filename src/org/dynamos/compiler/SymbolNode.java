package org.dynamos.compiler;

import java.util.List;

import org.dynamos.structures.OpCode;

public class SymbolNode extends NamedNode {
	public SymbolNode(String name) {
		appendToName(name);
	}

	@Override
	public void compile(List<OpCode> opCodes, int tempNumber) {
		opCodes.add(new OpCode.Push( getSymbol() ));
	}
}
