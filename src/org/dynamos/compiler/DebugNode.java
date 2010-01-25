package org.dynamos.compiler;

import java.util.List;

import org.dynamos.structures.OpCode;

public class DebugNode extends NamedNode {
	
	public DebugNode(String symbolName) {
		appendToName(symbolName);
	}

	@Override
	public void compile(List<OpCode> opCodes, int tempNumber) {
		opCodes.add(new OpCode.Debug("`````````````````!" + getName(), getSymbol()));
	}

}
