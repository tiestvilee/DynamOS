package org.dynamos.structures;

import java.util.List;

import org.dynamos.Environment;

public class Mirror {

	public static final Symbol CONTEXTUALIZE_FUNCTION_$_IN_$ = Symbol.get("contextualizeFunction:in:");
	public static final Symbol NEW_OBJECT_WITH_PROTOTYPE_$ = Symbol.get("newObjectWithPrototype:");

	public static final Symbol SET_PARENT_TO_$_ON_$ = Symbol.get("setParentTo:On:");
	public static final Symbol GET_PARENT_ON_$ = Symbol.get("getParentOn:");
	public static final Symbol SET_TRAIT_$_TO_$_ON_$ = Symbol.get("setTrait:To:On:");
	public static final Symbol GET_TRAIT_$_ON_$ = Symbol.get("getTrait:On:");


	public static final Symbol NEW_OBJECT = Symbol.get("newObject");

	public static final Symbol CREATE_FUNCTION_WITH_ARGUMENTS_$_OPCODES_$ = Symbol.get("createFunctionWithArguments:locals:opCodes:");

	public static final Symbol CREATE_CONSTRUCTOR_WITH_ARGUMENTS_$_OPCODES_$ = Symbol.get("createConstructorWithArguments:OpCodes:");


	public static ObjectDOS initialiseMirror(final Environment environment) {
		ObjectDOS mirror = environment.createNewObject();


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
        
		mirror.setFunction(CONTEXTUALIZE_FUNCTION_$_IN_$, CONTEXTUALIZE_FUNCTION_EXEC);
		mirror.setFunction(CREATE_FUNCTION_WITH_ARGUMENTS_$_OPCODES_$, CREATE_FUNCTION_WITH_ARGUMENTS_$_OPCODES_$_EXEC);
		mirror.setFunction(CREATE_CONSTRUCTOR_WITH_ARGUMENTS_$_OPCODES_$, CREATE_CONSTRUCTOR_WITH_ARGUMENTS_$_OPCODES_$_EXEC);
        
		mirror.setFunction(SET_PARENT_TO_$_ON_$, SET_PARENT_TO_$_ON_$_EXEC);
		mirror.setFunction(GET_PARENT_ON_$, GET_PARENT_ON_$_EXEC);
		mirror.setFunction(SET_TRAIT_$_TO_$_ON_$, SET_TRAIT_$_TO_$_ON_$_EXEC);
		mirror.setFunction(GET_TRAIT_$_ON_$, GET_TRAIT_$_ON_$_EXEC);
		
		return mirror;
	}
	
    private static ExecutableDOS SET_PARENT_TO_$_ON_$_EXEC = new ExecutableDOS() {
		@Override
		public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
			arguments.at(1).setParent(arguments.at(0));
			return arguments.at(1); // never return the mirror - just in case
		}
    };
    
    private static ExecutableDOS GET_PARENT_ON_$_EXEC = new ExecutableDOS() {
		@Override
		public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
			return arguments.at(0).getParent();
		}
    };
    
    private static ExecutableDOS SET_TRAIT_$_TO_$_ON_$_EXEC = new ExecutableDOS() {
		@Override
		public ObjectDOS execute(ObjectDOS  theObject, ListDOS arguments) {
			arguments.at(2).setTrait(arguments.at(0).toString(), arguments.at(1));
			return arguments.at(2); // never return the mirror - just in case
		}
    };
    
    private static ExecutableDOS GET_TRAIT_$_ON_$_EXEC = new ExecutableDOS() {
		@Override
		public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
			return arguments.at(1).getTrait(arguments.at(0).toString());
		}
    };
    
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
