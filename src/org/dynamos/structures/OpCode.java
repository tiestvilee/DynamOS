/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

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
            FunctionDOS function = object.getFunction(symbol);
            function.execute(context);
            return true;
        }
    }

    public static class ContextCall extends OpCode {
        private Symbol symbol;
        public ContextCall(Symbol symbol) {
            this.symbol = symbol;
        }

        public boolean execute(Context context) {
            FunctionDOS function = context.getParent().getFunction(symbol);
            function.execute(context);
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
