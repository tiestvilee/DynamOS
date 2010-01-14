package org.dynamos;

import org.dynamos.structures.Activation;
import org.dynamos.structures.ConstructorDOS;
import org.dynamos.structures.FunctionDOS;
import org.dynamos.structures.FunctionWithContext;
import org.dynamos.structures.Mirror;
import org.dynamos.structures.ObjectDOS;
import org.dynamos.structures.OpCode;
import org.dynamos.structures.Symbol;
import org.dynamos.structures.VMObjectDOS;
import org.dynamos.structures.Activation.ActivationBuilder;
import org.dynamos.types.BooleanDOS;
import org.dynamos.types.NumberDOS;
import org.dynamos.types.StandardObjects;
import org.dynamos.types.StandardObjects.NullDOS;
import org.dynamos.types.StandardObjects.UndefinedDOS;

public class Environment {

	private Activation.ActivationBuilder contextBuilder;
	private ObjectDOS virtualMachine;
	private ObjectDOS rootObject;
	private ObjectDOS numberFactory;
    
	private ObjectDOS nullDOS;
    private ObjectDOS undefined;
	private ObjectDOS mirror;
	private ObjectDOS booleanContainer;
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

	public Environment() {
		rootObject = new ObjectDOS();

		nullDOS = new NullDOS();
		nullDOS.setParent(rootObject);
		undefined = new UndefinedDOS();
		undefined.setParent(rootObject);
		
		ObjectDOS.initialiseRootObject(this);
		
		mirror = Mirror.initialiseMirror(this);
		
		virtualMachine = VMObjectDOS.getVMObject(this);
		contextBuilder = Activation.initializeContext(this);
	}
	
	public void init() {
		booleanContainer = BooleanDOS.initialiseBooleans(this);
        numberFactory = NumberDOS.createNumberLibrary(this);
        listFactory = StandardObjects.createListLibrary(this);
        functionPrototype = FunctionWithContext.createFunctionPrototype(this);
    }
	
	public ObjectDOS getVirtualMachine() {
		return virtualMachine;
	}

	public ActivationBuilder getContextBuilder() {
		return contextBuilder;
	}
	
	public ObjectDOS getMirror() {
		return mirror;
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
		object.setSlot(Symbol.THIS, object);
		return object;
	}
	
	public ObjectDOS createNewValueObject(int value) {
		NumberDOS.ValueObject object = new NumberDOS.ValueObject(value);
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
				arguments,
				opCodes);
		functionDefinition.setParent(rootObject);
		return functionDefinition;
	}

	public ConstructorDOS createConstructor(Symbol[] arguments, OpCode[] opCodes) {
		FunctionDOS functionDefinition = new FunctionDOS(
				arguments,
				opCodes);
		functionDefinition.setParent(rootObject);
		ConstructorDOS function = new ConstructorDOS(functionDefinition);
		function.setParent(rootObject);
		return function;
	}

}
