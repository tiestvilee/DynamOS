/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import java.util.HashMap;

import org.dynamos.Environment;

/**
 *
 * @author tiestvilee
 */
public class ObjectDOS {
	
	/* Compile time definition of Object */
	private static ObjectDOS UNDEFINED;
	protected static FunctionLookupStrategy FUNCTION_LOOKUP;
	
    private HashMap<Symbol, ObjectDOS> slots;
    private HashMap<Symbol, ExecutableDOS> functions;
    private ObjectDOS parent;
    private ObjectDOS context;
	
    public ObjectDOS() {
		slots = new HashMap<Symbol, ObjectDOS>();
        functions = new HashMap<Symbol, ExecutableDOS>();
    }

    public void setSlot(Symbol symbol, ObjectDOS value) {
        slots.put(symbol, value);
    }
    
    public ObjectDOS getSlot(Symbol symbol) {
        final ObjectDOS slot = slots.get(symbol);
        if(slot == null) {
            if(context == null) {
                return UNDEFINED;
            }
            return context.getSlot(symbol);
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

	public void setContext(ObjectDOS context) {
		this.context = context;
	}

	public ObjectDOS getContext() {
		return context;
	}

    public String toString() {
        return getClass().getSimpleName() + functions.keySet();
    }


    public void setFunction(Symbol symbol, ExecutableDOS function) {
        functions.put(symbol, function);
    }

    public ExecutableDOS getFunction(Symbol symbol) {
        return FUNCTION_LOOKUP.lookupFunction(this, symbol);
    }

    public HashMap<Symbol, ExecutableDOS> getFunctions() {
        return functions;
    }
    
    public ObjectDOS getLocalSlot(Symbol symbol) {
    	return slots.get(symbol);
    }

    /*
     * object appropriate functions
     */
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
    
    private static ExecutableDOS SET_SLOT_$_TO_$_EXEC = new ExecutableDOS() {
    	@Override
    	public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
    		Symbol symbol = ((SymbolWrapper) arguments.at(0)).getSymbol();
    		ObjectDOS value = arguments.at(1);
    		
        	ObjectDOS cursor = theObject;
        	while(cursor != null && cursor.getLocalSlot(symbol) == null)
       		{
        		cursor = cursor.getContext();
       		}
        	if(cursor == null) {
        		cursor = theObject;
        	}
        	cursor.setSlot(symbol, value);
        	System.out.println("+++ setting slot " + symbol + " on " + cursor + " to " + value);
    		return cursor;
    	}
    };
    
    private static ExecutableDOS GET_SLOT_$_EXEC = new ExecutableDOS() {
		@Override
		public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
    		Symbol symbol = ((SymbolWrapper) arguments.at(0)).getSymbol();
			return theObject.getSlot(symbol);
		}
    };
    

    /* Runtime Definition of Object */
    
	public static void initialiseRootObject(Environment environment, ObjectDOS rootObject) {
		UNDEFINED = environment.getUndefined();
		FUNCTION_LOOKUP = new NewspeakLookupStrategy();
		
		rootObject.setFunction(Symbol.SET_PARENT_$, SET_PARENT_EXEC);
		rootObject.setFunction(Symbol.SET_FUNCTION_$_TO_$, SET_FUNCTION_$_TO_$_EXEC);
		rootObject.setFunction(Symbol.SET_SLOT_$_TO_$, SET_SLOT_$_TO_$_EXEC);
		rootObject.setFunction(Symbol.GET_SLOT_$, GET_SLOT_$_EXEC);
	}

}
