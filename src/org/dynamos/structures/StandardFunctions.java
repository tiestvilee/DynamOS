package org.dynamos.structures;


public class StandardFunctions {

	public static class Getter extends ExecutableDOS {
		private Symbol symbol;
		
		public Getter(Symbol symbol) {
			this.symbol = symbol;
		}
		
		@Override
		public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
			return (ObjectDOS) theObject.getSlot(symbol);
		}

		public Symbol forSlot() {
			return symbol;
		}
	}

	public static class Setter extends ExecutableDOS {
		private Symbol symbol;
		
		public Setter(Symbol symbol) {
			this.symbol = Symbol.get(symbol.toString().substring(0, symbol.toString().length() - 1));
		}
		
		@Override
		public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
			theObject.setSlot(symbol, arguments.at(0));
			return (ObjectDOS) arguments.at(0);
		}

		public Symbol forSlot() {
			return symbol;
		}
		
		public String toString() {
			return "setter:" + symbol;
		}

	}
	
}
