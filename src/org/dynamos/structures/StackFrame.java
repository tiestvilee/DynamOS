/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author tiestvilee
 */
public class StackFrame {

    ObjectDOS object;
    List<ObjectDOS> arguments = new ArrayList<ObjectDOS>();

    public void setObject(ObjectDOS object) {
        this.object = object;
    }

    public ObjectDOS getObject() {
        return object;
    }

    public void pushArgument(ObjectDOS argument) {
        arguments.add(argument);
    }

    public List<ObjectDOS> getArguments() {
        return arguments;

    }

}
