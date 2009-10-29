/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import java.util.ArrayList;

import org.dynamos.OpCodeInterpreter;

/**
 *
 * @author tiestvilee
 */
public class StandardObjects {

    public static final ObjectDOS NULL = new ObjectDOS();


    public static final ObjectDOS TRUE = new ObjectDOS();

    public static final ObjectDOS FALSE = new ObjectDOS();

    public static void initialiseStandardObjects(OpCodeInterpreter interpreter) {
        Context context = new Context();

        /* TODO this should definitely be opcodes... */
        TRUE.setSlot(Symbol.get("ifTrue:ifFalse:"), new FunctionDOS.ContextualFunctionDOS(
                    new FunctionDOS(interpreter, new OpCode[] {
                        //OpCode.Return() // TODO how to get at args - by index, by name
                                // TODO how to contextualise functions - how are non-contextual functions scoped
                                // TODO how to set context values
                                // TODO if we update Number, it must be our own version of Number, but then PICs are unique to every sandbox!
                    }),
                    context));

        FALSE.setSlot(Symbol.get("ifTrue:ifFalse:"), new FunctionDOS.ContextualFunctionDOS(
                    new FunctionDOS(interpreter, null) {

                        @Override
                        public void execute(Context context) {
                            ObjectDOS falseBranch = (ObjectDOS) context.getArguments().get(1);
                            ObjectDOS result;
                            if(falseBranch instanceof FunctionDOS.ContextualFunctionDOS) {
                                result = ((FunctionDOS.ContextualFunctionDOS) falseBranch).execute(context, new ArrayList<ObjectDOS>());
                            } else {
                                // TODO what if executing a normal object simply returned that object... cool
                                result = falseBranch;
                            }
                            context.setSlot(Symbol.RESULT, result);
                        }

                    },
                    context));
    }

    public static class ValueObject extends ObjectDOS {
        private int value;

        public ValueObject(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        // should this exist?  I think this should probably be immutable...
        public void setValue(int value) {
            this.value = value;
        }


    }

    public static ValueObject makeValueANumber(final OpCodeInterpreter interpreter, final ValueObject object) {
        Context context = new Context();
        // TODO change these functions to be oopcodes that call VM...
        object.setSlot(Symbol.get("plus:"),
                new FunctionDOS.ContextualFunctionDOS(
                    new FunctionDOS(interpreter, null) {

                        @Override
                        public void execute(Context context) {
                            int left = ((ValueObject) context.getObject()).getValue();
                            int right = ((ValueObject) context.getArguments().get(0)).getValue();
                            ObjectDOS result = makeValueANumber(interpreter, new ValueObject(left + right));
                            context.setSlot(Symbol.RESULT, result);
                        }

                    },
                    context));
        object.setSlot(Symbol.get("minus:"),
                new FunctionDOS.ContextualFunctionDOS(
                    new FunctionDOS(interpreter, null) {

                        @Override
                        public void execute(Context context) {
                            int left = ((ValueObject) context.getObject()).getValue();
                            int right = ((ValueObject) context.getArguments().get(0)).getValue();
                            ObjectDOS result = makeValueANumber(interpreter, new ValueObject(left - right));
                            context.setSlot(Symbol.RESULT, result);
                        }

                    },
                    context));
        object.setSlot(Symbol.get("isLessThan:"),
                new FunctionDOS.ContextualFunctionDOS(
                    new FunctionDOS(interpreter, null) {

                        @Override
                        public void execute(Context context) {
                            int left = ((ValueObject) context.getObject()).getValue();
                            int right = ((ValueObject) context.getArguments().get(0)).getValue();
                            ObjectDOS result = left < right ? TRUE : FALSE;
                            context.setSlot(Symbol.RESULT, result);
                        }

                    },
                    context));
        return object;
    }

    public static ValueObject numberDOS(OpCodeInterpreter interpreter, int number) {
        ValueObject result = new ValueObject(number);
        makeValueANumber(interpreter, result);
        return result;
    }
}
