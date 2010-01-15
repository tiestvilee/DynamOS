package org.dynamos.compiler;

import java.util.List;

import org.dynamos.structures.OpCode;
import org.dynamos.structures.Symbol;

public class SymbolSetterNode extends FunctionCallNode {
	
	public SymbolSetterNode(String name) {
		appendToName(name);
	}

	@Override
	public void compile(List<OpCode> opCodes, int tempNumber) {
		getArguments().get(0).compile(opCodes, tempNumber); // assume result of this is in result...
		
		opCodes.add(new OpCode.PushSymbol(getSymbol()));
		opCodes.add(new OpCode.Push(Symbol.RESULT));
		opCodes.add(new OpCode.FunctionCall(Symbol.SET_SLOT_$_TO_$));
	}
}
