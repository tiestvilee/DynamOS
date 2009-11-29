/**
 * 
 */
package org.dynamos.structures;

public class FunctionDOS extends ExecutableDOS {
    protected FunctionDefinitionDOS function;

    public FunctionDOS(FunctionDefinitionDOS function, Context context) {
        this.function = function;
        setContext(context);
        
        // TODO should move this into prototype
        setFunction(Symbol.EXECUTE, new ExecuteFunction());
    }

    public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
        Context newContext = function.newContext(getContext(), arguments, theObject);
        function.execute(newContext);
        return newContext.getSlot(Symbol.RESULT);
    }
    
    private class ExecuteFunction extends ExecutableDOS {

		@Override
		public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
			return FunctionDOS.this.execute(theObject, arguments);
		}
    	
    }
}