/**
 * 
 */
package org.dynamos.structures;

import org.dynamos.OpCodeInterpreter;

public class ConstructorDOS extends ExecutableDOS {
    protected FunctionDOS function;

    public ConstructorDOS(FunctionDOS function) {
        this.function = function;
        
        // TODO should move this into prototype
        setFunction(Symbol.EXECUTE, new ExecuteFunction());
    }

    public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, ListDOS arguments) {
        return function.construct(interpreter, arguments);
    }
    
    class ExecuteFunction extends ExecutableDOS {

		@Override
		public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, ListDOS arguments) {
			return ConstructorDOS.this.execute(interpreter, theObject, arguments);
		}
    	
    }
}