/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.dynamos.Environment;

/**
 *
 * @author tiestvilee
 */
public class ObjectDOS {
	
	/* Compile time definition of Object */
	public static ObjectDOS UNDEFINED;
	
    private HashMap<Symbol, ObjectDOS> slots;
    private HashMap<Symbol, ExecutableDOS> functions;
    private LinkedHashMap<String, ObjectDOS> traits;
	
    public ObjectDOS() {
		slots = new HashMap<Symbol, ObjectDOS>();
        functions = new HashMap<Symbol, ExecutableDOS>();
        traits = new LinkedHashMap<String, ObjectDOS>();
    }

    public void setSlot(Symbol symbol, ObjectDOS value) {
        slots.put(symbol, value);
    }
    
    public ObjectDOS getSlot(Symbol symbol) {
        ObjectDOS slot = slots.get(symbol);
        if(slot != null) {
        	return slot;
        }
    	for(ObjectDOS parent : traits.values()) {
    		slot = parent.getSlot(symbol);
    		if(slot != UNDEFINED) {
    			return slot;
    		}
    	}
        return UNDEFINED;
    }

    public void setFunction(Symbol symbol, ExecutableDOS function) {
        functions.put(symbol, function);
    }

    public ExecutableDOS getFunction(Symbol symbol) {        
    	ExecutableDOS function = functions.get(symbol);
	    if(function != null) {
	    	return function;
	    }
		for(ObjectDOS trait : traits.values()) {
			function = trait.getFunction(symbol);
			if(function != null) {
				return function;
			}
		}
	    return null;
    }
    
    public void setTrait(String name, ObjectDOS trait) {
        traits.put(name, trait);
    }

    public ObjectDOS getTrait(String name) {
    	return traits.get(name);
    }

	public void removeTrait(String name) {
		traits.remove(name);
	}

    public void setParent(ObjectDOS parent) {
        setTrait("parent", parent);
    }

    public ObjectDOS getParent() {
    	return getTrait("parent");
    }

    public String toString() {
        return getClass().getSimpleName() + " f" + functions.keySet() + " s" + slots.keySet();
    }

    public HashMap<Symbol, ExecutableDOS> getFunctions() {
        return functions;
    }
    
    public ObjectDOS getLocalSlot(Symbol symbol) {
    	return slots.get(symbol);
    }

    public void setArguments(ListDOS arguments) {
	    setSlot(Symbol.ARGUMENTS, arguments);
	}

    /* Runtime Definition of Object */
    
	public static void initialiseRootObject(Environment environment) {
		UNDEFINED = environment.getUndefined();
	}

}
