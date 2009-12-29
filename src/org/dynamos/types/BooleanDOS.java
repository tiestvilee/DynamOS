/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.types;

import org.dynamos.Environment;
import org.dynamos.structures.ObjectDOS;
import org.dynamos.structures.OpCode;
import org.dynamos.structures.Symbol;

/**
 * 
 * @author tiestvilee
 */
public class BooleanDOS {
	public static class TrueDOS extends ObjectDOS {
		/* just for debugging really */
	}

	public static class FalseDOS extends ObjectDOS {
		/* just for debugging really */
	}

	public static ObjectDOS initialiseBooleans(Environment environment) {

		Symbol trueResult = Symbol.get("trueResult");
		Symbol falseResult = Symbol.get("falseResult");

		TrueDOS trueObject = new TrueDOS();
		trueObject.setParent(environment.getRootObject());
		trueObject.setFunction(Symbol.get("ifTrue:ifFalse:"), environment.createFunction( 
				new Symbol[] {trueResult, falseResult },
				new OpCode[] {
					new OpCode.PushSymbol(trueResult),
					new OpCode.FunctionCall(Symbol.GET_SLOT_$)
				}));

		FalseDOS falseObject = new FalseDOS();
		falseObject.setParent(environment.getRootObject());
		falseObject.setFunction(Symbol.get("ifTrue:ifFalse:"), environment.createFunction( 
				new Symbol[] {trueResult, falseResult },
				new OpCode[] {
					new OpCode.PushSymbol(falseResult),
					new OpCode.FunctionCall(Symbol.GET_SLOT_$)
				}));

		ObjectDOS booleanContainer = environment.createNewObject();
		booleanContainer.setSlot(Symbol.get("true"), trueObject);
		booleanContainer.setSlot(Symbol.get("false"), falseObject);
		return booleanContainer;
	}

}
