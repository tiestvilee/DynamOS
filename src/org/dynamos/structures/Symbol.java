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
	public static final Symbol SET_RESULT = Symbol.get("result:");
	public static final Symbol CURRENT_CONTEXT = Symbol.get("currentContext");
	public static final Symbol CONTEXTUALIZE_FUNCTION = Symbol.get("contextualizeFunction:in:");

	public boolean isPotentialGetter() {
		return !symbol.contains(":");
	}

	public boolean isPotentialSetter() {
		return symbol.indexOf(":") == symbol.lastIndexOf(":");
	}

	public Symbol toSetterSymbol() {
		return Symbol.get(symbol + ":");
	}

	public Symbol fromSetterSymbol() {
		return Symbol.get(symbol.substring(0, symbol.length() - 1));
	}
}
