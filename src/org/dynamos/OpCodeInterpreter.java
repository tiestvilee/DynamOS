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

    public void interpret(Context context, OpCode[] opCodes) {
        StackFrame stackFrame = new StackFrame();
        for(int i=0; i<opCodes.length; i++) {
            if(opCodes[i].execute(context, stackFrame)) {
                stackFrame = new StackFrame();
            }
        }
    }

}
