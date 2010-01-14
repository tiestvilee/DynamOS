package org.dynamos.structures;

import java.util.List;

import org.dynamos.Environment;
import org.dynamos.OpCodeInterpreter;

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
        	public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, ListDOS arguments) {
        		FunctionDOS function = (FunctionDOS) arguments.at(0);
        		Activation context = (Activation) arguments.at(1);
        		return environment.createFunctionWithContext(function, context);
        	}
        };

        ExecutableDOS CREATE_FUNCTION_WITH_ARGUMENTS_$_OPCODES_$_EXEC = new ExecutableDOS() {
        	@Override
        	public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, ListDOS arguments) {
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
        	public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, ListDOS arguments) {
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
	

    private static ExecutableDOS SET_FUNCTION_$_TO_$_EXEC = new ExecutableDOS() {
		@Override
		public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, ListDOS arguments) {
			theObject.setFunction(((SymbolWrapper) arguments.at(0)).getSymbol(), (ExecutableDOS) arguments.at(1));
			return theObject;
		}
    };
    
    private static ExecutableDOS SET_SLOT_$_TO_$_EXEC = new ExecutableDOS() {
    	@Override
    	public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, ListDOS arguments) {
    		Symbol symbol = ((SymbolWrapper) arguments.at(0)).getSymbol();
    		ObjectDOS value = arguments.at(1);
    		
        	ObjectDOS objectWithSlot;
    		if(theObject instanceof Activation) {
    			/* implicit call */
	        	objectWithSlot = findSlotInContextChain(theObject, symbol);
    		} else {
    			/* explicit call */
    			// this currently must be allowed so that Constructor functions work
    			// TODO is this bad?  is this a big hole? setters are only available on contexts, so you could change temps in a context, yuk
    			objectWithSlot = theObject;
    		}
    		objectWithSlot.setSlot(symbol, value);
        	System.out.println("+++|| set slot " + symbol + " on " + objectWithSlot + " to " + value + " -> " + objectWithSlot.getSlot(symbol));
    		return objectWithSlot;
    	}

		private ObjectDOS findSlotInContextChain(ObjectDOS theObject, Symbol symbol) {
			Activation cursor = (Activation) theObject;
			while(cursor != null && cursor.getLocalSlot(symbol) == null)
			{
				cursor = cursor.getContext();
			}
			
			if(cursor == null) {
				System.out.println("using a victim " + ((Activation) theObject).getVictim());
				return ((Activation) theObject).getVictim();
			}
    		System.out.println("using an activation " + cursor);
			
			return cursor;
		}
    };
    
    private static ExecutableDOS SET_LOCAL_SLOT_$_TO_$_EXEC = new ExecutableDOS() {
    	@Override
    	public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, ListDOS arguments) {
    		Symbol symbol = ((SymbolWrapper) arguments.at(0)).getSymbol();
    		ObjectDOS value = arguments.at(1);
    		
    		if(theObject instanceof Activation) {
    			/* implicit call */
    			theObject.setSlot(symbol, value);
	        	System.out.println("+++|| set slot " + symbol + " on " + theObject + " to " + value + " -> " + theObject.getSlot(symbol));
	        	return theObject;
    		}
    		/* explicit call */
    		throw new RuntimeException("Can't set a slot directly on an object, only within that object's functions");
    	}
    };
    
    private static ExecutableDOS GET_SLOT_$_EXEC = new ExecutableDOS() {
		@Override
		public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, ListDOS arguments) {
    		Symbol symbol = ((SymbolWrapper) arguments.at(0)).getSymbol();
			return theObject.getSlot(symbol);
		}
    };


	
    private static ExecutableDOS SET_PARENT_TO_$_ON_$_EXEC = new ExecutableDOS() {
		@Override
		public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, ListDOS arguments) {
			arguments.at(1).setParent(arguments.at(0));
			return arguments.at(1); // never return the mirror - just in case
		}
    };
    
    private static ExecutableDOS GET_PARENT_ON_$_EXEC = new ExecutableDOS() {
		@Override
		public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, ListDOS arguments) {
			return arguments.at(0).getParent();
		}
    };
    
    private static ExecutableDOS SET_TRAIT_$_TO_$_ON_$_EXEC = new ExecutableDOS() {
		@Override
		public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS  theObject, ListDOS arguments) {
			arguments.at(2).setTrait(arguments.at(0).toString(), arguments.at(1));
			return arguments.at(2); // never return the mirror - just in case
		}
    };
    
    private static ExecutableDOS GET_TRAIT_$_ON_$_EXEC = new ExecutableDOS() {
		@Override
		public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, ListDOS arguments) {
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
