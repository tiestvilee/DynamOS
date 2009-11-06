/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import org.dynamos.OpCodeInterpreter;

/**
 *
 * @author tiestvilee
 */
public class StandardObjects {
    // TODO how to get at args - by index, by name
    // TODO how to contextualise functions - how are non-contextual functions scoped
    // TODO how to set context values
    // TODO if we update Number, it must be our own version of Number, but then PICs are unique to every sandbox!

	public static class NullDOS extends ObjectDOS {}
    public static final ObjectDOS NULL = new NullDOS();


	public static class TrueDOS extends ObjectDOS {}
    public static final TrueDOS TRUE = new TrueDOS();

	public static class FalseDOS extends ObjectDOS {}
    public static final FalseDOS FALSE = new FalseDOS();

    public static class UndefinedDOS extends ObjectDOS {}
	public static final UndefinedDOS UNDEFINED = new UndefinedDOS();

    public static void initialiseStandardObjects(OpCodeInterpreter interpreter) {
        Context context = interpreter.newContext();

        Symbol trueResult = Symbol.get("trueResult");
		Symbol falseResult = Symbol.get("falseResult");
		
		/* TODO this should definitely be opcodes... */
        TRUE.setFunction(Symbol.get("ifTrue:ifFalse:"), new FunctionDOS(
                    new FunctionDefinitionDOS(interpreter, new Symbol[] {trueResult, falseResult}, new Symbol[] {}, new OpCode[] {
                    		new OpCode.Push(trueResult),
                    		new OpCode.ContextCall(Symbol.SET_RESULT)
                    }),
                    context));

        FALSE.setFunction(Symbol.get("ifTrue:ifFalse:"), new FunctionDOS(
                    new FunctionDefinitionDOS(interpreter, new Symbol[] {trueResult, falseResult}, new Symbol[] {}, new OpCode[] {
                    		new OpCode.Push(falseResult),
                    		new OpCode.ContextCall(Symbol.SET_RESULT)
                    }),
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
        Context context = interpreter.newContext();
        // TODO change these functions to be oopcodes that call VM...
        object.setFunction(Symbol.get("plus:"),
                new FunctionDOS(
                    new FunctionDefinitionDOS(interpreter, null, new Symbol[] {}, null) {

                        @Override
                        public void execute(Context context) {
                            int left = ((ValueObject) context.getObject()).getValue();
                            int right = ((ValueObject) context.getArguments().at(0)).getValue();
                            ObjectDOS result = makeValueANumber(interpreter, new ValueObject(left + right));
                            context.setSlot(Symbol.RESULT, result);
                        }

                    },
                    context));
        object.setFunction(Symbol.get("minus:"),
                new FunctionDOS(
                    new FunctionDefinitionDOS(interpreter, null, new Symbol[] {}, null) {

                        @Override
                        public void execute(Context context) {
                            int left = ((ValueObject) context.getObject()).getValue();
                            int right = ((ValueObject) context.getArguments().at(0)).getValue();
                            ObjectDOS result = makeValueANumber(interpreter, new ValueObject(left - right));
                            context.setSlot(Symbol.RESULT, result);
                        }

                    },
                    context));
        object.setFunction(Symbol.get("isLessThan:"),
                new FunctionDOS(
                    new FunctionDefinitionDOS(interpreter, null, new Symbol[] {}, null) {

                        @Override
                        public void execute(Context context) {
                            int left = ((ValueObject) context.getObject()).getValue();
                            int right = ((ValueObject) context.getArguments().at(0)).getValue();
                            ObjectDOS result = left < right ? TRUE : FALSE;
                            System.out.println("is less than returns " + (result == TRUE));
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
