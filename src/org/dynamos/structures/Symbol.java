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
}
