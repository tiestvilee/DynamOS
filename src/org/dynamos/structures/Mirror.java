package org.dynamos.structures;

import org.dynamos.Environment;

public class Mirror {

	public static ObjectDOS initialiseMirror(Environment environment) {
		ObjectDOS mirror = environment.createNewObject();
		
		mirror.setFunction(Symbol.SET_PARENT_TO_$_ON_$, SET_PARENT_TO_$_ON_$_EXEC);
		mirror.setFunction(Symbol.GET_PARENT_ON_$, GET_PARENT_ON_$_EXEC);
		mirror.setFunction(Symbol.SET_TRAIT_$_TO_$_ON_$, SET_TRAIT_$_TO_$_ON_$_EXEC);
		mirror.setFunction(Symbol.GET_TRAIT_$_ON_$, GET_TRAIT_$_ON_$_EXEC);
		
		return mirror;
	}
	
    private static ExecutableDOS SET_PARENT_TO_$_ON_$_EXEC = new ExecutableDOS() {
		@Override
		public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
			arguments.at(1).setParent(arguments.at(0));
			return arguments.at(1); // never return the mirror - just in case
		}
    };
    
    private static ExecutableDOS GET_PARENT_ON_$_EXEC = new ExecutableDOS() {
		@Override
		public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
			return arguments.at(0).getParent();
		}
    };
    
    private static ExecutableDOS SET_TRAIT_$_TO_$_ON_$_EXEC = new ExecutableDOS() {
		@Override
		public ObjectDOS execute(ObjectDOS  theObject, ListDOS arguments) {
			arguments.at(2).setTrait(arguments.at(0).toString(), arguments.at(1));
			return arguments.at(2); // never return the mirror - just in case
		}
    };
    
    private static ExecutableDOS GET_TRAIT_$_ON_$_EXEC = new ExecutableDOS() {
		@Override
		public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
			return arguments.at(1).getTrait(arguments.at(0).toString());
		}
    };

}
