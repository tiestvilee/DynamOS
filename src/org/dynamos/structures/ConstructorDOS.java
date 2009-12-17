/**
 * 
 */
package org.dynamos.structures;

public class ConstructorDOS extends ExecutableDOS {
    protected FunctionDOS function;

    public ConstructorDOS(FunctionDOS function, ObjectDOS context) {
        this.function = function;
        setContext(context);
        
        // TODO should move this into prototype
        setFunction(Symbol.EXECUTE, new ExecuteFunction());
    }

    public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
        ObjectDOS newObject = function.newObject(getContext(), arguments);
        function.execute(newObject);
        return newObject;
    }
    
    private class ExecuteFunction extends ExecutableDOS {

		@Override
		public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
			return ConstructorDOS.this.execute(theObject, arguments);
		}
    	
    }
}