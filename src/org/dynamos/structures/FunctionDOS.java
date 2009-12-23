/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import org.dynamos.OpCodeInterpreter;

/**
 *
 * @author tiestvilee
 */
public class FunctionDOS extends ExecutableDOS {
    private OpCodeInterpreter interpreter;
    private OpCode[] opCodes;
	private final Symbol[] arguments;

	// TODO should have parent set to rootObject
    public FunctionDOS(OpCodeInterpreter interpreter, Symbol[] arguments, OpCode[] opCodes) {
        this.interpreter = interpreter;
		this.arguments = arguments;
        this.opCodes = opCodes;
    }

    public void execute(Activation activation) {
    	ListDOS contextArguments = (ListDOS) activation.getSlot(Symbol.ARGUMENTS);
    	
		updateArgumentsInContext(activation, contextArguments);
    	activation.setSlot(Symbol.RESULT, activation);
    	
        interpreter.interpret(activation, opCodes);
    }
    
    public ObjectDOS construct(ListDOS contextArguments) {
    	ObjectDOS newObject = interpreter.getEnvironment().createNewObject();
    	newObject.setTrait("constructorFunctions", interpreter.newActivation());
    	
    	updateArgumentsInContext(newObject, contextArguments);
    	
        interpreter.interpret(newObject, opCodes);
        
        newObject.removeTrait("constructorFunctions");
        
        return newObject;
    }

	private void updateArgumentsInContext(ObjectDOS context, ListDOS contextArguments) {
		int finalIndex = Math.min(contextArguments.size(), arguments.length);
		int index = 0;
    	for(;index < finalIndex; index++) {
    		context.setSlot(arguments[index], contextArguments.at(index));
    	}
    	for(;index < arguments.length; index++) {
    		context.setSlot(arguments[index], interpreter.getEnvironment().getUndefined());
    	}
	}

	public Activation newActivation(Activation context, ListDOS arguments, ObjectDOS theObject) {
		// TODO where should this stuff be set?  in the interpreter?
		Activation activation = newActivation(arguments, theObject);
		activation.setContext(context);
		return activation;
	}

	public Activation newActivation(ListDOS arguments, ObjectDOS theObject) {
		// TODO where should this stuff be set?  in the interpreter?
		Activation activation = interpreter.newActivation();
        activation.setArguments(arguments);
        activation.setVictim(theObject);
		return activation;
	}

	public ObjectDOS newObject() {
		ObjectDOS newObject = interpreter.newObject();
		return newObject;
	}

	@Override
	public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
        Activation activation = newActivation(arguments, theObject);
        execute(activation);
        return activation.getSlot(Symbol.RESULT);
	}

}
