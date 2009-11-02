/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import java.util.List;

import org.dynamos.OpCodeInterpreter;

/**
 *
 * @author tiestvilee
 */
public class FunctionDOS extends ObjectDOS {
    private OpCodeInterpreter interpreter;
    private OpCode[] opCodes;
	private final Symbol[] symbols;

    public FunctionDOS(OpCodeInterpreter interpreter, Symbol[] symbols, OpCode[] opCodes) {
        super();
        this.interpreter = interpreter;
		this.symbols = symbols;
        this.opCodes = opCodes;
    }

    public void execute(Context context) {
    	int index = 0;
    	int finalIndex = Math.min(context.arguments.size(), symbols.length);
    	for(;index < finalIndex; index++) {
    		context.setSlot(symbols[index], context.arguments.get(index));
    	}
    	for(;index < symbols.length; index++) {
    		context.setSlot(symbols[index], StandardObjects.UNDEFINED);
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

        public ObjectDOS execute(ObjectDOS theObject, List<ObjectDOS> arguments) {
            Context newContext = new Context();
            newContext.setParent(context);
            newContext.setArguments(arguments);
            newContext.setObject(theObject);
            function.execute(newContext);
            return newContext.getSlot(Symbol.RESULT);
        }
    }

}
