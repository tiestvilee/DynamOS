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
public class Context extends ObjectDOS {

    List<ObjectDOS> arguments = new ArrayList<ObjectDOS>();
    ObjectDOS object;
    
    public Context() {
        super();
        //setSlot(Symbol.ARGUMENTS, arguments);
    }
    
    public void setObject(ObjectDOS object) {
        this.object = object;
        setSlot(Symbol.THIS, object);
    }

    public ObjectDOS getObject() {
        return object;
    }
    
    public void pushArgument(ObjectDOS object) {
        arguments.add(object);
    }

    public void setArguments(List<ObjectDOS> arguments) {
        //setSlot(Symbol.ARGUMENTS, arguments);
        this.arguments = arguments;
    }

    public List<ObjectDOS> getArguments() {
        return arguments;
    }

}
