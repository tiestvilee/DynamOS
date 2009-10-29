package org.dynamos.structures;

import java.util.List;

public class StandardFunctions {

	public static class Getter extends FunctionDOS.ContextualFunctionDOS {
		private Symbol symbol;
		
		public Getter(Symbol symbol) {
			this.symbol = symbol;
		}
		
		@Override
		public ObjectDOS execute(ObjectDOS theObject, List<Object> arguments) {
			return (ObjectDOS) theObject.getSlot(symbol);
		}

		public Symbol forSlot() {
			return symbol;
		}
	}

	public static class Setter extends FunctionDOS.ContextualFunctionDOS {
		private Symbol symbol;
		
		public Setter(Symbol symbol) {
			this.symbol = symbol.fromSetterSymbol();
		}
		
		@Override
		public ObjectDOS execute(ObjectDOS theObject, List<Object> arguments) {
			theObject.setSlot(symbol, arguments.get(0));
			return (ObjectDOS) arguments.get(0);
		}

		public Symbol forSlot() {
			return symbol;
		}
	}
}
