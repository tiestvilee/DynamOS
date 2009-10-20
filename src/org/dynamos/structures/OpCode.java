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

    public boolean execute(Context context) {
        // NOOP
        return false;
    }
   
    public static final OpCode NOOP = new OpCode();

    public static class MethodCall extends OpCode {
        private Symbol symbol;
        public MethodCall(Symbol symbol) {
            this.symbol = symbol;
        }

        public boolean execute(Context context) {
            ObjectDOS object = context.getObject();
            System.out.println("trying to find function " + symbol + " on " + object);
            FunctionDOS.ContextualFunctionDOS function = (ContextualFunctionDOS) object.getSlot(symbol);
            function.execute(object, (List<Object>) context.getSlot(Symbol.ARGUMENTS));
            return true;
        }
    }

    public static class ContextCall extends OpCode {
        private Symbol symbol;
        public ContextCall(Symbol symbol) {
            this.symbol = symbol;
        }

        public boolean execute(Context context) {
            FunctionDOS.ContextualFunctionDOS function = (ContextualFunctionDOS) context.getParent().getSlot(symbol);
            function.execute((List<Object>) context.getSlot(Symbol.ARGUMENTS));
            return true;
        }
    }

    public static class SetObject extends OpCode {
        private Symbol symbol;
        public SetObject(Symbol symbol) {
            this.symbol = symbol;
        }

        public boolean execute(Context context) {
            ObjectDOS object = (ObjectDOS) context.getParent().getSlot(symbol);
            context.setObject(object);
            return false;
        }
    }

    public static class SetObjectToThis extends OpCode {
        public SetObjectToThis() {
        }

        public boolean execute(Context context) {
            ObjectDOS object = (ObjectDOS) ((Context) context.getParent()).getObject();
            context.setObject(object);
            return false;
        }
    }

    public static class Push extends OpCode {
        private Symbol symbol;
        public Push(Symbol symbol) {
            this.symbol = symbol;
        }

        public boolean execute(Context context) {
            ObjectDOS argument = (ObjectDOS) context.getParent().getSlot(symbol);
            context.pushArgument(argument);
            return false;
        }
    }

}
