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
    private OpCode[] opCodes;
	private final Symbol[] requiredArguments;

	// TODO should have parent set to rootObject
    public FunctionDOS(Symbol[] requiredArguments, OpCode[] opCodes) {
		this.requiredArguments = requiredArguments;
        this.opCodes = opCodes;
    }

    public void execute(OpCodeInterpreter interpreter, Activation activation) {
    	ListDOS contextArguments = (ListDOS) activation.getSlot(Symbol.ARGUMENTS);
    	
		updateArgumentsInContext(activation, contextArguments, interpreter.getEnvironment().getUndefined());
    	activation.setSlot(Symbol.RESULT, activation);
    	
        interpreter.interpret(activation, opCodes);
    }
    
    public ObjectDOS construct(OpCodeInterpreter interpreter, ListDOS contextArguments) {
    	ObjectDOS newObject = interpreter.getEnvironment().createNewObject();
    	newObject.setTrait("activationFunctions", interpreter.newActivation());
    	
    	updateArgumentsInContext(newObject, contextArguments, interpreter.getEnvironment().getUndefined());
    	
        interpreter.interpret(newObject, opCodes);
        
        newObject.removeTrait("activationFunctions");
        
        return newObject;
    }

	private void updateArgumentsInContext(ObjectDOS context, ListDOS contextArguments, ObjectDOS undefined) {
		int finalIndex = Math.min(contextArguments.size(), requiredArguments.length);
		int index = 0;
    	for(;index < finalIndex; index++) {
    		context.setSlot(requiredArguments[index], contextArguments.at(index));
    	}
    	for(;index < requiredArguments.length; index++) {
    		context.setSlot(requiredArguments[index], undefined);
    	}
	}

	public Activation newActivation(OpCodeInterpreter interpreter, Activation context, ListDOS suppliedArguments, ObjectDOS theObject) {
		// TODO where should this stuff be set?  in the interpreter?
		Activation activation = newActivation(interpreter, suppliedArguments, theObject);
		activation.setContext(context);
		return activation;
	}

	public Activation newActivation(OpCodeInterpreter interpreter, ListDOS suppliedArguments, ObjectDOS theObject) {
		// TODO where should this stuff be set?  in the interpreter?
		Activation activation = interpreter.newActivation();
        activation.setArguments(suppliedArguments);
        activation.setVictim(theObject);
		return activation;
	}

	public ObjectDOS newObject(OpCodeInterpreter interpreter) {
		ObjectDOS newObject = interpreter.newObject();
		return newObject;
	}

	@Override
	public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, ListDOS suppliedArguments) {
        Activation activation = newActivation(interpreter, suppliedArguments, theObject);
        execute(interpreter, activation);
        return activation.getSlot(Symbol.RESULT);
	}

	public OpCode[] getOpCodes() {
		return opCodes;
	}

}
