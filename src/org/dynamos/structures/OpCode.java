/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import org.dynamos.OpCodeInterpreter;
import org.dynamos.structures.StandardObjects.ValueObject;

/**
 *
 * @author tiestvilee
 */
public class OpCode {


	public boolean execute(Context context, StackFrame stackFrame) {
        // NOOP
        return false;
    }
   
    protected ObjectDOS findFunctionInObjectAndExecute(ObjectDOS object, Symbol symbol, StackFrame stackFrame) {
    	System.out.println("find and execute " + symbol + " on " + object);
    	ExecutableDOS function = (ExecutableDOS) object.getFunction(symbol);
	    
	    return function.execute(object, stackFrame.getArguments());
	}

	public static final OpCode NOOP = new OpCode();

    public static class FunctionCall extends OpCode {
        private Symbol symbol;
        public FunctionCall(Symbol symbol) {
            this.symbol = symbol;
        }

        @Override
        public boolean execute(Context context, StackFrame stackFrame) {
        	ObjectDOS target = context;
        	if(stackFrame.getObject() != null) {
        		target = stackFrame.getObject();
        	}
        	ObjectDOS result = findFunctionInObjectAndExecute(target, symbol, stackFrame);

        	context.setSlot(Symbol.RESULT, result);
            return true;
        }
    }

    public static class SetObject extends OpCode {
        private Symbol symbol;
        public SetObject(Symbol symbol) {
            this.symbol = symbol;
        }

        @Override
        public boolean execute(Context context, StackFrame stackFrame) {
            ObjectDOS object = (ObjectDOS) context.getSlot(symbol);
            stackFrame.setObject(object);
            return false;
        }
    }
    
    public static class Push extends OpCode {
        private Symbol symbol;
        public Push(Symbol symbol) {
            this.symbol = symbol;
        }

        @Override
        public boolean execute(Context context, StackFrame stackFrame) {
            ObjectDOS argument = (ObjectDOS) context.getSlot(symbol);
            stackFrame.pushArgument(argument);
            return false;
        }
    }

	public static class PushSymbol extends OpCode {
        private Symbol symbol;
        public PushSymbol(Symbol symbol) {
            this.symbol = symbol;
        }

        @Override
        public boolean execute(Context context, StackFrame stackFrame) {
            stackFrame.pushArgument(new SymbolWrapper(symbol));
            return false;
        }
	}


    public static class CreateValueObject extends OpCode {
        private final int value;
		private final OpCodeInterpreter interpreter;

		public CreateValueObject(OpCodeInterpreter interpreter, int value) {
			this.interpreter = interpreter; // TODO this sux
			this.value = value;
		}

        @Override
        public boolean execute(Context context, StackFrame stackFrame) {
        	context.setSlot(Symbol.RESULT, interpreter.getEnvironment().createNewValueObject(value));
            return false;
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
        public boolean execute(Context context, StackFrame stackFrame) {
            ObjectDOS argument = (ObjectDOS) context.getSlot(symbol);
            if(argument instanceof ValueObject) {
            	System.out.println(message + " " + ((ValueObject) argument).getValue() + "@" + argument);
            } else  {
            	System.out.println(message + " " + argument);
            }
            return false;
        }
    }

}
