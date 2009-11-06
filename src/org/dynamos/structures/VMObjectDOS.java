/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;


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
    
    public static final ObjectDOS VirtualMachine;

	public static final Symbol VM = Symbol.get("vm");
	public static final Symbol CONTEXTUALIZE_FUNCTION = Symbol.get("contextualizeFunction:in:");
	
    static {
        VirtualMachine = new ObjectDOS();
        VirtualMachine.setFunction(Symbol.get("print"), PRINT_FUNCTION);
        
        VirtualMachine.setFunction(CONTEXTUALIZE_FUNCTION, CONTEXTUALIZE_FUNCTION_EXEC);
        
        
        System.out.println("initialized VM");
    }
}
