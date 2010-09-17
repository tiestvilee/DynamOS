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
        Activation activation = newActivation(suppliedArguments, theObject);
        function.execute(interpreter, suppliedArguments, activation, theObject);
        return activation.getSlot(Symbol.RESULT);
    }

	public Activation newActivation(List<ObjectDOS> suppliedArguments, ObjectDOS theObject) {
		// TODO where should this stuff be set?  in the interpreter?
		Activation activation = function.newActivation(suppliedArguments, theObject);
		activation.setContext(context);
		return activation;
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