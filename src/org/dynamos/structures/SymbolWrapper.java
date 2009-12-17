package org.dynamos.structures;

public class SymbolWrapper extends ObjectDOS {

	private final Symbol symbol;

	public SymbolWrapper(Symbol symbol) {
		this.symbol = symbol;
	}
	
	public Symbol getSymbol() {
		return symbol;
	}

	public String toString() {
		return symbol.toString();
	}
}
