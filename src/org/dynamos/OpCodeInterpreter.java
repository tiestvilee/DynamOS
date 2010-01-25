/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos;

import java.util.ArrayList;
import java.util.List;

import org.dynamos.structures.Activation;
import org.dynamos.structures.ObjectDOS;
import org.dynamos.structures.OpCode;
import org.dynamos.structures.OpCodeWrapper;
import org.dynamos.structures.StackFrame;
import org.dynamos.structures.Symbol;
import org.dynamos.types.StandardObjects;

/**
 *
 * @author tiestvilee
 */
public class OpCodeInterpreter {
	Environment environment;
	
	public OpCodeInterpreter() {
		environment = new Environment();
		environment.init(this);
	}

    public void interpret(ObjectDOS context, OpCode[] opCodes) {
        StackFrame stackFrame = new StackFrame();
        for(int i=0; i<opCodes.length; i++) {
			if(opCodes[i] instanceof OpCode.StartOpCodeList) {
				i = storeOpCodesInList(context, opCodes, i);
        	}
			else if(opCodes[i].execute(this, context, stackFrame)) {
                stackFrame = new StackFrame();
            }
        }
    }

	private int storeOpCodesInList(ObjectDOS context, OpCode[] opCodes, int index) {
		int opcodeListDepth = 1;
		List<ObjectDOS> opcodeList = new ArrayList<ObjectDOS>();
		int i = index + 1;
		for(;i<opCodes.length && opcodeListDepth > 0;i++) {
			if(opCodes[i] instanceof OpCode.StartOpCodeList) {
				opcodeListDepth++;
			} else 
			if(opCodes[i] instanceof OpCode.EndOpCodeList) {
				opcodeListDepth--;
			}
			opcodeList.add(new OpCodeWrapper(opCodes[i]));
		}
		opcodeList.remove(opcodeList.size() - 1); // get rid of last endopcode - not needed
		ObjectDOS opcodeDOSList = StandardObjects.toDOSList(opcodeList);
		context.setSlot(Symbol.RESULT, opcodeDOSList);
		return i - 1;
	}

	public Activation newActivation() {
		return environment.getContextBuilder().createActivation();
	}

	public Environment getEnvironment() {
		return environment;
	}

	public ObjectDOS newObject() {
		return environment.createNewObject();
	}

}
