/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;


/**
 *
 * @author tiestvilee
 */
public class VMObjectDOS {

    private static final ExecutableDOS PRINT_FUNCTION = new ExecutableDOS() {

        @Override
        public ObjectDOS execute(ObjectDOS object, ListDOS arguments) {
            System.out.println(arguments.at(0));
            return StandardObjects.NULL;
        }

    };

    public static final ObjectDOS VM;
    static {
        VM = new ObjectDOS();
        VM.setFunction(Symbol.get("print"), PRINT_FUNCTION);
        System.out.println("initialized VM");
    }
}
