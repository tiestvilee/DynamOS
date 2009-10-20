/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import java.util.List;

/**
 *
 * @author tiestvilee
 */
public class VMObjectDOS {

    private static final FunctionDOS.ContextualFunctionDOS PRINT_FUNCTION = new FunctionDOS.ContextualFunctionDOS(null, null) {

        @Override
        public void execute(ObjectDOS object, List arguments) {
            System.out.println(arguments.get(0));
        }

    };

    public static final ObjectDOS VM;
    static {
        VM = new ObjectDOS();
        VM.setSlot(Symbol.get("print"), PRINT_FUNCTION);
        System.out.println("initialized VM");
    }
}
