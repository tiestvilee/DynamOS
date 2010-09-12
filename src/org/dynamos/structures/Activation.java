/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import java.util.List;

import org.dynamos.Environment;
import org.dynamos.OpCodeInterpreter;
import org.dynamos.types.StandardObjects;


/**
 *
 * @author tiestvilee
 */
public class Activation extends ObjectDOS {

	public static ActivationBuilder initializeActivation(Environment environment) {
		return new ActivationBuilder(environment);
	}

	public static class ActivationBuilder {

		private final ObjectDOS activationPrototype;

        private final Symbol functionDefinition = Symbol.get("functionDefinition");
        private final Symbol context = Symbol.get("context");

        private final Symbol object = Symbol.get("object");
        private final Symbol parent = Symbol.get("parent");
        private final Symbol name = Symbol.get("name");
        private final Symbol trait = Symbol.get("trait");

        private final Symbol argumentList = Symbol.get("argumentList");
        private final Symbol opcodes = Symbol.get("opcodes");

        private Environment environment;

		protected ActivationBuilder(Environment environment) {
			this.environment = environment;
			activationPrototype = environment.createNewObject();
			activationPrototype.setSlot(Symbol.RESULT, environment.getUndefined());

	        Activation contextContainingVM = createActivation();
	        // because the mirror is in the context, not a trait, we must delegate to its functions
	        contextContainingVM.setSlot(Symbol.META_VM, environment.getMetaVM());

	        activationPrototype.setSlot(Symbol.EMPTY_LIST, environment.getEmptyList());
	        activationPrototype.setFunction(Symbol.EMPTY_LIST,
	        		environment.createFunctionWithContext(
       				new Symbol[] {},
	        		new OpCode[] {
	        				new OpCode.PushSymbol(Symbol.EMPTY_LIST),
	        				new OpCode.FunctionCall(Symbol.GET_SLOT_$)
	        		},
	        		contextContainingVM));

	        activationPrototype.setFunction(MetaVM.CONTEXTUALIZE_FUNCTION_$_IN_$,
	        		environment.createFunctionWithContext(
       				new Symbol[] {functionDefinition, context},
	        		new OpCode[] {
	        				new OpCode.Push(functionDefinition),
	        				new OpCode.Push(context),
	        				new OpCode.SetObject(Symbol.META_VM),
	        				new OpCode.FunctionCall(MetaVM.CONTEXTUALIZE_FUNCTION_$_IN_$)
	        		},
	        		contextContainingVM));

			activationPrototype.setFunction(MetaVM.NEW_OBJECT,
					environment.createFunctionWithContext(
						new Symbol[] {context},
						new OpCode[] {
		        			new OpCode.Push(context),
							new OpCode.SetObject(Symbol.META_VM),
							new OpCode.FunctionCall(MetaVM.NEW_OBJECT)
						},
						contextContainingVM ));

			activationPrototype.setFunction(MetaVM.CREATE_FUNCTION_WITH_ARGUMENTS_$_OPCODES_$,
					environment.createFunctionWithContext(
						new Symbol[] {argumentList, opcodes},
						new OpCode[] {
	        				new OpCode.Push(argumentList),
	        				new OpCode.Push(opcodes),
							new OpCode.SetObject(Symbol.META_VM),
							new OpCode.FunctionCall(MetaVM.CREATE_FUNCTION_WITH_ARGUMENTS_$_OPCODES_$)
						},
						contextContainingVM ));

			activationPrototype.setFunction(MetaVM.MIRROR_FOR_$,
					environment.createFunctionWithContext(
						new Symbol[] {object},
						new OpCode[] {
		        			new OpCode.Push(object),
							new OpCode.SetObject(Symbol.META_VM),
							new OpCode.FunctionCall(MetaVM.GET_MIRROR_FOR_$)
						},
						contextContainingVM ));

			activationPrototype.setFunction(Symbol.SET_LOCAL_SLOT_$_TO_$, SET_LOCAL_SLOT_$_TO_$_EXEC);
			activationPrototype.setFunction(Symbol.SET_SLOT_$_TO_$, SET_SLOT_$_TO_$_EXEC);
			activationPrototype.setFunction(Symbol.GET_SLOT_$, GET_SLOT_$_EXEC);

		}

	    private static ExecutableDOS SET_LOCAL_FUNCTION_$_TO_$_EXEC = new ExecutableDOS() {
			@Override
			public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, List<ObjectDOS> arguments) {
				theObject.setFunction(((SymbolWrapper) arguments.get(0)).getSymbol(), (ExecutableDOS) arguments.get(1));
				return theObject;
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
	    			// TODO get rid of constructor functions, just use mirrors.
	    			objectWithSlot = theObject;
	    		}
	    		objectWithSlot.setSlot(symbol, value);
	        	System.out.println("+++|| set slot " + symbol + " on " + objectWithSlot + " to " + value + " -> " + objectWithSlot.getSlot(symbol));
	    		return objectWithSlot;
	    	}

			private ObjectDOS findSlotInContextChain(ObjectDOS theObject, Symbol symbol) {
				Activation cursor = (Activation) theObject;
				while(true)
				{
					cursor = cursor.getContext();
					if(cursor == null) {
						return theObject;
					}
					if(cursor.getLocalSlot(symbol) != null) {
						return cursor;
					}
				}
			}
	    };

	    private static ExecutableDOS SET_LOCAL_SLOT_$_TO_$_EXEC = new ExecutableDOS() {
	    	@Override
	    	public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, List<ObjectDOS> arguments) {
	    		Symbol symbol = ((SymbolWrapper) arguments.get(0)).getSymbol();
	    		ObjectDOS value = arguments.get(1);

    			theObject.setSlot(symbol, value);
	        	System.out.println("+++|| set slot " + symbol + " on " + theObject + " to " + value + " -> " + theObject.getSlot(symbol));
	        	return theObject;
	    	}
	    };

	    private static ExecutableDOS GET_SLOT_$_EXEC = new ExecutableDOS() {
			@Override
			public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, List<ObjectDOS> arguments) {
	    		Symbol symbol = ((SymbolWrapper) arguments.get(0)).getSymbol();
				ObjectDOS slot = theObject.getSlot(symbol);
				System.out.println("got slot " + slot);
				return slot;
			}
	    };

		public Activation createActivation() {
			Activation result = new Activation();
			result.setParent(activationPrototype);
			return result;
		}

	}

    public Activation() {
        setSlot(Symbol.CURRENT_CONTEXT, this);
    }

    public void setArguments(List<ObjectDOS> arguments) {
	    setSlot(Symbol.ARGUMENTS, StandardObjects.toDOSList(arguments));
	}

    public void setVictim(ObjectDOS object) {
        setSlot(Symbol.THIS, object);
    }

    public ObjectDOS getVictim() {
        return getSlot(Symbol.THIS);
    }

    public void setContext(Activation context) {
        setTrait("context", context);
    }

    public Activation getContext() {
    	return (Activation) getTrait("context");
    }

    public ObjectDOS getSlot(Symbol symbol) {
    	// TODO test for this...
        ObjectDOS slot = super.getSlot(symbol);
        if(slot == UNDEFINED) {
        	if(symbol == Symbol.THIS) {
        		throw new RuntimeException("couldn't find THIS, make sure you set a victim");
        	}
        	slot = getSlot(Symbol.THIS).getSlot(symbol);
        }
        return slot;
    }

    public ExecutableDOS getFunction(Symbol symbol) {
    	ExecutableDOS slot = super.getFunction(symbol);
        if(slot == null) {
        	slot = getSlot(Symbol.THIS).getFunction(symbol);
        }
        return slot;
    }


}
