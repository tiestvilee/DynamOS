package org.dynamos.compiler;

import java.util.List;

import org.dynamos.structures.MetaVM;
import org.dynamos.structures.OpCode;
import org.dynamos.structures.Symbol;


public class PrivateConstructorNode extends NamedFunctionNode {
	public String type() {
		return "private-constructor";
	}


	protected void addFunctionPrefix(List<OpCode> opCodes) {
		opCodes.add(new OpCode.FunctionCall(MetaVM.NEW_OBJECT));

		opCodes.add(new OpCode.PushSymbol(Symbol.get("__object")));
		opCodes.add(new OpCode.Push(Symbol.RESULT));
		opCodes.add(new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$));

		opCodes.add(new OpCode.Push(Symbol.get("__object")));
		opCodes.add(new OpCode.FunctionCall(MetaVM.MIRROR_FOR_$));

		opCodes.add(new OpCode.PushSymbol(Symbol.get("__mirror")));
		opCodes.add(new OpCode.Push(Symbol.RESULT));
		opCodes.add(new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$));

	}

	protected void addFunctionPostfix(List<OpCode> opCodes) {
		opCodes.add(new OpCode.PushSymbol(Symbol.get("__object")));
		opCodes.add(new OpCode.FunctionCall(Symbol.GET_SLOT_$));
	}

}