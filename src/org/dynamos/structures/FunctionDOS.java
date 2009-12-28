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
	private final Symbol[] requiredArguments;

	// TODO should have parent set to rootObject
    public FunctionDOS(OpCodeInterpreter interpreter, Symbol[] requiredArguments, OpCode[] opCodes) {
        this.interpreter = interpreter;
		this.requiredArguments = requiredArguments;
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
    	newObject.setTrait("activationFunctions", interpreter.newActivation());
    	
    	updateArgumentsInContext(newObject, contextArguments);
    	
        interpreter.interpret(newObject, opCodes);
        
        newObject.removeTrait("activationFunctions");
        
        return newObject;
    }

	private void updateArgumentsInContext(ObjectDOS context, ListDOS contextArguments) {
		int finalIndex = Math.min(contextArguments.size(), requiredArguments.length);
		int index = 0;
    	for(;index < finalIndex; index++) {
    		context.setSlot(requiredArguments[index], contextArguments.at(index));
    	}
    	for(;index < requiredArguments.length; index++) {
    		context.setSlot(requiredArguments[index], interpreter.getEnvironment().getUndefined());
    	}
	}

	public Activation newActivation(Activation context, ListDOS suppliedArguments, ObjectDOS theObject) {
		// TODO where should this stuff be set?  in the interpreter?
		Activation activation = newActivation(suppliedArguments, theObject);
		activation.setContext(context);
		return activation;
	}

	public Activation newActivation(ListDOS suppliedArguments, ObjectDOS theObject) {
		// TODO where should this stuff be set?  in the interpreter?
		Activation activation = interpreter.newActivation();
        activation.setArguments(suppliedArguments);
        activation.setVictim(theObject);
		return activation;
	}

	public ObjectDOS newObject() {
		ObjectDOS newObject = interpreter.newObject();
		return newObject;
	}

	@Override
	public ObjectDOS execute(ObjectDOS theObject, ListDOS suppliedArguments) {
        Activation activation = newActivation(suppliedArguments, theObject);
        execute(activation);
        return activation.getSlot(Symbol.RESULT);
	}

}
