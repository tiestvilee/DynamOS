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

    private static final FunctionDOS PRINT_FUNCTION = new FunctionDOS(null) {

        @Override
        public void execute(Context context) {
            final List arguments = (List) context.getSlot(Symbol.ARGUMENTS);
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
