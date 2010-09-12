/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import java.util.List;

import org.dynamos.Environment;
import org.dynamos.OpCodeInterpreter;
import org.dynamos.types.ValueObject;


/**
 *
 * @author tiestvilee
 */
public class VMObjectDOS {


	public static final Symbol VM = Symbol.get("vm");
	
	public static final Symbol ADD_$_TO_$ = Symbol.get("add:to:");
	public static final Symbol SUBTRACT_$_FROM_$ = Symbol.get("subtract:from:");
	public static final Symbol VALUE_$_IS_LESS_THAN_$ = Symbol.get("value:isLessThan:");
	
    public static ObjectDOS getVMObject(final Environment environment) {
        ExecutableDOS PRINT_FUNCTION = new ExecutableDOS() {
            @Override
            public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS object, List<ObjectDOS> arguments) {
                System.out.println(arguments.get(0));
                return environment.getNull();
            }
        };

        ExecutableDOS ADD_EXEC = new ExecutableDOS() {
        	@Override
        	public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, List<ObjectDOS> arguments) {
        		int right = ((ValueObject) arguments.get(0)).getValue();
                int left = ((ValueObject) arguments.get(1)).getValue();
                return environment.createNewValueObject(left + right);
        	}
        };
        
        ExecutableDOS SUB_EXEC = new ExecutableDOS() {
        	@Override
        	public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, List<ObjectDOS> arguments) {
        		int right = ((ValueObject) arguments.get(0)).getValue();
                int left = ((ValueObject) arguments.get(1)).getValue();
                return environment.createNewValueObject(left - right);
        	}
        };
        
        ExecutableDOS IS_LESS_THAN_EXEC = new ExecutableDOS() {
        	@Override
        	public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, List<ObjectDOS> arguments) {
                int left = ((ValueObject) arguments.get(0)).getValue();
                int right = ((ValueObject) arguments.get(1)).getValue();
                return left < right ? environment.getTrue() : environment.getFalse();
        	}
        };
        
    	
        ObjectDOS virtualMachine = environment.createNewObject();
        virtualMachine.setFunction(Symbol.get("print"), PRINT_FUNCTION);
        
        virtualMachine.setFunction(ADD_$_TO_$, ADD_EXEC);
        virtualMachine.setFunction(SUBTRACT_$_FROM_$, SUB_EXEC);
        virtualMachine.setFunction(VALUE_$_IS_LESS_THAN_$, IS_LESS_THAN_EXEC);
        
        
        System.out.println("initialized VM");
        return virtualMachine;
    }
}
