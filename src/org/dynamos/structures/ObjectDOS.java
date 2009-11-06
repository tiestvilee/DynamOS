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
	
    HashMap<Symbol, ObjectDOS> slots;
    HashMap<Symbol, FunctionDOS.ContextualFunctionDOS> functions;
    ObjectDOS parent;

    public ObjectDOS() {
        slots = new HashMap<Symbol, ObjectDOS>();
        functions = new HashMap<Symbol, FunctionDOS.ContextualFunctionDOS>();
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
                return StandardObjects.NULL;
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
            return OBJECT;
        }
        return parent;
    }

    public String toString() {
        return getClass().getSimpleName() + functions.keySet();
    }


    public void setFunction(Symbol symbol, FunctionDOS.ContextualFunctionDOS function) {
        functions.put(symbol, function);
    }

    public FunctionDOS.ContextualFunctionDOS getFunction(Symbol symbol) {
        final FunctionDOS.ContextualFunctionDOS function = functions.get(symbol);
        if(function == null) {
            if(parent == null) {
                // return StandardObjects.NULL;
                throw new RuntimeException("message not understood " + symbol);
            }
            return parent.getFunction(symbol);
        }
        return function;
    }
	
	/* Runtime Definition of Object */
	
    public static final ObjectDOS OBJECT = new ObjectDOS();
    {
//    	OBJECT.setFunction(Symbol.ADD_SLOT, new FunctionDOS.ContextualFunctionDOS() {
//    		@Override
//    		public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
//    			theObject.setSlot(arguments.at(0), StandardObjects.NULL);
//    			return theObject;
//    		}
//    	});
    }
}
