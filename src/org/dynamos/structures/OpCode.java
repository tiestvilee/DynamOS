/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import java.util.List;

import org.dynamos.structures.FunctionDOS.ContextualFunctionDOS;

/**
 *
 * @author tiestvilee
 */
public class OpCode {

    public boolean execute(Context context, StackFrame stackFrame) {
        // NOOP
        return false;
    }
   
    protected void findFunctionInObjectAndExecute(ObjectDOS object, Symbol symbol, StackFrame stackFrame) {
		FunctionDOS.ContextualFunctionDOS function = (ContextualFunctionDOS) object.getFunction(symbol);
	    System.out.println(function);
	    
	    ObjectDOS result = function.execute(object, stackFrame.getArguments());
	    
	    object.setSlot(Symbol.RESULT, result);
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
            findFunctionInObjectAndExecute(object, symbol, stackFrame);
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
            findFunctionInObjectAndExecute(context, symbol, stackFrame);
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

}
