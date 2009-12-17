/**
 * 
 */
package org.dynamos.structures;

import org.dynamos.Environment;

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

    public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
        Activation activation = function.newActivation(context, arguments, theObject);
        function.execute(activation);
        return activation.getSlot(Symbol.RESULT);
    }
    
    class ExecuteFunction extends ExecutableDOS {

		@Override
		public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
			return FunctionWithContext.this.execute(theObject, arguments);
		}
    	
    }
    
    public static ObjectDOS createFunctionPrototype(Environment environment) {
    	ObjectDOS functionPrototype = environment.createNewObject();
    	
    	return functionPrototype;
    }
    
	
}