/**
 * 
 */
package org.dynamos.structures;

import java.util.List;

import org.dynamos.OpCodeInterpreter;

public class ConstructorDOS extends ExecutableDOS {
    protected FunctionDOS function;

    public ConstructorDOS(FunctionDOS function) {
        this.function = function;
        
        // TODO should move this into prototype
        setFunction(Symbol.EXECUTE, new ExecuteFunction());
    }

    public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, List<ObjectDOS> arguments) {
        return execute(interpreter, arguments);
    }
    
    public ObjectDOS execute(OpCodeInterpreter interpreter, List<ObjectDOS> arguments) {
        return function.construct(interpreter, arguments);
    }
    
    class ExecuteFunction extends ExecutableDOS {

		@Override
		public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, List<ObjectDOS> arguments) {
			return ConstructorDOS.this.execute(interpreter, theObject, arguments);
		}
    	
    }
}