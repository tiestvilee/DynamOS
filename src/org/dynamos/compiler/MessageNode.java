package org.dynamos.compiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dynamos.structures.OpCode;
import org.dynamos.structures.Symbol;

public abstract class MessageNode extends StatementContainingNode {

	private List<String> arguments = new ArrayList<String>();
	private boolean isPrivate = false;
	
	public List<String> getArguments() {
		return Collections.unmodifiableList(arguments);
	}

	public void addParameter(String string) {
		arguments.add(string);
	}
	
	public Boolean isPrivate() {
		return isPrivate;
	}
	
	public void isPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate ;
	}
	
	public abstract String type();
	
	public String toString() {		
		return toString("");
	}

	public String toString(String indent) {		
		String statementsAsString = "";
		for(ASTNode statement: statements) {
			statementsAsString += statement.toString(indent + "  ") + " ";
		}
		return indent + "(" + type() + " " + getName() + "{\n" + indent + "  " + statementsAsString + "\n" + indent + "})\n";
	}


	@Override
	public void compile(List<OpCode> opCodes, int tempNumber) {
		setupArgumentList(opCodes);
		
		opCodes.add(new OpCode.StartOpCodeList()); // function body
		super.compile(opCodes, 0); // reset tempNumber because of scope
		opCodes.add(new OpCode.EndOpCodeList());
		
		createFunction(opCodes);
	    
		assignToSlotsOrFunctions(opCodes);
	
	}

	private void setupArgumentList(List<OpCode> opCodes) {
		opCodes.add(new OpCode.SetObject(Symbol.get("listFactory"))); // argument list
		opCodes.add(new OpCode.FunctionCall(Symbol.get("newList")));
		opCodes.add(new OpCode.PushSymbol(Symbol.get("__argument_list")));
		opCodes.add(new OpCode.Push(Symbol.RESULT));
		opCodes.add(new OpCode.FunctionCall(Symbol.SET_SLOT_$_TO_$));
		
		for(String argument : getArguments()) {
			opCodes.add(new OpCode.PushSymbol(Symbol.get(argument)));
			opCodes.add(new OpCode.SetObject(Symbol.get("__argument_list")));
			opCodes.add(new OpCode.FunctionCall(Symbol.get("add:")));
		}
	}
	
	abstract void createFunction(List<OpCode> opCodes);
	
	void assignToSlotsOrFunctions(List<OpCode> opCodes) {
		if(isPrivate) {
			opCodes.add(new OpCode.PushSymbol(Symbol.get(getName()))); // save into functions with correct name
			opCodes.add(new OpCode.Push(Symbol.RESULT));
			opCodes.add(new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$));
		} else {
			opCodes.add(new OpCode.PushSymbol(Symbol.get(getName()))); // save into functions with correct name
			opCodes.add(new OpCode.Push(Symbol.RESULT));
			opCodes.add(new OpCode.FunctionCall(Symbol.SET_LOCAL_FUNCTION_$_TO_$));
		}
	}


}
