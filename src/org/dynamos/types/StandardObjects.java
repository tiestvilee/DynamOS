/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.types;

import org.dynamos.Environment;
import org.dynamos.structures.ExecutableDOS;
import org.dynamos.structures.ListDOS;
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

	public static ObjectDOS createListLibrary(Environment environment) {
		ObjectDOS listFactory = environment.createNewObject();
		
		// add appropriate function to the prototype
		listFactory.setFunction(Symbol.NEW_LIST, new ExecutableDOS() {

			@Override
			public ObjectDOS execute(org.dynamos.OpCodeInterpreter interpreter, ObjectDOS theObject, ListDOS arguments) {
				return new ListDOS();
			}
			
		});

		return listFactory;
	}

}
