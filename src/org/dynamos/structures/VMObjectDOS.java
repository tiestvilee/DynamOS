/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import java.util.List;

import org.dynamos.Environment;
import org.dynamos.types.NumberDOS.ValueObject;


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

	public static final Symbol CREATE_FUNCTION_WITH_ARGUMENTS_$_OPCODES_$ = Symbol.get("createFunctionWithArguments:locals:opCodes:");

	public static final Symbol CREATE_CONSTRUCTOR_WITH_ARGUMENTS_$_OPCODES_$ = Symbol.get("createConstructorWithArguments:OpCodes:");

	
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
        		FunctionDOS function = (FunctionDOS) arguments.at(0);
        		Activation context = (Activation) arguments.at(1);
        		return environment.createFunctionWithContext(function, context);
        	}
        };

        ExecutableDOS CREATE_FUNCTION_WITH_ARGUMENTS_$_OPCODES_$_EXEC = new ExecutableDOS() {
        	@Override
        	public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
        		List<ObjectDOS> argumentList = ((ListDOS) arguments.at(0)).getRawList();
        		List<ObjectDOS> opCodes = ((ListDOS) arguments.at(1)).getRawList();
        		
        		System.out.println("... creating funciton with " + argumentList + " " + opCodes);

				Symbol[] nativeArguments = copyListOfSymbolWrappersToArrayOfSymbols(argumentList);
				OpCode[] nativeOpCodes = copyListOfOpcodeWrappersToArrayOfOpcodes(opCodes);
				
				return environment.createFunction(nativeArguments, nativeOpCodes);
        	}
        };

        ExecutableDOS CREATE_CONSTRUCTOR_WITH_ARGUMENTS_$_OPCODES_$_EXEC = new ExecutableDOS() {
        	@Override
        	public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
        		List<ObjectDOS> argumentList = ((ListDOS) arguments.at(0)).getRawList();
        		List<ObjectDOS> opCodes = ((ListDOS) arguments.at(1)).getRawList();
        		
        		System.out.println("... creating constructor with " + argumentList + " " + opCodes);

				Symbol[] nativeArguments = copyListOfSymbolWrappersToArrayOfSymbols(argumentList);
				OpCode[] nativeOpCodes = copyListOfOpcodeWrappersToArrayOfOpcodes(opCodes);
				
				return environment.createConstructor(nativeArguments, nativeOpCodes);
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
        virtualMachine.setFunction(CREATE_FUNCTION_WITH_ARGUMENTS_$_OPCODES_$, CREATE_FUNCTION_WITH_ARGUMENTS_$_OPCODES_$_EXEC);
        virtualMachine.setFunction(CREATE_CONSTRUCTOR_WITH_ARGUMENTS_$_OPCODES_$, CREATE_CONSTRUCTOR_WITH_ARGUMENTS_$_OPCODES_$_EXEC);
        
        virtualMachine.setFunction(ADD_$_TO_$, ADD_EXEC);
        virtualMachine.setFunction(SUBTRACT_$_FROM_$, SUB_EXEC);
        virtualMachine.setFunction(VALUE_$_IS_LESS_THAN_$, IS_LESS_THAN_EXEC);
        
        
        System.out.println("initialized VM");
        return virtualMachine;
    }
    
	static OpCode[] copyListOfOpcodeWrappersToArrayOfOpcodes(
			List<ObjectDOS> opCodes) {
		OpCode[] nativeOpCodes = new OpCode[opCodes.size()];
		int index;
		index = 0;
		for(ObjectDOS opCode : opCodes) {
			nativeOpCodes[index++] = ((OpCodeWrapper) opCode).getOpCode();
		}
		return nativeOpCodes;
	}

	static Symbol[] copyListOfSymbolWrappersToArrayOfSymbols(
			List<ObjectDOS> argumentList) {
		Symbol[] nativeArguments = new Symbol[argumentList.size()];
		int index = 0;
		for(ObjectDOS symbol : argumentList) {
			nativeArguments[index++] = ((SymbolWrapper) symbol).getSymbol();
		}
		return nativeArguments;
	}
}
