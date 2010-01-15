package org.dynamos.compiler;

import java.util.List;

import org.dynamos.structures.OpCode;
import org.dynamos.structures.Symbol;

public class SymbolGetterNode extends ChainedNode {
	
	public SymbolGetterNode(String name) {
		appendToName(name);
	}

	@Override
	public void compile(List<OpCode> opCodes, int tempNumber) {
		opCodes.add(new OpCode.PushSymbol(getSymbol())); 
		opCodes.add(new OpCode.FunctionCall(Symbol.GET_SLOT_$));

		if(getChain() != null) {
			int newTempNumber = tempNumber + 1;
			opCodes.add(new OpCode.PushSymbol(Symbol.get("__temp" + newTempNumber)));
			opCodes.add(new OpCode.Push(Symbol.RESULT));
			opCodes.add(new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$));
			
			getChain().compileChain(opCodes, newTempNumber);
		}
	}
}
