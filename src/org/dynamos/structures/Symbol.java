/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author tiestvilee
 */
public class Symbol {
    private static final Map<String, Symbol> symbols = new HashMap<String, Symbol>();

    public static Symbol get(String name) {
        if(!symbols.containsKey(name)) {
            symbols.put(name, new Symbol(name));
        }
        return symbols.get(name);
    }

    String symbol;

    private Symbol(String symbol) {
        this.symbol = symbol;
    }

    public String toString() {
        return "Symbol:" + symbol;
    }
    
    public String value() {
    	return symbol;
    }

    public static final Symbol ARGUMENTS = Symbol.get("arguments");
    public static final Symbol RESULT = Symbol.get("result");
    public static final Symbol THIS = Symbol.get("this");
	public static final Symbol CURRENT_CONTEXT = Symbol.get("currentContext");
	public static final Symbol ADD_SLOT_$ = Symbol.get("addSlot:");
	public static final Symbol ADD_SLOT_$_WITH_VALUE_$ = Symbol.get("addSlot:withValue:");
	public static final Symbol PARENT = Symbol.get("parent");
	public static final Symbol PARENT_$ = Symbol.get("parent:");
	public static final Symbol EXECUTE = Symbol.get("execute");
	public static final Symbol SET_SLOT_$_TO_$ = Symbol.get("setSlot:to:");
	public static final Symbol GET_SLOT_$ = Symbol.get("getSlot:");
	public static final Symbol SET_LOCAL_SLOT_$_TO_$ = Symbol.get("setLocalSlot:to:");
	public static final Symbol SET_TRAIT_$_TO_$ = Symbol.get("setTrait:to:");
	public static final Symbol GET_TRAIT_$ = Symbol.get("getTrait:");
	public static final Symbol EMPTY_LIST = Symbol.get("emptyList");

	public static final Symbol META_VM = Symbol.get("metaVM");
	
	public static final Symbol UNDEFINED = Symbol.get("undefined");
}