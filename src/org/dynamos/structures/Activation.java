/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import org.dynamos.Environment;
import org.dynamos.OpCodeInterpreter;


/**
 *
 * @author tiestvilee
 */
public class Activation extends ObjectDOS {
	
	public static ActivationBuilder initializeContext(OpCodeInterpreter interpreter, Environment environment) {
		return new ActivationBuilder(interpreter, environment);
	}
	
	public static class ActivationBuilder {

		private final ObjectDOS activationPrototype;

        private final Symbol functionDefinition = Symbol.get("functionDefinition");
        private final Symbol context = Symbol.get("context");
        private final Symbol argumentList = Symbol.get("argumentList");
        private final Symbol opcodes = Symbol.get("opcodes");
        
		protected ActivationBuilder(OpCodeInterpreter interpreter, Environment environment) {
			activationPrototype = environment.createNewObject();
			activationPrototype.setSlot(Symbol.RESULT, environment.getUndefined());
			
	        Activation contextContainingVM = createActivation();
	        contextContainingVM.setSlot(VMObjectDOS.VM, environment.getVirtualMachine());
	        
	        activationPrototype.setFunction(Symbol.CONTEXTUALIZE_FUNCTION_$_IN_$, 
	        		environment.createFunctionWithContext(
       				new Symbol[] {functionDefinition, context},
	        		new OpCode[] {
	        				new OpCode.Push(functionDefinition),
	        				new OpCode.Push(context),
	        				new OpCode.SetObject(VMObjectDOS.VM),
	        				new OpCode.FunctionCall(VMObjectDOS.CONTEXTUALIZE_FUNCTION_$_IN_$)
	        		},
	        		contextContainingVM));

			activationPrototype.setFunction(Symbol.NEW_OBJECT, 
					environment.createFunctionWithContext( 
						new Symbol[] {context},
						new OpCode[] {
		        			new OpCode.Push(context),
							new OpCode.SetObject(VMObjectDOS.VM),
							new OpCode.FunctionCall(VMObjectDOS.NEW_OBJECT)
						}, 
						contextContainingVM ));
			
			activationPrototype.setFunction(Symbol.CREATE_FUNCTION_WITH_ARGUMENTS_$_OPCODES_$, 
					environment.createFunctionWithContext( 
						new Symbol[] {argumentList, opcodes}, 
						new OpCode[] {
	        				new OpCode.Push(argumentList),
	        				new OpCode.Push(opcodes),
							new OpCode.SetObject(VMObjectDOS.VM),
							new OpCode.FunctionCall(VMObjectDOS.CREATE_FUNCTION_WITH_ARGUMENTS_$_OPCODES_$)
						}, 
						contextContainingVM ));
			
			activationPrototype.setFunction(Symbol.CREATE_CONSTRUCTOR_WITH_ARGUMENTS_$_OPCODES_$, 
					environment.createFunctionWithContext( 
						new Symbol[] {argumentList, opcodes, context}, 
						new OpCode[] {
	        				new OpCode.Push(argumentList),
	        				new OpCode.Push(opcodes),
	        				new OpCode.Push(context),
							new OpCode.SetObject(VMObjectDOS.VM),
							new OpCode.FunctionCall(VMObjectDOS.CREATE_CONSTRUCTOR_WITH_ARGUMENTS_$_OPCODES_$)
						}, 
						contextContainingVM ));
			

			activationPrototype.setFunction(Symbol.SET_FUNCTION_$_TO_$, SET_FUNCTION_$_TO_$_EXEC);
			activationPrototype.setFunction(Symbol.SET_LOCAL_SLOT_$_TO_$, SET_LOCAL_SLOT_$_TO_$_EXEC);
			activationPrototype.setFunction(Symbol.SET_SLOT_$_TO_$, SET_SLOT_$_TO_$_EXEC);
			activationPrototype.setFunction(Symbol.GET_SLOT_$, GET_SLOT_$_EXEC);
			
		}
	    
	    private static ExecutableDOS SET_FUNCTION_$_TO_$_EXEC = new ExecutableDOS() {
			@Override
			public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
				theObject.setFunction(((SymbolWrapper) arguments.at(0)).getSymbol(), (ExecutableDOS) arguments.at(1));
				return theObject;
			}
	    };
	    
	    private static ExecutableDOS SET_SLOT_$_TO_$_EXEC = new ExecutableDOS() {
	    	@Override
	    	public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
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
					cursor = (Activation) cursor.getContext();
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
	    	public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
	    		Symbol symbol = ((SymbolWrapper) arguments.at(0)).getSymbol();
	    		ObjectDOS value = arguments.at(1);
	    		
	    		if(theObject instanceof Activation) {
	    			/* implicit call */
	    			theObject.setSlot(symbol, value);
		        	System.out.println("+++|| set slot " + symbol + " on " + theObject + " to " + value + " -> " + theObject.getSlot(symbol));
		        	return theObject;
	    		} else {
	    			/* explicit call */
	    			throw new RuntimeException("Can't set a slot directly on an object, only within that object's functions");
	    		}
	    	}
	    };
	    
	    private static ExecutableDOS GET_SLOT_$_EXEC = new ExecutableDOS() {
			@Override
			public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
	    		Symbol symbol = ((SymbolWrapper) arguments.at(0)).getSymbol();
				return theObject.getSlot(symbol);
			}
	    };
		
		public Activation createActivation() {
			Activation result = new Activation();
			result.setParent(activationPrototype);
			return result;
		}
		
	}
    
    public Activation() {
        setSlot(Symbol.ARGUMENTS, new ListDOS());
        setSlot(Symbol.CURRENT_CONTEXT, this);
    }
    
    // TODO can these be just slots?
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
