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
public class FunctionDOS extends ObjectDOS {
    private OpCodeInterpreter interpreter;
    private OpCode[] opCodes;
	private final Symbol[] arguments;
	private final Symbol[] locals;

    public FunctionDOS(OpCodeInterpreter interpreter, Symbol[] arguments, Symbol[] locals, OpCode[] opCodes) {
        super();
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
    		context.setSlot(arguments[index], StandardObjects.UNDEFINED);
    	}
    	for(index=0;index < locals.length; index++) {
    		context.setSlot(locals[index], StandardObjects.NULL);
    	}
        interpreter.interpret(context, opCodes);
    }

    public static class ContextualFunctionDOS extends ObjectDOS {
        protected FunctionDOS function;
        protected Context context;
        
        public ContextualFunctionDOS() {
        	
        }

        public ContextualFunctionDOS(FunctionDOS function, Context context) {
            this.function = function;
            this.context = context;
        }

        public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
            Context newContext = new Context();
            newContext.setParent(context);
            newContext.setArguments(arguments);
            newContext.setObject(theObject);
            function.execute(newContext);
            return newContext.getSlot(Symbol.RESULT);
        }
    }

}
