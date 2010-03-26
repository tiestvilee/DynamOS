package org.dynamos.structures;

import org.dynamos.Environment;
import org.dynamos.OpCodeInterpreter;
import org.dynamos.types.StandardObjects;

import java.util.ArrayList;
import java.util.List;

public class MetaVM {

	public static final Symbol CONTEXTUALIZE_FUNCTION_$_IN_$ = Symbol.get("contextualizeFunction:in:");
	public static final Symbol NEW_OBJECT_WITH_PROTOTYPE_$ = Symbol.get("newObjectWithPrototype:");

	public static final Symbol SET_PARENT_TO_$_ON_$ = Symbol.get("setParentTo:On:");
	public static final Symbol GET_PARENT_ON_$ = Symbol.get("getParentOn:");
	public static final Symbol SET_TRAIT_$_TO_$_ON_$ = Symbol.get("setTrait:To:On:");
	public static final Symbol GET_TRAIT_$_ON_$ = Symbol.get("getTrait:On:");

	public static final Symbol SET_FUNCTION_$_TO_$_ON_$ = Symbol.get("setFunction:to:on:");
	public static final Symbol SET_SLOT_$_TO_$_ON_$ = Symbol.get("setSlot:to:on:");
	public static final Symbol GET_SLOT_$_ON_$ = Symbol.get("getSlot:on:");

	public static final Symbol GET_MIRROR_FOR_$ = Symbol.get("getMirrorFor:");


	public static final Symbol NEW_OBJECT = Symbol.get("newObject");

	public static final Symbol CREATE_FUNCTION_WITH_ARGUMENTS_$_OPCODES_$ = Symbol.get("createFunctionWithArguments:locals:opCodes:");

	public static final Symbol MIRROR_FOR_$ = Symbol.get("mirrorFor:");
	private static final Symbol MIRROR_PROTOTYPE = Symbol.get("mirrorPrototype");


	public static ObjectDOS initialiseMetaVM(final Environment environment) {
		ObjectDOS metaVM = environment.createNewObject();


        ExecutableDOS NEW_OBJECT_EXEC = new ExecutableDOS() {
        	@Override
        	public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, List<ObjectDOS> arguments) {
        		return environment.createNewObject();
        	}
        };

