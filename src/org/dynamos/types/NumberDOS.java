/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.types;

import java.util.ArrayList;
import java.util.List;

import org.dynamos.Environment;
import org.dynamos.OpCodeInterpreter;
import org.dynamos.compiler.BootstrapCompiler;
import org.dynamos.structures.FunctionDOS;
import org.dynamos.structures.ObjectDOS;
import org.dynamos.structures.Symbol;

/**
 *
 * @author tiestvilee
 */
public class NumberDOS {

	public static ObjectDOS createZero(OpCodeInterpreter interpreter, Environment environment) {

		String libraryProgram = StandardObjects.loadFile("NumberDOS.oc", NumberDOS.class);

        System.out.println(libraryProgram);

		FunctionDOS libraryConstructor = new BootstrapCompiler().compileFunction(libraryProgram);

		List<ObjectDOS> arguments = new ArrayList<ObjectDOS>();
		arguments.add(environment.getVirtualMachine());
		arguments.add(environment.getEmptyList());

		return libraryConstructor.execute(interpreter, arguments, interpreter.newActivation());
	}

	public static ObjectDOS numberDOS(OpCodeInterpreter interpreter, int number) {
		ObjectDOS valueObject = interpreter.getEnvironment().createNewValueObject(number);
		ObjectDOS zero = interpreter.getEnvironment().getZero();
		List<ObjectDOS> arguments = new ArrayList<ObjectDOS>();
		arguments.add(valueObject);
		return zero.getFunction(Symbol.get("addValue:")).execute(interpreter, interpreter.getEnvironment().getZero(), arguments);
	}
}
