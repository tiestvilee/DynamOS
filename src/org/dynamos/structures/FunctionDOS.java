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
        private FunctionDOS function;
        private Context context;

        public ContextualFunctionDOS(FunctionDOS function, Context context) {
            this.function = function;
            this.context = context;
        }

        public ObjectDOS execute(List<Object> arguments) {
            return execute(null, arguments);
        }

        public ObjectDOS execute(ObjectDOS theObject, List<Object> arguments) {
            Context newContext = new Context();
            newContext.setParent(context);
            newContext.setArguments(arguments);
            newContext.setObject(theObject);
            function.execute(newContext);
            return newContext.getResult();
        }
    }

    /* is this needed? It's just a wrapper for 'binding' a function to an object
     Implementable in native code, so should probably get rid of it */
    public static class MethodDOS extends ObjectDOS {
        private ContextualFunctionDOS contextualFunction;
        private ObjectDOS object;

        public MethodDOS(ContextualFunctionDOS contextualFunction, ObjectDOS object) {
            this.contextualFunction = contextualFunction;
            this.object = object;
        }
        
        public ObjectDOS execute(List<Object> arguments) {
            return contextualFunction.execute(object, arguments);
        }
    }

}
