package org.dynamos.compiler;

import java.util.List;

import org.dynamos.structures.Mirror;
import org.dynamos.structures.OpCode;
import org.dynamos.structures.Symbol;

public class NamedMethodNode extends NamedFunctionNode {

	protected void assignToSlotsOrFunctions(List<OpCode> opCodes, int tempNumber) {
        opCodes.add(new OpCode.PushSymbol(Symbol.get(getName()))); // save into functions with correct name
        opCodes.add(new OpCode.Push(Symbol.RESULT));
        opCodes.add(new OpCode.SetObject(Symbol.get("__mirror")));
		opCodes.add(new OpCode.FunctionCall(Mirror.SET_FUNCTION_$_TO_$));
	}


}