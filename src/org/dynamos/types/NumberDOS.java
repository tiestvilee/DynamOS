/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.types;

import java.io.IOException;
import java.io.InputStream;

import org.dynamos.Environment;
import org.dynamos.OpCodeInterpreter;
import org.dynamos.compiler.BootstrapCompiler;
import org.dynamos.structures.ConstructorDOS;
import org.dynamos.structures.ExecutableDOS;
import org.dynamos.structures.ListDOS;
import org.dynamos.structures.ObjectDOS;
import org.dynamos.structures.Symbol;

/**
 * 
 * @author tiestvilee
 */
public class NumberDOS {

	public static ObjectDOS createNumberLibrary(OpCodeInterpreter interpreter, Environment environment) {

		String filename = "NumberDOS.oc";
		String libraryProgram = loadFile(filename);
		
		ConstructorDOS libraryConstructor = new BootstrapCompiler().compile(libraryProgram);
		
		ListDOS arguments = new ListDOS();
		arguments.add(environment.getVirtualMachine());
		arguments.add(environment.getListFactory());
		
		return libraryConstructor.execute(interpreter, arguments);
	}

	private static String loadFile(String filename) {
		String libraryProgram = "";
		try {
			byte[] buf = new byte[12*1000];
			InputStream in = NumberDOS.class.getResourceAsStream(filename);
			
            if (in != null) {
                try {
                    int total = 0;
                    while (true) {
                        int numRead = in.read(buf,
                                total, buf.length-total);
                        if (numRead <= 0) {
                            break;
                        }
                        total += numRead;
                    }
                    byte[] stringBuf = new byte[total];
                    System.arraycopy(buf, 0, stringBuf, 0, total);
                    libraryProgram = new String(stringBuf);
                } catch (Exception e) {} finally {
                    in.close();
                }
            }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return libraryProgram;
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
