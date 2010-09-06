package org.dynamos.compiler;

import java.util.List;

import org.dynamos.structures.MetaVM;
import org.dynamos.structures.OpCode;
import org.dynamos.structures.Symbol;

public class NamedFunctionNode extends AnonymousFunctionNode {

    protected String type() {
        return "function";
    }

	protected void createFunction(List<OpCode> opCodes) {
		opCodes.add(new OpCode.Push(Symbol.get("__argument_list"))); // create function
		opCodes.add(new OpCode.Push(Symbol.RESULT));
        opCodes.add(new OpCode.FunctionCall(MetaVM.CREATE_FUNCTION_WITH_ARGUMENTS_$_OPCODES_$));
	}

	protected void assignToSlotsOrFunctions(List<OpCode> opCodes, int tempNumber) {
        opCodes.add(new OpCode.PushSymbol(Symbol.get(getName()))); // save into functions with correct name
        opCodes.add(new OpCode.Push(Symbol.RESULT));
		opCodes.add(new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$));
	}
}
