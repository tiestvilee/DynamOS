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

    public FunctionDOS(OpCodeInterpreter interpreter, OpCode[] opCodes) {
        super();
        this.interpreter = interpreter;
        this.opCodes = opCodes;
    }

    public void execute(Context context) {
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
            return newContext.getResult();
        }
    }

}
