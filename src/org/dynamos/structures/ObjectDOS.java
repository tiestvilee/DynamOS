/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import java.util.HashMap;

/**
 *
 * @author tiestvilee
 */
public class ObjectDOS {
	
	/* Compile time definition of Object */
	
    private HashMap<Symbol, ObjectDOS> slots;
    private HashMap<Symbol, ExecutableDOS> functions;
    private ObjectDOS parent;
	private ObjectDOS nullObject;
	
	public ObjectDOS(ObjectDOS nullObject) {
		this();
		this.nullObject = nullObject;
	}

    public ObjectDOS() {
		slots = new HashMap<Symbol, ObjectDOS>();
        functions = new HashMap<Symbol, ExecutableDOS>();
    }

    public void setSlot(Symbol symbol, ObjectDOS value) {
    	if(slots.get(symbol) == null) {
    		setFunction(symbol, new StandardFunctions.Getter(symbol));
    		setFunction(symbol.toSetterSymbol(), new StandardFunctions.Setter(symbol.toSetterSymbol()));
    	}
        slots.put(symbol, value);
    }
    
    public ObjectDOS getSlot(Symbol symbol) {
        final ObjectDOS slot = slots.get(symbol);
        if(slot == null) {
            if(parent == null) {
                return nullObject; // TODO this sux, only gets here for the top object, otherwise returns null :(
            }
            return parent.getSlot(symbol);
        }
        return slot;
    }

    public void setParent(ObjectDOS parent) {
        this.parent = parent;
    }

    public ObjectDOS getParent() {
        if(parent == null) {
            return this;
        }
        return parent;
    }

    public String toString() {
        return getClass().getSimpleName() + functions.keySet();
    }


    public void setFunction(Symbol symbol, ExecutableDOS function) {
        functions.put(symbol, function);
    }

    public ExecutableDOS getFunction(Symbol symbol) {
        final ExecutableDOS function = functions.get(symbol);
        if(function == null) {
            if(parent == null) {
                // return StandardObjects.NULL;
                throw new RuntimeException("message not understood " + symbol);
            }
            return parent.getFunction(symbol);
        }
        return function;
    }
    
    private static ExecutableDOS SET_PARENT_EXEC = new ExecutableDOS() {
		@Override
		public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
			theObject.setParent(arguments.at(0));
			return theObject;
		}
    };
    
    private static ExecutableDOS SET_FUNCTION_$_TO_$_EXEC = new ExecutableDOS() {
		@Override
		public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
			theObject.setFunction(((SymbolWrapper) arguments.at(0)).getSymbol(), (ExecutableDOS) arguments.at(1));
			return theObject;
		}
    };
	
	/* Runtime Definition of Object */
    
	public static void initialiseRootObject(ObjectDOS rootObject) {
		rootObject.setFunction(Symbol.SET_PARENT_$, SET_PARENT_EXEC);
		rootObject.setFunction(Symbol.SET_FUNCTION_$_TO_$, SET_FUNCTION_$_TO_$_EXEC);
	}
}
