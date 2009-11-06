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
	
	public static ContextBuilder initializeContext(OpCodeInterpreter interpreter) {
		return new ContextBuilder(interpreter);
	}
	
	public static class ContextBuilder {

		private final OpCodeInterpreter interpreter;

		protected ContextBuilder(OpCodeInterpreter interpreter) {
			this.interpreter = interpreter;
		}
		
		public Context createContext() {
			return new Context(interpreter);
		}
		
	}

    ListDOS arguments = new ListDOS();
    ObjectDOS object;
    
    public Context(OpCodeInterpreter interpreter) {
        super();
        setSlot(Symbol.RESULT, StandardObjects.UNDEFINED);
        setSlot(Symbol.ARGUMENTS, arguments);
        setSlot(Symbol.CURRENT_CONTEXT, this);
        
        Symbol functionDefinition = Symbol.get("functionDefinition");
        Symbol context = Symbol.get("context");
        
        setFunction(Symbol.CONTEXTUALIZE_FUNCTION, new FunctionDOS(new FunctionDefinitionDOS(
        		interpreter,
        		new Symbol[] {functionDefinition, context},
        		new Symbol[] {},
        		new OpCode[] {
        			new OpCode.Push(functionDefinition),
        			new OpCode.Push(context),
        			new OpCode.SetObject(VMObjectDOS.VM),
        			new OpCode.MethodCall(VMObjectDOS.CONTEXTUALIZE_FUNCTION)
        		}), this));
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

}
