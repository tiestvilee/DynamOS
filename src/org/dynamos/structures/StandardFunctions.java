package org.dynamos.structures;

import java.util.List;

import org.dynamos.OpCodeInterpreter;


public class StandardFunctions {

	public static class Getter extends ExecutableDOS {
		private Symbol symbol;
		
		public Getter(Symbol symbol) {
			this.symbol = symbol;
		}
		
		@Override
		public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, List<ObjectDOS> arguments) {
			return theObject.getSlot(symbol);
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
		public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, List<ObjectDOS> arguments) {
			theObject.setSlot(symbol, arguments.get(0));
			return arguments.get(0);
		}

		public Symbol forSlot() {
			return symbol;
		}
		
		public String toString() {
			return "setter:" + symbol;
		}

	}
	
}
