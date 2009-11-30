/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import org.dynamos.OpCodeInterpreter;

/**
 *
 * @author tiestvilee
 */
public class FunctionDefinitionDOS extends ObjectDOS {
    private OpCodeInterpreter interpreter;
    private OpCode[] opCodes;
	private final Symbol[] arguments;
	private final Symbol[] locals;

	// TODO should have parent set to rootObject
    public FunctionDefinitionDOS(OpCodeInterpreter interpreter, Symbol[] arguments, Symbol[] locals, OpCode[] opCodes) {
        this.interpreter = interpreter;
		this.arguments = arguments;
		this.locals = locals;
        this.opCodes = opCodes;
    }

    public void execute(Context context) {
    	int index = 0;
    	int finalIndex = Math.min(context.arguments.size(), arguments.length);
    	for(;index < finalIndex; index++) {
    		context.setSlot(arguments[index], context.arguments.at(index));
    	}
    	for(;index < arguments.length; index++) {
    		context.setSlot(arguments[index], interpreter.getEnvironment().getUndefined());
    	}
    	for(index=0;index < locals.length; index++) {
    		context.setSlot(locals[index], interpreter.getEnvironment().getNull());
    	}
    	context.setSlot(Symbol.RESULT, context);
    	
        interpreter.interpret(context, opCodes);
    }

	public Context newContext(ObjectDOS context, ListDOS arguments, ObjectDOS theObject) {
		// TODO where should this stuff be set?  in the interpreter?
		Context newContext = interpreter.newContext();
        newContext.setContext(context);
        newContext.setArguments(arguments);
        newContext.setObject(theObject);
		return newContext;
	}

}
