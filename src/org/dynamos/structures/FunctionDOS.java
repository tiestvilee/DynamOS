/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import java.util.List;

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

    public ObjectDOS execute(OpCodeInterpreter interpreter, List<ObjectDOS> suppliedArguments, Activation activation) {
		updateArgumentsInContext(activation, suppliedArguments, interpreter.getEnvironment().getUndefined());
    	activation.setSlot(Symbol.RESULT, activation);
    	
        interpreter.interpret(activation, opCodes);
        
        return activation.getSlot(Symbol.RESULT);
    }

	private void updateArgumentsInContext(ObjectDOS context, List<ObjectDOS> contextArguments, ObjectDOS undefined) {
		int finalIndex = Math.min(contextArguments.size(), requiredArguments.length);
		int index = 0;
    	for(;index < finalIndex; index++) {
    		context.setSlot(requiredArguments[index], contextArguments.get(index));
    	}
    	for(;index < requiredArguments.length; index++) {
    		context.setSlot(requiredArguments[index], undefined);
    	}
	}

	public Activation newActivation(OpCodeInterpreter interpreter, Activation context, List<ObjectDOS> suppliedArguments, ObjectDOS theObject) {
		// TODO where should this stuff be set?  in the interpreter?
		Activation activation = newActivation(interpreter, suppliedArguments, theObject);
		activation.setContext(context);
		return activation;
	}

	public Activation newActivation(OpCodeInterpreter interpreter, List<ObjectDOS> suppliedArguments, ObjectDOS theObject) {
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
	public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, List<ObjectDOS> suppliedArguments) {
        Activation activation = newActivation(interpreter, suppliedArguments, theObject);
        execute(interpreter, suppliedArguments, activation);
        return activation.getSlot(Symbol.RESULT);
	}

	public OpCode[] getOpCodes() {
		return opCodes;
	}
	
	public Symbol[] getRequiredArguments() {
		return requiredArguments;
	}

}
