package org.dynamos;

import org.dynamos.structures.ConstructorDOS;
import org.dynamos.structures.Activation;
import org.dynamos.structures.FunctionWithContext;
import org.dynamos.structures.FunctionDOS;
import org.dynamos.structures.ObjectDOS;
import org.dynamos.structures.OpCode;
import org.dynamos.structures.StandardObjects;
import org.dynamos.structures.Symbol;
import org.dynamos.structures.VMObjectDOS;
import org.dynamos.structures.Activation.ActivationBuilder;
import org.dynamos.structures.StandardObjects.NullDOS;
import org.dynamos.structures.StandardObjects.UndefinedDOS;

public class Environment {

	private Activation.ActivationBuilder contextBuilder;
	private ObjectDOS virtualMachine;
	private ObjectDOS rootObject;
	private ObjectDOS numberFactory;
    
	private ObjectDOS nullDOS;
    private ObjectDOS undefined;
	private ObjectDOS booleanContainer;
	private final OpCodeInterpreter interpreter;
	private ObjectDOS listFactory;
	private ObjectDOS functionPrototype;

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

		nullDOS = new NullDOS();
		nullDOS.setParent(rootObject);
		undefined = new UndefinedDOS();
		undefined.setParent(rootObject);
		
		ObjectDOS.initialiseRootObject(this, rootObject);
		
		virtualMachine = VMObjectDOS.getVMObject(this);
		contextBuilder = Activation.initializeContext(interpreter, this);
	}
	
	public void init(OpCodeInterpreter interpreter) {
		booleanContainer = StandardObjects.initialiseBooleans(interpreter, this);
        numberFactory = StandardObjects.createNumberLibrary(interpreter, this);
        listFactory = StandardObjects.createListLibrary(interpreter, this);
        functionPrototype = FunctionWithContext.createFunctionPrototype(this);
    }
	
	public ObjectDOS getVirtualMachine() {
		return virtualMachine;
	}

	public ActivationBuilder getContextBuilder() {
		return contextBuilder;
	}
	
	public ObjectDOS getNumberFactory() {
		return numberFactory;
	}
	
	public ObjectDOS getListFactory() {
		return listFactory;
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

	public FunctionWithContext createFunctionWithContext(Symbol[] arguments, OpCode[] opCodes, Activation localContext) {
		return createFunctionWithContext(createFunction(arguments, opCodes), localContext);
	}

	public FunctionWithContext createFunctionWithContext(FunctionDOS functionDefinition, Activation localContext) {
		FunctionWithContext function = new FunctionWithContext(
				functionDefinition,
				localContext);
		function.setParent(functionPrototype);
		return function;
	}

	public FunctionDOS createFunction(Symbol[] arguments, OpCode[] opCodes) {
		FunctionDOS functionDefinition = new FunctionDOS(
				interpreter, 
				arguments,
				opCodes);
		functionDefinition.setParent(rootObject);
		return functionDefinition;
	}

	public ObjectDOS createConstructor(Symbol[] arguments, OpCode[] opCodes, ObjectDOS localContext) {
		FunctionDOS functionDefinition = new FunctionDOS(
				interpreter, 
				arguments,
				opCodes);
		functionDefinition.setParent(rootObject);
		ConstructorDOS function = new ConstructorDOS(
				functionDefinition,
				localContext);
		function.setParent(rootObject);
		return function;
	}

}
