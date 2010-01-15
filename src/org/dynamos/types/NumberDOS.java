/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.types;

import org.dynamos.Environment;
import org.dynamos.OpCodeInterpreter;
import org.dynamos.compiler.BootstrapCompiler;
import org.dynamos.structures.Activation;
import org.dynamos.structures.ConstructorDOS;
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

	public static ObjectDOS createNumberLibrary(OpCodeInterpreter interpreter, Environment environment) {

		String libraryProgram = 
			"(constructor numberFactoryConstructor: vm and: listFactory\n" + 
			"  \n" + 
			"  (constructor numberPrototypeConstructor: vm and: listFactory and: numberFactory\n" + 
			"    (function plus: number\n" + 
			"      $numberFactory numberFrom: ($vm add: ($number value) to: $value)\n" + 
			"    )\n" + 
			"    \n" + 
			"    (function minus: number\n" + 
			"      $numberFactory numberFrom: ($vm subtract: ($number value) from: $value)\n" + 
			"    )\n" + 
			"    \n" + 
			"    (function isLessThan: number\n" + 
			"      $vm value: $value isLessThan: ($number value)\n" + // not compiling
			"    )\n" + 
			"    \n" + // fuck
			"    (function value\n" + 
			"      $value\n" + 
			"    )\n" + 
			"  )\n" +
			"  $numberPrototype: (numberPrototypeConstructor: $vm and: $listFactory and: $this)\n" + 
			"  \n" + 
			"  (constructor numberConstructor: value prototype: numberPrototype\n" +
			"     parent: $numberPrototype\n" + 
			"  )\n" +
			"  \n" + 
			"  (function numberFrom: value\n" + 
			"     numberConstructor: $value prototype: $numberPrototype\n" + 
			"  )\n" + 
			"  \n" +
			") \n ";
		
		ConstructorDOS libraryConstructor = new BootstrapCompiler().compile(libraryProgram);
		
		ListDOS arguments = new ListDOS();
		arguments.add(environment.getVirtualMachine());
		arguments.add(environment.getListFactory());
		
		return libraryConstructor.execute(interpreter, arguments);
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
		return numberFromFunction.execute(interpreter, interpreter.getEnvironment().getNumberFactory(), arguments);
	}
}
