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
	
    private HashMap<Symbol, ObjectDOS> slots;
    private HashMap<Symbol, ExecutableDOS> functions;
    private ObjectDOS parent;
    private ObjectDOS context;
	private ObjectDOS undefined;
	
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
                return undefined;
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
        return getContextFunction(false, symbol);
    }
    
    private ExecutableDOS getContextFunction(boolean foundEnclosingObject, Symbol symbol) {
    	
        ExecutableDOS function = functions.get(symbol);
        if(function == null) {
        	boolean isEnclosingObject = !(foundEnclosingObject || this instanceof Context);
        	
        	if(context != null) {
        		function = context.getContextFunction(isEnclosingObject, symbol);
        	}
        	if(function == null && isEnclosingObject && parent != null) {
	            function = parent.getParentFunction(symbol);
        	}
        }
        return function;
    }
    
    private ExecutableDOS getParentFunction(Symbol symbol) {
        ExecutableDOS function = functions.get(symbol);
        if(function == null && parent != null) {
        	function = parent.getParentFunction(symbol);
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
    
	public static void initialiseRootObject(Environment environment, ObjectDOS rootObject) {
		rootObject.undefined = environment.getUndefined();
		rootObject.setFunction(Symbol.SET_PARENT_$, SET_PARENT_EXEC);
		rootObject.setFunction(Symbol.SET_FUNCTION_$_TO_$, SET_FUNCTION_$_TO_$_EXEC);
	}
}
