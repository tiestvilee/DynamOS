/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import org.dynamos.Environment;
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
	public static class UndefinedDOS extends ObjectDOS {}

	public static class TrueDOS extends ObjectDOS {}
	public static class FalseDOS extends ObjectDOS {}

    public static ObjectDOS initialiseBooleans(final OpCodeInterpreter interpreter, Environment environment) {
        Context booleanContext = interpreter.newContext();

        Symbol trueResult = Symbol.get("trueResult");
		Symbol falseResult = Symbol.get("falseResult");
		
		TrueDOS trueObject = new TrueDOS();
        trueObject.setFunction(Symbol.get("ifTrue:ifFalse:"), new FunctionDOS(
                    new FunctionDefinitionDOS(interpreter, new Symbol[] {trueResult, falseResult}, new Symbol[] {}, new OpCode[] {
                    		new OpCode.Push(trueResult),
                    		new OpCode.ContextCall(Symbol.SET_RESULT)
                    }),
                    booleanContext));

        FalseDOS falseObject = new FalseDOS();
        falseObject.setFunction(Symbol.get("ifTrue:ifFalse:"), new FunctionDOS(
                    new FunctionDefinitionDOS(interpreter, new Symbol[] {trueResult, falseResult}, new Symbol[] {}, new OpCode[] {
                    		new OpCode.Push(falseResult),
                    		new OpCode.ContextCall(Symbol.SET_RESULT)
                    }),
                    booleanContext));
        
        ObjectDOS booleanContainer = environment.createNewObject();
        booleanContainer.setSlot(Symbol.get("true"), trueObject);
        booleanContainer.setSlot(Symbol.get("false"), falseObject);
        return booleanContainer;
    }

	public static ObjectDOS createNumberLibrary(final OpCodeInterpreter interpreter, Environment environment) {
        
        // the context in which the number library exists
        Context numberLibraryContext = interpreter.newContext();
        
        // number library depends upon the VM, but that's all
        numberLibraryContext.setSlot(VMObjectDOS.VM, environment.getVirtualMachine());
        
        Symbol numberPrototypeSymbol = Symbol.get("numberPrototype");
        
        createNumberPrototype(interpreter, environment, numberLibraryContext, numberPrototypeSymbol);
        
        // createNumberFactory
        Symbol numberFactorySymbol = Symbol.get("numberFactory");
        Symbol number = Symbol.get("number");
        
        ObjectDOS numberFactory = environment.createNewObject();
        numberLibraryContext.setSlot(numberFactorySymbol, numberFactory);
        
        // add appropriate function to the prototype
        numberFactory.setFunction(Symbol.get("numberFrom:"), new FunctionDOS(new FunctionDefinitionDOS(
        		interpreter,
        		new Symbol[] {number},
        		new Symbol[] {},
        		new OpCode[] {
        			new OpCode.Push(numberPrototypeSymbol),
        			new OpCode.SetObject(number),
        			new OpCode.MethodCall(Symbol.SET_PARENT)
        		}), 
        		numberLibraryContext));
        
        return numberFactory;
	}

	private static void createNumberPrototype(final OpCodeInterpreter interpreter, Environment environment, Context numberLibraryContext, Symbol numberPrototypeSymbol) {
		Symbol right = Symbol.get("right");
        
        // define the prototype for all numbers
        ObjectDOS numberPrototype = environment.createNewObject();
        numberLibraryContext.setSlot(numberPrototypeSymbol, numberPrototype);
        
        // add all appropriate functions to the prototype
        numberPrototype.setFunction(Symbol.get("plus:"), new FunctionDOS(new FunctionDefinitionDOS(
        		interpreter,
        		new Symbol[] {right},
        		new Symbol[] {},
        		new OpCode[] {
       				new OpCode.Push(right),
        			new OpCode.Push(Symbol.THIS),
        			new OpCode.SetObject(VMObjectDOS.VM),
        			new OpCode.MethodCall(VMObjectDOS.ADD),
        			new OpCode.Push(numberPrototypeSymbol),
        			new OpCode.SetObject(Symbol.RESULT),
        			new OpCode.MethodCall(Symbol.SET_PARENT)
        		}), 
        		numberLibraryContext));

        numberPrototype.setFunction(Symbol.get("minus:"), new FunctionDOS(new FunctionDefinitionDOS(
        		interpreter,
        		new Symbol[] {right},
        		new Symbol[] {},
        		new OpCode[] {
       				new OpCode.Push(right),
        			new OpCode.Push(Symbol.THIS),
        			new OpCode.SetObject(VMObjectDOS.VM),
        			new OpCode.MethodCall(VMObjectDOS.SUB),
        			new OpCode.Push(numberPrototypeSymbol),
        			new OpCode.SetObject(Symbol.RESULT),
        			new OpCode.MethodCall(Symbol.SET_PARENT)
        		}), 
        		numberLibraryContext));
        
        numberPrototype.setFunction(Symbol.get("isLessThan:"), new FunctionDOS(new FunctionDefinitionDOS(
        		interpreter,
        		new Symbol[] {right},
        		new Symbol[] {},
        		new OpCode[] {
        			new OpCode.Push(Symbol.THIS),
        			new OpCode.Push(right),
        			new OpCode.SetObject(VMObjectDOS.VM),
        			new OpCode.MethodCall(VMObjectDOS.IS_LESS_THAN)
        		}), 
        		numberLibraryContext));
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

    public static ObjectDOS numberDOS(Environment env, int number) {
        ObjectDOS result = env.createNewValueObject(number);
        ExecutableDOS numberFromFunction = env.getNumberFactory().getFunction(Symbol.get("numberFrom:"));
		ListDOS arguments = new ListDOS();
		arguments.add(result);
		numberFromFunction.execute(result, arguments);
        return result;
    }
}
