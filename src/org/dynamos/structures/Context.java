/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;


/**
 *
 * @author tiestvilee
 */
public class Context extends ObjectDOS {

    ListDOS arguments = new ListDOS();
    ObjectDOS object;
    
    public Context() {
        super();
        setSlot(Symbol.ARGUMENTS, arguments);
        setSlot(Symbol.CURRENT_CONTEXT, this);
        setSlot(Symbol.CONTEXTUALIZE_FUNCTION, new ContextualizeFunction());
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

    public void setArguments(ListDOS arguments) {
        setSlot(Symbol.ARGUMENTS, arguments);
        this.arguments = arguments;
    }

    public ListDOS getArguments() {
        return arguments;
    }
    
    private class ContextualizeFunction extends FunctionDOS.ContextualFunctionDOS {
    	@Override
    	public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
    		FunctionDOS function = (FunctionDOS) arguments.at(0);
    		Context context = (Context) arguments.at(1);
    		return new FunctionDOS.ContextualFunctionDOS(function, context);
    	}
    }

}
