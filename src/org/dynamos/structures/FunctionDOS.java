/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import java.util.List;

import org.dynamos.OpCodeInterpreter;
import org.dynamos.types.StandardObjects;

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

    public void execute(OpCodeInterpreter interpreter, List<ObjectDOS> suppliedArguments, Activation activation) {
		updateArgumentsInContext(activation, suppliedArguments, interpreter.getEnvironment().getUndefined());
    	activation.setSlot(Symbol.RESULT, activation);
    	
        interpreter.interpret(activation, opCodes);
    }
    
    public ObjectDOS construct(OpCodeInterpreter interpreter, List<ObjectDOS> contextArguments) {
    	ObjectDOS newObject = interpreter.getEnvironment().createNewObject();
    	newObject.setTrait("activationFunctions", interpreter.newActivation());
    	
    	updateArgumentsInContext(newObject, contextArguments, interpreter.getEnvironment().getUndefined());
    	
        interpreter.interpret(newObject, opCodes);
        
        newObject.removeTrait("activationFunctions");
        
        return newObject;
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

}
