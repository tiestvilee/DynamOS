/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import org.dynamos.OpCodeInterpreter;
import org.dynamos.types.ValueObject;

/**
 *
 * @author tiestvilee
 */
public class OpCode {


	public boolean execute(@SuppressWarnings("unused") OpCodeInterpreter interpreter, @SuppressWarnings("unused") ObjectDOS self, @SuppressWarnings("unused") StackFrame stackFrame) {
        // NOOP
        return false;
    }
   
    public static final OpCode NOOP = new OpCode();
    
    protected static class OpCodeWithSymbol extends OpCode{
    	protected Symbol symbol;

    	@Override
		public int hashCode() {
			return symbol.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			return getClass() == obj.getClass() && symbol.equals(((OpCodeWithSymbol) obj).symbol);
		}
		
		@Override
		public String toString() {
			String name = getClass().getSimpleName();
			return name + ">" + symbol.toString();
		}

    }

    public static class FunctionCall extends OpCodeWithSymbol {
        public FunctionCall(Symbol symbol) {
        	if(symbol == null) {
        		throw new IllegalArgumentException("No symbol supplied");
        	}
            this.symbol = symbol;
        }

        @Override
        public boolean execute(OpCodeInterpreter interpreter, ObjectDOS self, StackFrame stackFrame) {
        	ObjectDOS source, victim;
        	
        	if(stackFrame.getObject() != null) {
        		victim = source = stackFrame.getObject();
        	} else {
            	source = self;
            	victim = self.getSlot(Symbol.THIS);
        	}
			System.out.println("find " + symbol + " on " + source + "\n....with " + stackFrame.getArguments() + "\n....victim = " + victim);
			ExecutableDOS function = source.getFunction(symbol);

        	ObjectDOS result = function.execute(interpreter, victim, stackFrame.getArguments());

        	self.setSlot(Symbol.RESULT, result);
            return true;
        }
        
    }

    public static class SetObject extends OpCodeWithSymbol {
        public SetObject(Symbol symbol) {
            this.symbol = symbol;
        }

        @Override
        public boolean execute(OpCodeInterpreter interpreter, ObjectDOS self, StackFrame stackFrame) {
        	System.out.println(this);

            ObjectDOS object = self.getSlot(symbol);
    		System.out.println("setting object");
            stackFrame.setObject(object);
            return false;
        }

    }
    
    public static class Push extends OpCodeWithSymbol {
        public Push(Symbol symbol) {
            this.symbol = symbol;
        }

        @Override
        public boolean execute(OpCodeInterpreter interpreter, ObjectDOS self, StackFrame stackFrame) {
        	System.out.println(this);

            ObjectDOS argument = self.getSlot(symbol);
            stackFrame.pushArgument(argument);
            return false;
        }

    }

	public static class PushSymbol extends OpCodeWithSymbol {
        public PushSymbol(Symbol symbol) {
            this.symbol = symbol;
        }

        @Override
        public boolean execute(OpCodeInterpreter interpreter, ObjectDOS self, StackFrame stackFrame) {
        	System.out.println(this);

            stackFrame.pushArgument(new SymbolWrapper(symbol));
            return false;
        }
	}


    public static class CreateValueObject extends OpCode {
        private final int value;

		public CreateValueObject(int value) {
			this.value = value;
		}

        @Override
        public boolean execute(OpCodeInterpreter interpreter, ObjectDOS self, StackFrame stackFrame) {
        	System.out.println(this);

        	self.setSlot(Symbol.RESULT, new ValueObject(value));
            return false;
        }
        
    	@Override
		public int hashCode() {
			return value;
		}

		@Override
		public boolean equals(Object obj) {
			return value == ((CreateValueObject) obj).value;
		}
	}

	public static class StartOpCodeList extends OpCode {
		/* do nothing */
        
    	@Override
		public int hashCode() {
			return 1;
		}

		@Override
		public boolean equals(Object obj) {
			return obj instanceof StartOpCodeList;
		}
	}
    
	public static class EndOpCodeList extends OpCode {
		/* do nothing */
        
    	@Override
		public int hashCode() {
			return 2;
		}

		@Override
		public boolean equals(Object obj) {
			return obj instanceof EndOpCodeList;
		}
	}
	
	public static class Debug extends OpCode {
		private Symbol symbol;
		private final String message;
		public Debug(String message, Symbol symbol) {
			this.message = message;
			this.symbol = symbol;
		}
		
		@Override
		public boolean execute(OpCodeInterpreter interpreter, ObjectDOS self, StackFrame stackFrame) {
			System.out.println(this);
			
			if(symbol == Symbol.PARENT) {
				System.out.println(message + " parent of " + self + " is " + self.getTrait("parent"));
			} else {
				ObjectDOS argument = self.getSlot(symbol);
				if(argument instanceof ValueObject) {
					System.out.println(message + " " + ((ValueObject) argument).getValue() + "@" + argument + " parent " + argument.getTrait("parent"));
				} else  {
					System.out.println(message + " " + argument + " parent " + argument.getParent());
				}
			}
			return false;
		}
		
		@Override
		public String toString() {
			return getClass().getSimpleName() + ":" + symbol.toString();
		}
	}
	

	public static void printOpCodes(OpCode[] opCodes) {
		String indent = "";
		for(OpCode opCode : opCodes) {
			if(opCode instanceof OpCode.EndOpCodeList) {
				indent = indent.substring(2);
			}
			System.out.println(indent + opCode);
			if(opCode instanceof OpCode.FunctionCall) {
				System.out.println();
			}
			if(opCode instanceof OpCode.StartOpCodeList) {
				indent += "  ";
			}
		}
	}


}
