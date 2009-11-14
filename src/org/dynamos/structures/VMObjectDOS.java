/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import org.dynamos.Environment;
import org.dynamos.structures.StandardObjects.ValueObject;


/**
 *
 * @author tiestvilee
 */
public class VMObjectDOS {


	public static final Symbol VM = Symbol.get("vm");
	
	public static final Symbol CONTEXTUALIZE_FUNCTION_$_IN_$ = Symbol.get("contextualizeFunction:in:");
	public static final Symbol NEW_OBJECT_WITH_PROTOTYPE_$ = Symbol.get("newObjectWithPrototype:");
	
	public static final Symbol ADD_$_TO_$ = Symbol.get("add:to:");
	public static final Symbol SUBTRACT_$_FROM_$ = Symbol.get("subtract:from:");
	public static final Symbol VALUE_$_IS_LESS_THAN_$ = Symbol.get("value:isLessThan:");

	public static final Symbol NEW_OBJECT = Symbol.get("newObject");

	
    public static ObjectDOS getVMObject(final Environment environment) {
        ExecutableDOS PRINT_FUNCTION = new ExecutableDOS() {
            @Override
            public ObjectDOS execute(ObjectDOS object, ListDOS arguments) {
                System.out.println(arguments.at(0));
                return environment.getNull();
            }
        };

        ExecutableDOS CONTEXTUALIZE_FUNCTION_EXEC = new ExecutableDOS() {
        	@Override
        	public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
        		FunctionDefinitionDOS function = (FunctionDefinitionDOS) arguments.at(0);
        		Context context = (Context) arguments.at(1);
        		return environment.createFunction(function, context);
        	}
        };

        ExecutableDOS NEW_OBJECT_WITH_PARENT_$_EXEC = new ExecutableDOS() {
        	@Override
        	public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
        		ObjectDOS result = environment.createNewObject();
        		result.setParent(arguments.at(0));
        		return result;
        	}
        };
        
        ExecutableDOS NEW_OBJECT_EXEC = new ExecutableDOS() {
        	@Override
        	public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
        		ObjectDOS result = environment.createNewObject();
        		return result;
        	}
        };
        
        ExecutableDOS ADD_EXEC = new ExecutableDOS() {
        	@Override
        	public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
        		int right = ((ValueObject) arguments.at(0)).getValue();
                int left = ((ValueObject) arguments.at(1)).getValue();
                return environment.createNewValueObject(left + right);
        	}
        };
        
        ExecutableDOS SUB_EXEC = new ExecutableDOS() {
        	@Override
        	public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
        		int right = ((ValueObject) arguments.at(0)).getValue();
                int left = ((ValueObject) arguments.at(1)).getValue();
                return environment.createNewValueObject(left - right);
        	}
        };
        
        ExecutableDOS IS_LESS_THAN_EXEC = new ExecutableDOS() {
        	@Override
        	public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
                int left = ((ValueObject) arguments.at(0)).getValue();
                int right = ((ValueObject) arguments.at(1)).getValue();
                return left < right ? environment.getTrue() : environment.getFalse();
        	}
        };
        
    	
        ObjectDOS virtualMachine = environment.createNewObject();
        virtualMachine.setFunction(Symbol.get("print"), PRINT_FUNCTION);
        
        virtualMachine.setFunction(CONTEXTUALIZE_FUNCTION_$_IN_$, CONTEXTUALIZE_FUNCTION_EXEC);
        virtualMachine.setFunction(NEW_OBJECT, NEW_OBJECT_EXEC);
        
        virtualMachine.setFunction(ADD_$_TO_$, ADD_EXEC);
        virtualMachine.setFunction(SUBTRACT_$_FROM_$, SUB_EXEC);
        virtualMachine.setFunction(VALUE_$_IS_LESS_THAN_$, IS_LESS_THAN_EXEC);
        
        
        System.out.println("initialized VM");
        return virtualMachine;
    }
}
