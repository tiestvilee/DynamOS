package org.dynamos;

import org.dynamos.structures.Context;
import org.dynamos.structures.ObjectDOS;
import org.dynamos.structures.StandardObjects;
import org.dynamos.structures.VMObjectDOS;
import org.dynamos.structures.Context.ContextBuilder;

public class Environment {

	private Context.ContextBuilder contextBuilder;
	private ObjectDOS virtualMachine;
	/*
	 * need
	 *  object prototype
	 *  function prototype
	 *  VM
	 *  context prototype? - how are these built?
	 *  number (including prototype)
	 */

	public Environment(OpCodeInterpreter interpreter) {
		ObjectDOS rootObject = new ObjectDOS();
		ObjectDOS.initialiseRootObject(rootObject);
		virtualMachine = VMObjectDOS.getVMObject();
		contextBuilder = Context.initializeContext(interpreter, this);
	}
	
	public void init(OpCodeInterpreter interpreter) {
		StandardObjects.initialiseStandardObjects(interpreter, this);
	}
	
	public ObjectDOS getVirtualMachine() {
		return virtualMachine;
	}

	public ContextBuilder getContextBuilder() {
		return contextBuilder;
	}

}
