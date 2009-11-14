/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos;

import org.dynamos.structures.Context;
import org.dynamos.structures.ListDOS;
import org.dynamos.structures.OpCode;
import org.dynamos.structures.OpCodeWrapper;
import org.dynamos.structures.StackFrame;
import org.dynamos.structures.Symbol;

/**
 *
 * @author tiestvilee
 */
public class OpCodeInterpreter {
	Environment environment;
	
	public OpCodeInterpreter() {
		environment = new Environment(this);
		environment.init(this);
	}

    public void interpret(Context context, OpCode[] opCodes) {
        StackFrame stackFrame = new StackFrame();
        for(int i=0; i<opCodes.length; i++) {
			if(opCodes[i] instanceof OpCode.StartOpCodeList) {
				i = storeOpCodesInList(context, opCodes, i);
        	}
			else if(opCodes[i].execute(context, stackFrame)) {
                stackFrame = new StackFrame();
            }
        }
    }

	private int storeOpCodesInList(Context context, OpCode[] opCodes, int i) {
		int opcodeListDepth = 1;
		ListDOS opcodeList = new ListDOS();
		i += 1;
		for(;i<opCodes.length && opcodeListDepth > 0;i++) {
			if(opCodes[i] instanceof OpCode.StartOpCodeList) {
				opcodeListDepth++;
			} else 
			if(opCodes[i] instanceof OpCode.EndOpCodeList) {
				opcodeListDepth--;
			}
			opcodeList.add(new OpCodeWrapper(opCodes[i]));
		}
		opcodeList.getRawList().remove(opcodeList.getRawList().size() - 1); // get rid of last endopcode - not needed
		context.setSlot(Symbol.RESULT, opcodeList);
		return i - 1;
	}

	public Context newContext() {
		return environment.getContextBuilder().createContext();
	}

	public Environment getEnvironment() {
		return environment;
	}

}
