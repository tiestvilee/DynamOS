/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos;

import org.dynamos.structures.Context;
import org.dynamos.structures.OpCode;
import org.dynamos.structures.StackFrame;

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
            if(opCodes[i].execute(context, stackFrame)) {
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
