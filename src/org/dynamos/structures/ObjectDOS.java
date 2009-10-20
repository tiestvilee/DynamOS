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
    HashMap<Symbol, Object> slots;
    ObjectDOS parent;

    public ObjectDOS() {
        slots = new HashMap<Symbol, Object>();
    }

    public void setSlot(Symbol symbol, Object value) {
        slots.put(symbol, value);
    }

    public Object getSlot(Symbol symbol) {
        final Object slot = slots.get(symbol);
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

    public FunctionDOS getFunction(Symbol symbol) {
        Object slot = getSlot(symbol);
        if(! (slot instanceof FunctionDOS)) System.out.println("no function for '" + symbol + "' got " + slot );
        return (FunctionDOS) slot;
    }

    public String toString() {
        return super.toString() + "[" + slots +
                "]";
    }

    public static final ObjectDOS OBJECT = new ObjectDOS();
}
