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
        return symbol;
    }

    public static final Symbol ARGUMENTS = Symbol.get("arguments");
    public static final Symbol RESULT = Symbol.get("result");
    public static final Symbol THIS = Symbol.get("this");
	public static final Symbol CURRENT_CONTEXT = Symbol.get("currentContext");
	public static final Symbol ADD_SLOT_$ = Symbol.get("addSlot:");
	public static final Symbol ADD_SLOT_$_WITH_VALUE_$ = Symbol.get("addSlot:withValue:");
	public static final Symbol CONTEXTUALIZE_FUNCTION_$_IN_$ = VMObjectDOS.CONTEXTUALIZE_FUNCTION_$_IN_$;
	public static final Symbol SET_PARENT_$ = Symbol.get("parent:");
	public static final Symbol EXECUTE = Symbol.get("execute");
	public static final Symbol SET_FUNCTION_$_TO_$ = Symbol.get("setFunction:to:");
	public static final Symbol NEW_OBJECT = VMObjectDOS.NEW_OBJECT;
	public static final Symbol CREATE_FUNCTION_WITH_ARGUMENTS_$_LOCALS_$_OPCODES_$ = VMObjectDOS.CREATE_FUNCTION_WITH_ARGUMENTS_$_LOCALS_$_OPCODES_$;
	public static final Symbol SET_SLOT_$_TO_$ = Symbol.get("setSlot:to:");
	public static final Symbol GET_SLOT_$ = Symbol.get("getSlot:");
}