        ExecutableDOS CONTEXTUALIZE_FUNCTION_EXEC = new ExecutableDOS() {
        	@Override
        	public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, List<ObjectDOS> arguments) {
        		FunctionDOS function = (FunctionDOS) arguments.get(0);
        		Activation context = (Activation) arguments.get(1);
        		return environment.createFunctionWithContext(function, context);
        	}
        };

        ExecutableDOS CREATE_FUNCTION_WITH_ARGUMENTS_$_OPCODES_$_EXEC = new ExecutableDOS() {
        	@Override
        	public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, List<ObjectDOS> arguments) {
        		List<ObjectDOS> argumentList = StandardObjects.toJavaList(arguments.get(0));
        		List<ObjectDOS> opCodes = StandardObjects.toJavaList(arguments.get(1));

        		System.out.println("... creating funciton with \n\t" + argumentList + " \n\t" + opCodes);

				Symbol[] nativeArguments = copyListOfSymbolWrappersToArrayOfSymbols(argumentList);
				OpCode[] nativeOpCodes = copyListOfOpcodeWrappersToArrayOfOpcodes(opCodes);

				return environment.createFunction(nativeArguments, nativeOpCodes);
        	}
        };

		metaVM.setFunction(NEW_OBJECT, NEW_OBJECT_EXEC);
		metaVM.setFunction(CONTEXTUALIZE_FUNCTION_$_IN_$, CONTEXTUALIZE_FUNCTION_EXEC);
		metaVM.setFunction(CREATE_FUNCTION_WITH_ARGUMENTS_$_OPCODES_$, CREATE_FUNCTION_WITH_ARGUMENTS_$_OPCODES_$_EXEC);
        metaVM.setFunction(SET_FUNCTION_$_TO_$_ON_$, SET_FUNCTION_$_TO_$_ON_$_EXEC);

		metaVM.setFunction(SET_PARENT_TO_$_ON_$, SET_PARENT_TO_$_ON_$_EXEC);
		metaVM.setFunction(GET_PARENT_ON_$, GET_PARENT_ON_$_EXEC);
		metaVM.setFunction(SET_TRAIT_$_TO_$_ON_$, SET_TRAIT_$_TO_$_ON_$_EXEC);
		metaVM.setFunction(GET_TRAIT_$_ON_$, GET_TRAIT_$_ON_$_EXEC);

		metaVM.setSlot(MIRROR_PROTOTYPE, Mirror.createMirrorPrototype(environment, metaVM));
		metaVM.setFunction(GET_MIRROR_FOR_$, GET_MIRROR_FOR_$_EXEC);

		return metaVM;
	}



	private static ExecutableDOS SET_FUNCTION_$_TO_$_ON_$_EXEC = new ExecutableDOS() {
		@Override
		public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, List<ObjectDOS> arguments) {
			arguments.get(2).setFunction(((SymbolWrapper) arguments.get(0)).getSymbol(), (ExecutableDOS) arguments.get(1));
			return arguments.get(2);
		}
    };

    private static ExecutableDOS SET_SLOT_$_TO_$_EXEC = new ExecutableDOS() {
    	@Override
    	public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, List<ObjectDOS> arguments) {
    		Symbol symbol = ((SymbolWrapper) arguments.get(0)).getSymbol();
    		ObjectDOS value = arguments.get(1);

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
    	public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, List<ObjectDOS> arguments) {
    		Symbol symbol = ((SymbolWrapper) arguments.get(0)).getSymbol();
    		ObjectDOS value = arguments.get(1);

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
		public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, List<ObjectDOS> arguments) {
    		Symbol symbol = ((SymbolWrapper) arguments.get(0)).getSymbol();
			return theObject.getSlot(symbol);
		}
    };



    private static ExecutableDOS SET_PARENT_TO_$_ON_$_EXEC = new ExecutableDOS() {
		@Override
		public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, List<ObjectDOS> arguments) {
			System.out.println("************* setting parent on " + arguments.get(1) + " to " + arguments.get(0));
			arguments.get(1).setParent(arguments.get(0));
			return arguments.get(1); // never return the mirror - just in case
		}
    };

    private static ExecutableDOS GET_PARENT_ON_$_EXEC = new ExecutableDOS() {
		@Override
		public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, List<ObjectDOS> arguments) {
			return arguments.get(0).getParent();
		}
    };

    private static ExecutableDOS SET_TRAIT_$_TO_$_ON_$_EXEC = new ExecutableDOS() {
		@Override
		public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS  theObject, List<ObjectDOS> arguments) {
			arguments.get(2).setTrait(arguments.get(0).toString(), arguments.get(1));
			return arguments.get(2); // never return the mirror - just in case
		}
    };

    private static ExecutableDOS GET_TRAIT_$_ON_$_EXEC = new ExecutableDOS() {
		@Override
		public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, List<ObjectDOS> arguments) {
			return arguments.get(1).getTrait(arguments.get(0).toString());
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

    private static ExecutableDOS GET_MIRROR_FOR_$_EXEC = new ExecutableDOS() {
		@Override
		public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, List<ObjectDOS> arguments) {
			ObjectDOS mirrorPrototype = theObject.getSlot(MIRROR_PROTOTYPE);
			ObjectDOS mirror = theObject.getFunction(NEW_OBJECT).execute(interpreter, theObject, new ArrayList<ObjectDOS>());
			mirror.setParent(mirrorPrototype);
			mirror.setSlot(Mirror.MIRRORED_SLOT, arguments.get(0));
			return mirror;
		}
    };


}
