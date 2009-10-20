/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos;

import org.dynamos.structures.Context;
import org.dynamos.structures.ObjectDOS;
import org.dynamos.structures.OpCode;

/**
 *
 * @author tiestvilee
 */
public class OpCodeInterpreter {

    void interpret(Context context, OpCode[] opCodes) {
        Context newContext = createNewContext(context);
        for(int i=0; i<opCodes.length; i++) {
            if(opCodes[i].execute(newContext)) {
                newContext = createNewContext(context);
            }
        }
    }

    private Context createNewContext(Context context) {
        Context newContext = new Context();
        newContext.setParent(context);
        return newContext;
    }

}
