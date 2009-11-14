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
        int opcodeListDepth = 0;
        for(int i=0; i<opCodes.length; i++) {
			if(opCodes[i] instanceof OpCode.StartOpCodeList) {
        		opcodeListDepth++;
        		ListDOS opcodeList = new ListDOS();
                i += 1;
				System.out.println("starting opcode list " + i + " depth " + opcodeListDepth);
                for(;i<opCodes.length && opcodeListDepth > 0;i++) {
					if(opCodes[i] instanceof OpCode.StartOpCodeList) {
						System.out.println("another opcode list " + i + " depth " + opcodeListDepth);
		        		opcodeListDepth++;
		        	} else 
					if(opCodes[i] instanceof OpCode.EndOpCodeList) {
						System.out.println("end of an opcode list " + i + " depth " + opcodeListDepth);
		        		opcodeListDepth--;
		        	} else {
						System.out.println("add opcode " + opCodes[i]);
		        		opcodeList.add(new OpCodeWrapper(opCodes[i]));
		        	}
                }
        		context.setSlot(Symbol.RESULT, opcodeList);
                i -= 1;
        	}
			else if(opCodes[i].execute(context, stackFrame)) {
                stackFrame = new StackFrame();
            }
        }
    }

	public Context newContext() {
		return environment.getContextBuilder().createContext();
	}

	public Environment getEnvironment() {
		return environment;
	}

}
