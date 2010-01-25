/**
 * 
 */
package org.dynamos.structures;

import java.util.List;

import org.dynamos.Environment;
import org.dynamos.OpCodeInterpreter;

public class FunctionWithContext extends ExecutableDOS {
    protected FunctionDOS function;

	private final Activation context;

    public FunctionWithContext(FunctionDOS function, Activation context) {
        this.function = function;
//        if(context != null) {
//        	setContext(context);
//        }
		this.context = context;
        
        // TODO should move this into prototype
        setFunction(Symbol.EXECUTE, new ExecuteFunction());
    }

    public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, List<ObjectDOS> suppliedArguments) {
        Activation activation = function.newActivation(interpreter, context, suppliedArguments, theObject);
        function.execute(interpreter, suppliedArguments, activation);
        return activation.getSlot(Symbol.RESULT);
    }
    
    class ExecuteFunction extends ExecutableDOS {

		@Override
		public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, List<ObjectDOS> arguments) {
			return FunctionWithContext.this.execute(interpreter, theObject, arguments);
		}
    	
    }
    
    public static ObjectDOS createFunctionPrototype(Environment environment) {
    	ObjectDOS functionPrototype = environment.createNewObject();
    	
    	return functionPrototype;
    }
    
	
}