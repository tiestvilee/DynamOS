/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import org.dynamos.structures.FunctionDOS.ContextualFunctionDOS;
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
		FunctionDOS.ContextualFunctionDOS function = (ContextualFunctionDOS) object.getFunction(symbol);
	    
	    return function.execute(object, stackFrame.getArguments());
	}

	public static final OpCode NOOP = new OpCode();

    public static class MethodCall extends OpCode {
        private Symbol symbol;
        public MethodCall(Symbol symbol) {
            this.symbol = symbol;
        }

        @Override
        public boolean execute(Context context, StackFrame stackFrame) {
            ObjectDOS object = stackFrame.getObject();
            ObjectDOS result = findFunctionInObjectAndExecute(object, symbol, stackFrame);

            context.setSlot(Symbol.RESULT, result);
            return true;
        }
    }

    public static class ContextCall extends OpCode {
        private Symbol symbol;
        public ContextCall(Symbol symbol) {
            this.symbol = symbol;
        }

        @Override
        public boolean execute(Context context, StackFrame stackFrame) {
        	ObjectDOS result = findFunctionInObjectAndExecute(context, symbol, stackFrame);

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
