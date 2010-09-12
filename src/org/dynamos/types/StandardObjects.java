/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.types;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dynamos.Environment;
import org.dynamos.compiler.AnonymousFunctionNode;
import org.dynamos.compiler.BootstrapCompiler;
import org.dynamos.compiler.StatementContainingNode;
import org.dynamos.structures.FunctionDOS;
import org.dynamos.structures.ObjectDOS;
import org.dynamos.structures.Symbol;

/**
 *
 * @author tiestvilee
 */
public class StandardObjects {
	// TODO how to get at args - by index, by name
	// TODO how to contextualise functions - how are non-contextual functions
	// scoped
	// TODO how to set context values
	// TODO if we update Number, it must be our own version of Number, but then
	// PICs are unique to every sandbox!

	public static class NullDOS extends ObjectDOS {
		/* just for debugging really */
	}

	public static class UndefinedDOS extends ObjectDOS {
		/* just for debugging really */
	}

	public static class TrueDOS extends ObjectDOS {
		/* just for debugging really */
	}

	public static class FalseDOS extends ObjectDOS {
		/* just for debugging really */
	}

	private static Environment env;
	public static void setEnvironment(Environment environment) {
		env = environment;
	}
	public static ObjectDOS createEmptyList(Environment environment) {
		env = environment;

		String program = loadFile("ListDOS.oc", StandardObjects.class);
        System.out.println(program);

		Matcher matcher = Pattern.compile("\\/\\/ function start([\\s\\S]+?)\\/\\/ function end", Pattern.MULTILINE).matcher(program);

		BootstrapCompiler compiler = new BootstrapCompiler();
		ObjectDOS emptyList = environment.createNewObject();

		while(matcher.find()) {
			String functionString = matcher.group(1);
			FunctionDOS function = compiler.compileFunction(functionString);
			Symbol functionName = determineFunctionName(compiler, functionString);
			emptyList.setFunction(functionName, function);
		}

		matcher = Pattern.compile("\\/\\/ constructor start([\\s\\S]+?)\\/\\/ constructor end").matcher(program);

		matcher.find();
		String constructorString = matcher.group(1);
		System.out.println(constructorString);
		FunctionDOS function = compiler.compileFunction(constructorString);
		Symbol functionName = determineFunctionName(compiler, constructorString);
		emptyList.setFunction(functionName, function);

		emptyList.setSlot(Symbol.get("head"), environment.getUndefined());
		emptyList.setSlot(Symbol.get("tail"), environment.getUndefined());

		return emptyList;
	}

	private static Symbol determineFunctionName(BootstrapCompiler compiler,
			String functionString) {
		StatementContainingNode ast = compiler.transform(functionString);
		String name = ((AnonymousFunctionNode) ast.getStatements().get(0)).getName();
		return Symbol.get(name);
	}

	public static String loadFile(String filename, Class clazz) {
		String libraryProgram = "";
		try {
			byte[] buf = new byte[12*1000];
			InputStream in = clazz.getResourceAsStream(filename);

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
                } catch (Exception e) {
                	throw new RuntimeException("couldn't load " + clazz, e);
                } finally {
                    in.close();
                }
            }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return libraryProgram;
	}

	public static List<ObjectDOS> toJavaList(ObjectDOS listDOS) {
		List<ObjectDOS> list = toJavaListRecurse(listDOS);
		Collections.reverse(list);
		return list;
	}

	private static List<ObjectDOS> toJavaListRecurse(ObjectDOS listDOS) {
		List<ObjectDOS> result;
		ObjectDOS head = listDOS.getSlot(Symbol.get("head"));
		//System.out.println(head);

		ObjectDOS tail = listDOS.getSlot(Symbol.get("tail"));
		if(tail != env.getUndefined() && tail != null && tail != listDOS) {
			result = toJavaListRecurse(tail);
		} else {
			result = new ArrayList<ObjectDOS>();
		}

		if(head != env.getUndefined() && head != null) {
			result.add(head);
		}

		return result;
	}

	// TODO move this into environment or some such.  NOT static
	public static ObjectDOS toDOSList(List<ObjectDOS> arguments) {
		if(arguments.size() > 0) {
			return toDOSListRecurse(arguments);
		}
		return new ObjectDOS(); //env.getEmptyList();
	}

	public static ObjectDOS toDOSListRecurse(List<ObjectDOS> arguments) {
		ObjectDOS emptyList = env.getEmptyList();
		ObjectDOS list = env.createNewObject();
		list.setParent(emptyList);
		list.setSlot(Symbol.get("head"), arguments.get(0));
		list.setSlot(Symbol.get("tail"), env.getUndefined());
		if(arguments.size() > 1) {
			list.setSlot(Symbol.get("tail"), toDOSListRecurse(arguments.subList(1, arguments.size())));
		}
		return list;
	}

}
