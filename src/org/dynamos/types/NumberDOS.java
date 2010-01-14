/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.types;

import org.dynamos.Environment;
import org.dynamos.OpCodeInterpreter;
import org.dynamos.structures.Activation;
import org.dynamos.structures.ExecutableDOS;
import org.dynamos.structures.ListDOS;
import org.dynamos.structures.Mirror;
import org.dynamos.structures.ObjectDOS;
import org.dynamos.structures.OpCode;
import org.dynamos.structures.Symbol;
import org.dynamos.structures.VMObjectDOS;

/**
 * 
 * @author tiestvilee
 */
public class NumberDOS {

	public static ObjectDOS createNumberLibrary(Environment environment) {

		ObjectDOS numberPrototype = createNumberPrototype(environment);
		ObjectDOS numberFactory = createNumberFactory(environment, numberPrototype);

		return numberFactory;
	}

	private static ObjectDOS createNumberFactory(Environment environment, ObjectDOS numberPrototype) {
		// createNumberFactory
		Symbol numberPrototypeSymbol = Symbol.get("numberPrototype");
		Symbol number = Symbol.get("number");
		Symbol mirror = Symbol.get("mirror");

		ObjectDOS numberFactory = environment.createNewObject();
		
		numberFactory.setSlot(numberPrototypeSymbol, numberPrototype);
		
		Activation mirrorContext = environment.getContextBuilder().createActivation();
		mirrorContext.setSlot(mirror, environment.getMirror());
		numberFactory.setSlot(numberPrototypeSymbol, numberPrototype);

		// add appropriate function to the prototype
		numberFactory.setFunction(Symbol.get("numberFrom:"), environment.createFunctionWithContext(
				new Symbol[] { number },
				new OpCode[] {
					new OpCode.Push(numberPrototypeSymbol),
					new OpCode.Push(number),
					new OpCode.SetObject(mirror),
					new OpCode.FunctionCall(Mirror.SET_PARENT_TO_$_ON_$)
				}, mirrorContext));
		
		// the prototype needs the numberfactory so it can convert values into numbers.
        numberPrototype.setParent(numberFactory);
        
		return numberFactory;
	}


	private static ObjectDOS createNumberPrototype(Environment environment) {
		Symbol right = Symbol.get("right");
		

		// number library depends upon the VM, but that's all
		Activation numberLibraryContext = environment.getContextBuilder().createActivation();
		numberLibraryContext.setSlot(VMObjectDOS.VM, environment.getVirtualMachine());
        
        // define the prototype for all numbers
        ObjectDOS numberPrototype = environment.createNewObject();
        
        // add all appropriate functions to the prototype
        numberPrototype.setFunction(Symbol.get("plus:"), environment.createFunctionWithContext(
        		new Symbol[] {right},
        		new OpCode[] {
       				new OpCode.Push(right),
        			new OpCode.Push(Symbol.THIS),
        			new OpCode.SetObject(VMObjectDOS.VM),
        			new OpCode.FunctionCall(VMObjectDOS.ADD_$_TO_$),
        			new OpCode.Push(Symbol.RESULT),
        			new OpCode.FunctionCall(Symbol.get("numberFrom:"))
        		}, 
        		numberLibraryContext));

        numberPrototype.setFunction(Symbol.get("minus:"), environment.createFunctionWithContext(
        		new Symbol[] {right},
        		new OpCode[] {
       				new OpCode.Push(right),
        			new OpCode.Push(Symbol.THIS),
        			new OpCode.SetObject(VMObjectDOS.VM),
        			new OpCode.FunctionCall(VMObjectDOS.SUBTRACT_$_FROM_$),
        			new OpCode.Push(Symbol.RESULT),
        			new OpCode.FunctionCall(Symbol.get("numberFrom:"))
        		},
        		numberLibraryContext));
        
        numberPrototype.setFunction(Symbol.get("isLessThan:"), environment.createFunctionWithContext(
        		new Symbol[] {right},
        		new OpCode[] {
        			new OpCode.Push(Symbol.THIS),
        			new OpCode.Push(right),
        			new OpCode.SetObject(VMObjectDOS.VM),
        			new OpCode.FunctionCall(VMObjectDOS.VALUE_$_IS_LESS_THAN_$)
        		}, 
        		numberLibraryContext));
        
        return numberPrototype;
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

	public static ObjectDOS numberDOS(OpCodeInterpreter interpreter, int number) {
		ObjectDOS result = interpreter.getEnvironment().createNewValueObject(number);
		ExecutableDOS numberFromFunction = interpreter.getEnvironment().getNumberFactory().getFunction(
				Symbol.get("numberFrom:"));
		ListDOS arguments = new ListDOS();
		arguments.add(result);
		numberFromFunction.execute(interpreter, interpreter.getEnvironment().getNumberFactory(), arguments);
		return result;
	}
}
