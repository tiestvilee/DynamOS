/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author tiestvilee
 */
public class StackFrame {

    ObjectDOS object;
    List arguments = new ArrayList();

    public void setObject(ObjectDOS object) {
        this.object = object;
    }

    public ObjectDOS getObject() {
        return object;
    }

    public void pushArgument(Object object) {
        arguments.add(object);
    }

    public List getArguments() {
        return Collections.unmodifiableList(arguments);

    }

}
