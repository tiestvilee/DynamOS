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
    ObjectDOS result;
    private Symbol resultTarget = Symbol.RESULT;
    
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

    public List getArguments() {
        return arguments;
    }

    public void setResult(ObjectDOS result) {
        this.result = result;
    }

    public ObjectDOS getResult() {
        return result;
    }

    public void setResultTarget(Symbol resultTarget) {
        this.resultTarget = resultTarget;
    }

    public Symbol getResultTarget() {
        return resultTarget;
    }

}
