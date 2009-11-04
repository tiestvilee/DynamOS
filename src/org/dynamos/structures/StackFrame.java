/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;


/**
 *
 * @author tiestvilee
 */
public class StackFrame {

    ObjectDOS object;
    ListDOS arguments = new ListDOS();

    public void setObject(ObjectDOS object) {
        this.object = object;
    }

    public ObjectDOS getObject() {
        return object;
    }

    public void pushArgument(ObjectDOS object) {
        arguments.add(object);
    }

    public ListDOS getArguments() {
        return arguments;

    }

}
