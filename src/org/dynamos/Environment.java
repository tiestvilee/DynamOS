package org.dynamos;

import org.dynamos.structures.Context;
import org.dynamos.structures.FunctionDOS;
import org.dynamos.structures.FunctionDefinitionDOS;
import org.dynamos.structures.ObjectDOS;
import org.dynamos.structures.OpCode;
import org.dynamos.structures.StandardObjects;
import org.dynamos.structures.Symbol;
import org.dynamos.structures.VMObjectDOS;
import org.dynamos.structures.Context.ContextBuilder;
import org.dynamos.structures.StandardObjects.NullDOS;
import org.dynamos.structures.StandardObjects.UndefinedDOS;

public class Environment {

	private Context.ContextBuilder contextBuilder;
	private ObjectDOS virtualMachine;
	private ObjectDOS rootObject;
	private ObjectDOS numberFactory;
    
	private ObjectDOS nullDOS;
    private ObjectDOS undefined;
	private ObjectDOS booleanContainer;
	private final OpCodeInterpreter interpreter;

    /*
	 * need
	 *  object prototype
	 *  function prototype
	 *  VM
	 *  context prototype? - how are these built?
	 *  number (including prototype)
	 */

	public Environment(OpCodeInterpreter interpreter) {
		this.interpreter = interpreter;
		
		rootObject = new ObjectDOS();
		ObjectDOS.initialiseRootObject(rootObject);
		
		nullDOS = new NullDOS();
		nullDOS.setParent(rootObject);
		undefined = new UndefinedDOS();
		undefined.setParent(rootObject);
		
		virtualMachine = VMObjectDOS.getVMObject(this);
		contextBuilder = Context.initializeContext(interpreter, this);
	}
	
	public void init(OpCodeInterpreter interpreter) {
		booleanContainer = StandardObjects.initialiseBooleans(interpreter, this);
        numberFactory = StandardObjects.createNumberLibrary(interpreter, this);
    }
	
	public ObjectDOS getVirtualMachine() {
		return virtualMachine;
	}

	public ContextBuilder getContextBuilder() {
		return contextBuilder;
	}
	
	public ObjectDOS getNumberFactory() {
		return numberFactory;
	}
	
	public ObjectDOS getUndefined() {
		return undefined;
	}
	
	public ObjectDOS getNull() {
		return nullDOS;
	}
	
	public ObjectDOS createNewObject() {
		ObjectDOS object = new ObjectDOS();
		object.setParent(rootObject);
		return object;
	}
	
	public ObjectDOS createNewValueObject(int value) {
		StandardObjects.ValueObject object = new StandardObjects.ValueObject(value);
		object.setParent(rootObject);
		return object;
	}

	public ObjectDOS getTrue() {
		return booleanContainer.getSlot(Symbol.get("true"));
	}

	public ObjectDOS getFalse() {
		return booleanContainer.getSlot(Symbol.get("false"));
	}

	public ObjectDOS getRootObject() {
		return rootObject;
	}

	public FunctionDOS createFunction(Symbol[] arguments, Symbol[] locals, OpCode[] opCodes, Context localContext) {
		return createFunction(createFunctionDefinition(arguments, locals, opCodes), localContext);
	}

	public FunctionDOS createFunction(FunctionDefinitionDOS functionDefinition, Context localContext) {
		FunctionDOS function = new FunctionDOS(
				functionDefinition,
				localContext);
		function.setParent(rootObject);
		return function;
	}

	public FunctionDefinitionDOS createFunctionDefinition(Symbol[] arguments, Symbol[] locals, OpCode[] opCodes) {
		FunctionDefinitionDOS functionDefinition = new FunctionDefinitionDOS(
				interpreter, 
				arguments,
				locals, 
				opCodes);
		functionDefinition.setParent(rootObject);
		return functionDefinition;
	}

}
