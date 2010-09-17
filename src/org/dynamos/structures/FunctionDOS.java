/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import java.util.Collections;
import java.util.List;

import org.dynamos.OpCodeInterpreter;
import org.dynamos.structures.Activation.ActivationBuilder;

/**
 *
 * @author tiestvilee
 */
public class FunctionDOS extends ExecutableDOS {
    private OpCode[] opCodes;
	private final Symbol[] requiredArguments;

    public FunctionDOS(Symbol[] requiredArguments, OpCode[] opCodes) {
		this.requiredArguments = requiredArguments;
        this.opCodes = opCodes;
    }

	@Override
	public ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, List<ObjectDOS> suppliedArguments) {
        Activation activation = newActivation(suppliedArguments, theObject);
        execute(interpreter, suppliedArguments, activation, theObject);
        return activation.getSlot(Symbol.RESULT);
	}

	public Activation newActivation(List<ObjectDOS> suppliedArguments, ObjectDOS theObject) {
		ActivationBuilder activationBuilder = (ActivationBuilder) this.getFunction(Symbol.get("newActivationWithArguments:andVictim:"));
		return activationBuilder.createActivation(suppliedArguments, theObject);
	}

    protected ObjectDOS execute(OpCodeInterpreter interpreter, List<ObjectDOS> suppliedArguments, Activation activation, ObjectDOS theObject) {
		ObjectDOS undefined = this.getFunction(Symbol.UNDEFINED).execute(interpreter, activation, Collections.<ObjectDOS>emptyList());
		updateArgumentsInContext(activation, suppliedArguments, undefined);
    	activation.setSlot(Symbol.RESULT, theObject);
    	
        interpreter.interpret(activation, opCodes);
        
        return activation.getSlot(Symbol.RESULT);
    }

	private void updateArgumentsInContext(ObjectDOS activation, List<ObjectDOS> contextArguments, ObjectDOS undefined) {
		int finalIndex = Math.min(contextArguments.size(), requiredArguments.length);
		int index = 0;
    	for(;index < finalIndex; index++) {
    		activation.setSlot(requiredArguments[index], contextArguments.get(index));
    	}
    	for(;index < requiredArguments.length; index++) {
    		activation.setSlot(requiredArguments[index], undefined);
    	}
	}

	public OpCode[] getOpCodes() {
		return opCodes;
	}
	
	public Symbol[] getRequiredArguments() {
		return requiredArguments;
	}

}
