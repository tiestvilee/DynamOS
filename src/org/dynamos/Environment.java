package org.dynamos;

import org.dynamos.structures.Activation;
import org.dynamos.structures.FunctionDOS;
import org.dynamos.structures.FunctionWithContext;
import org.dynamos.structures.MetaVM;
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

	private Activation.ActivationBuilder activationBuilder;
	private ObjectDOS virtualMachine;
	private ObjectDOS rootObject;
	private ObjectDOS zero;

	private ObjectDOS nullDOS;
    private ObjectDOS undefined;
	private ObjectDOS metaVM;
	private ObjectDOS booleanContainer;
	private ObjectDOS emptyList;
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

        System.out.println("Initialise MetaVM");
		metaVM = MetaVM.initialiseMetaVM(this);

        System.out.println("Initialise VM");
		virtualMachine = VMObjectDOS.getVMObject(this);
        System.out.println("Initialise empty list");
		emptyList = StandardObjects.createEmptyList(this);
        System.out.println("Initialise Activation");
		activationBuilder = Activation.initializeActivation(this);
	}

	public void init(OpCodeInterpreter interpreter) {
        System.out.println("Initialise Boolean");
		booleanContainer = BooleanDOS.initialiseBooleans(this);
        System.out.println("Initialise number");
        zero = NumberDOS.createZero(interpreter, this);
        System.out.println("Initialise function prototype");
        functionPrototype = FunctionWithContext.createFunctionPrototype(this);
    }

	public ObjectDOS getVirtualMachine() {
		return virtualMachine;
	}

	public ActivationBuilder getContextBuilder() {
		return activationBuilder;
	}

	public ObjectDOS getMetaVM() {
		return metaVM;
	}

	public ObjectDOS getZero() {
		return zero;
	}

	public ObjectDOS getEmptyList() {
		return emptyList;
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

}
