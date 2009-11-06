/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import org.dynamos.structures.StandardObjects.ValueObject;


/**
 *
 * @author tiestvilee
 */
public class VMObjectDOS {

    private static final ExecutableDOS PRINT_FUNCTION = new ExecutableDOS() {
        @Override
        public ObjectDOS execute(ObjectDOS object, ListDOS arguments) {
            System.out.println(arguments.at(0));
            return StandardObjects.NULL;
        }
    };

    private static final ExecutableDOS CONTEXTUALIZE_FUNCTION_EXEC = new ExecutableDOS() {
    	@Override
    	public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
    		FunctionDefinitionDOS function = (FunctionDefinitionDOS) arguments.at(0);
    		Context context = (Context) arguments.at(1);
    		return new FunctionDOS(function, context);
    	}
    };
    
    private static final ExecutableDOS ADD_EXEC = new ExecutableDOS() {
    	@Override
    	public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
    		int right = ((ValueObject) arguments.at(0)).getValue();
            int left = ((ValueObject) arguments.at(1)).getValue();
            return StandardObjects.numberDOS(left + right);
    	}
    };
    
    private static final ExecutableDOS SUB_EXEC = new ExecutableDOS() {
    	@Override
    	public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
    		int right = ((ValueObject) arguments.at(0)).getValue();
            int left = ((ValueObject) arguments.at(1)).getValue();
            return StandardObjects.numberDOS(left - right);
    	}
    };
    
    private static final ExecutableDOS IS_LESS_THAN_EXEC = new ExecutableDOS() {
    	@Override
    	public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
            int left = ((ValueObject) arguments.at(0)).getValue();
            int right = ((ValueObject) arguments.at(1)).getValue();
            return left < right ? StandardObjects.TRUE : StandardObjects.FALSE;
    	}
    };
    
	public static final Symbol VM = Symbol.get("vm");
	public static final Symbol CONTEXTUALIZE_FUNCTION = Symbol.get("contextualizeFunction:in:");
	public static final Symbol ADD = Symbol.get("add:to:");
	public static final Symbol SUB = Symbol.get("subtract:from:");
	public static final Symbol IS_LESS_THAN = Symbol.get("value:isLessThan:");

	
    public static ObjectDOS getVMObject() {
        ObjectDOS virtualMachine = new ObjectDOS();
        virtualMachine.setFunction(Symbol.get("print"), PRINT_FUNCTION);
        
        virtualMachine.setFunction(CONTEXTUALIZE_FUNCTION, CONTEXTUALIZE_FUNCTION_EXEC);
        
        virtualMachine.setFunction(ADD, ADD_EXEC);
        virtualMachine.setFunction(SUB, SUB_EXEC);
        virtualMachine.setFunction(IS_LESS_THAN, IS_LESS_THAN_EXEC);
        
        
        System.out.println("initialized VM");
        return virtualMachine;
    }
}
