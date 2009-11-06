/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import org.dynamos.OpCodeInterpreter;


/**
 *
 * @author tiestvilee
 */
public class Context extends ObjectDOS {
	
	public static ContextBuilder initializeContext(OpCodeInterpreter interpreter, ObjectDOS virtualMachine) {
		return new ContextBuilder(interpreter, virtualMachine);
	}
	
	public static class ContextBuilder {

		private final ObjectDOS contextPrototype;

        private final Symbol functionDefinition = Symbol.get("functionDefinition");
        private final Symbol context = Symbol.get("context");
        
		protected ContextBuilder(OpCodeInterpreter interpreter, ObjectDOS virtualMachine) {
			contextPrototype = new ObjectDOS();
			contextPrototype.setSlot(Symbol.RESULT, StandardObjects.UNDEFINED);
			
	        Context contextContainingVM = new Context();
	        contextContainingVM.setSlot(VMObjectDOS.VM, virtualMachine);
	        
	        contextPrototype.setFunction(Symbol.CONTEXTUALIZE_FUNCTION, new FunctionDOS(new FunctionDefinitionDOS(
	        		interpreter,
	        		new Symbol[] {functionDefinition, context},
	        		new Symbol[] {},
	        		new OpCode[] {
	        			new OpCode.Push(functionDefinition),
	        			new OpCode.Push(context),
	        			new OpCode.SetObject(VMObjectDOS.VM),
	        			new OpCode.MethodCall(VMObjectDOS.CONTEXTUALIZE_FUNCTION)
	        		}), 
	        		contextContainingVM));
		}
		
		public Context createContext() {
			Context result = new Context();
			result.setParent(contextPrototype);
			return result;
		}
		
	}

    ListDOS arguments = new ListDOS();
    ObjectDOS object;
    
    public Context() {
        super();
        setSlot(Symbol.ARGUMENTS, arguments);
        setSlot(Symbol.CURRENT_CONTEXT, this);
    }
    
    // TODO can these be just slots?
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

}
