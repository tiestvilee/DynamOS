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
   
    public static final OpCode NOOP = new OpCode();

    public static class MethodCall extends OpCode {
        private Symbol symbol;
        public MethodCall(Symbol symbol) {
            this.symbol = symbol;
        }

        @Override
        public boolean execute(Context context, StackFrame stackFrame) {
            ObjectDOS object = stackFrame.getObject();
            System.out.println("trying to find function " + symbol + " on " + object);
            FunctionDOS.ContextualFunctionDOS function = (ContextualFunctionDOS) object.getSlot(symbol);
            function.execute(object, stackFrame.getArguments());
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
            FunctionDOS.ContextualFunctionDOS function = (ContextualFunctionDOS) context.getSlot(symbol);
            ObjectDOS result = function.execute(stackFrame.getArguments());
            context.setSlot(context.getResultTarget(), result);
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

    public static class SetObjectToThis extends OpCode {
        public SetObjectToThis() {
        }

        @Override
        public boolean execute(Context context, StackFrame stackFrame) {
            ObjectDOS object = (ObjectDOS) context.getObject();
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

    public static class Return extends OpCode {
        private Symbol symbol;
        public Return(Symbol symbol) {
            this.symbol = symbol;
        }

        @Override
        public boolean execute(Context context, StackFrame stackFrame) {
            context.setResult( (ObjectDOS) context.getSlot(symbol));
            return false;
        }
    }

    public static class SetResultTarget extends OpCode {
        private Symbol resultTarget;

        public SetResultTarget(Symbol returnTarget) {
            this.resultTarget = returnTarget;
        }

        @Override
        public boolean execute(Context context, StackFrame stackFrame) {
            context.setResultTarget(resultTarget);
            return false;
        }
    }

}
