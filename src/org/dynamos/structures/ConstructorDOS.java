/**
 * 
 */
package org.dynamos.structures;

public class ConstructorDOS extends ExecutableDOS {
    protected FunctionDOS function;

    public ConstructorDOS(FunctionDOS function) {
        this.function = function;
        
        // TODO should move this into prototype
        setFunction(Symbol.EXECUTE, new ExecuteFunction());
    }

    public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
        return function.construct(arguments);
    }
    
    class ExecuteFunction extends ExecutableDOS {

		@Override
		public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
			return ConstructorDOS.this.execute(theObject, arguments);
		}
    	
    }
}