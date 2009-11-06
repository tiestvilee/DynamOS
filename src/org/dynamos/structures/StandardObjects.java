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
	
	public static ObjectDOS NUMBER_PROTOTYPE;

    public static void initialiseStandardObjects(final OpCodeInterpreter interpreter, ObjectDOS virtualMachine) {
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
        
        Context contextContainingVM = new Context();
        contextContainingVM.setSlot(VMObjectDOS.VM, virtualMachine);
        
        Symbol right = Symbol.get("right");
        
        NUMBER_PROTOTYPE = new ObjectDOS();
        
        NUMBER_PROTOTYPE.setFunction(Symbol.get("plus:"), new FunctionDOS(new FunctionDefinitionDOS(
        		interpreter,
        		new Symbol[] {right},
        		new Symbol[] {},
        		new OpCode[] {
       				new OpCode.Push(right),
        			new OpCode.Push(Symbol.THIS),
        			new OpCode.SetObject(VMObjectDOS.VM),
        			new OpCode.MethodCall(VMObjectDOS.ADD)
        		}), 
        		contextContainingVM));

        NUMBER_PROTOTYPE.setFunction(Symbol.get("minus:"), new FunctionDOS(new FunctionDefinitionDOS(
        		interpreter,
        		new Symbol[] {right},
        		new Symbol[] {},
        		new OpCode[] {
       				new OpCode.Push(right),
        			new OpCode.Push(Symbol.THIS),
        			new OpCode.SetObject(VMObjectDOS.VM),
        			new OpCode.MethodCall(VMObjectDOS.SUB)
        		}), 
        		contextContainingVM));
        
        NUMBER_PROTOTYPE.setFunction(Symbol.get("isLessThan:"), new FunctionDOS(new FunctionDefinitionDOS(
        		interpreter,
        		new Symbol[] {right},
        		new Symbol[] {},
        		new OpCode[] {
        			new OpCode.Push(Symbol.THIS),
        			new OpCode.Push(right),
        			new OpCode.SetObject(VMObjectDOS.VM),
        			new OpCode.MethodCall(VMObjectDOS.IS_LESS_THAN)
        		}), 
        		contextContainingVM));
    }

    public static class ValueObject extends ObjectDOS {
        private int value;

        public ValueObject(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }

    public static ValueObject makeValueANumber(final ValueObject object) {
        object.setParent(NUMBER_PROTOTYPE);

        return object;
    }

    public static ValueObject numberDOS(int number) {
        ValueObject result = new ValueObject(number);
        makeValueANumber(result);
        return result;
    }
}
