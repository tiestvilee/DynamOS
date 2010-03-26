package org.dynamos.structures;

import org.dynamos.Environment;

public class Mirror {


	
	public static final Symbol SET_FUNCTION_$_TO_$ = Symbol.get("setFunction:to:"); 
	public static final Symbol SET_SLOT_$_TO_$ = Symbol.get("setSlot:to:"); 
	public static final Symbol GET_SLOT_$ = Symbol.get("getSlot:"); 
	public static final Symbol MIRRORED_SLOT = Symbol.get("mirrored"); 
	public static final Symbol SET_PARENT_TO_$ = Symbol.get("setParentTo:"); 
	public static final Symbol GET_PARENT = Symbol.get("getParent"); 
	public static final Symbol SET_TRAIT_$_TO_$ = Symbol.get("setTrait:To:"); 
	public static final Symbol GET_TRAIT_$ = Symbol.get("getTrait:"); 

    public static ObjectDOS createMirrorPrototype(Environment environment, ObjectDOS metaVM) {
    	ObjectDOS mirrorPrototype = environment.createNewObject();
    	mirrorPrototype.setSlot(Symbol.META_VM, metaVM);
    	
    	mirrorPrototype.setFunction(SET_PARENT_TO_$, environment.createFunction(new Symbol[] {Symbol.get("parent")}, new OpCode[] {
    		new OpCode.Push(Symbol.get("parent")),
    		new OpCode.Push(MIRRORED_SLOT),
    		new OpCode.SetObject(Symbol.META_VM),
    		new OpCode.FunctionCall(MetaVM.SET_PARENT_TO_$_ON_$)
    	}));
    	
    	mirrorPrototype.setFunction(GET_PARENT, environment.createFunction(new Symbol[] {}, new OpCode[] {
    		new OpCode.Push(MIRRORED_SLOT),
    		new OpCode.SetObject(Symbol.META_VM),
    		new OpCode.FunctionCall(MetaVM.GET_PARENT_ON_$)
    	}));
    	
    	mirrorPrototype.setFunction(SET_TRAIT_$_TO_$, environment.createFunction(new Symbol[] {Symbol.get("traitName"), Symbol.get("traitObject")}, new OpCode[] {
    		new OpCode.Push(Symbol.get("traitName")),
    		new OpCode.Push(Symbol.get("traitObject")),
    		new OpCode.Push(MIRRORED_SLOT),
    		new OpCode.SetObject(Symbol.META_VM),
    		new OpCode.FunctionCall(MetaVM.SET_TRAIT_$_TO_$_ON_$)
    	}));
    	
    	mirrorPrototype.setFunction(GET_TRAIT_$, environment.createFunction(new Symbol[] {Symbol.get("traitName")}, new OpCode[] {
   			new OpCode.Push(Symbol.get("traitName")),
    		new OpCode.Push(MIRRORED_SLOT),
    		new OpCode.SetObject(Symbol.META_VM),
    		new OpCode.FunctionCall(MetaVM.GET_TRAIT_$_ON_$)
    	}));
    	
    	mirrorPrototype.setFunction(SET_FUNCTION_$_TO_$, environment.createFunction(new Symbol[] {Symbol.get("functionName"), Symbol.get("functionObject")}, new OpCode[] {
    		new OpCode.Push(Symbol.get("functionName")),
    		new OpCode.Push(Symbol.get("functionObject")),
    		new OpCode.Push(MIRRORED_SLOT),
    		new OpCode.SetObject(Symbol.META_VM),
    		new OpCode.FunctionCall(MetaVM.SET_FUNCTION_$_TO_$_ON_$)
    	}));
    	
    	mirrorPrototype.setFunction(SET_SLOT_$_TO_$, environment.createFunction(new Symbol[] {Symbol.get("slot"), Symbol.get("value")}, new OpCode[] {
    		new OpCode.Push(Symbol.get("slot")),
    		new OpCode.Push(Symbol.get("value")),
    		new OpCode.Push(MIRRORED_SLOT),
    		new OpCode.SetObject(Symbol.META_VM),
    		new OpCode.FunctionCall(MetaVM.SET_SLOT_$_TO_$_ON_$)
    	}));
        	
    	mirrorPrototype.setFunction(GET_SLOT_$, environment.createFunction(new Symbol[] {Symbol.get("slot")}, new OpCode[] {
    		new OpCode.Push(Symbol.get("slot")),
    		new OpCode.Push(MIRRORED_SLOT),
    		new OpCode.SetObject(Symbol.META_VM),
    		new OpCode.FunctionCall(MetaVM.GET_SLOT_$_ON_$)
    	}));
            	
		return mirrorPrototype;
	}



}
