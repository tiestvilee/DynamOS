/**
 * 
 */
package org.dynamos.structures;

public class FunctionDOS extends ExecutableDOS {
    protected FunctionDefinitionDOS function;
    protected Context context;

    public FunctionDOS(FunctionDefinitionDOS function, Context context) {
        this.function = function;
        this.context = context;
        
        setFunction(Symbol.EXECUTE, new ExecuteFunction());
    }

    public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
        Context newContext = function.newContext(context, arguments, theObject);
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