/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import org.dynamos.Environment;
import org.dynamos.OpCodeInterpreter;


/**
 *
 * @author tiestvilee
 */
public class Context extends ObjectDOS {
	
	public static ContextBuilder initializeContext(OpCodeInterpreter interpreter, Environment environment) {
		return new ContextBuilder(interpreter, environment);
	}
	
	public static class ContextBuilder {

		private final ObjectDOS contextPrototype;

        private final Symbol functionDefinition = Symbol.get("functionDefinition");
        private final Symbol context = Symbol.get("context");
        private final Symbol argumentList = Symbol.get("argumentList");
        private final Symbol opcodes = Symbol.get("opcodes");
        
		protected ContextBuilder(OpCodeInterpreter interpreter, Environment environment) {
			contextPrototype = environment.createNewObject();
			contextPrototype.setSlot(Symbol.RESULT, environment.getUndefined());
			
	        Context contextContainingVM = createContext();
	        contextContainingVM.setSlot(VMObjectDOS.VM, environment.getVirtualMachine());
	        
	        contextPrototype.setFunction(Symbol.CONTEXTUALIZE_FUNCTION_$_IN_$, 
	        		environment.createFunction(
       				new Symbol[] {functionDefinition, context},
	        		new OpCode[] {
	        				new OpCode.Push(functionDefinition),
	        				new OpCode.Push(context),
	        				new OpCode.SetObject(VMObjectDOS.VM),
	        				new OpCode.FunctionCall(VMObjectDOS.CONTEXTUALIZE_FUNCTION_$_IN_$)
	        		},
	        		contextContainingVM));

			contextPrototype.setFunction(Symbol.NEW_OBJECT, 
					environment.createFunction( 
						new Symbol[] {},
						new OpCode[] {
							new OpCode.SetObject(VMObjectDOS.VM),
							new OpCode.FunctionCall(VMObjectDOS.NEW_OBJECT)
						}, 
						contextContainingVM ));
			
			contextPrototype.setFunction(Symbol.CREATE_FUNCTION_WITH_ARGUMENTS_$_OPCODES_$, 
					environment.createFunction( 
						new Symbol[] {argumentList, opcodes}, 
						new OpCode[] {
	        				new OpCode.Push(argumentList),
	        				new OpCode.Push(opcodes),
							new OpCode.SetObject(VMObjectDOS.VM),
							new OpCode.FunctionCall(VMObjectDOS.CREATE_FUNCTION_WITH_ARGUMENTS_$_OPCODES_$)
						}, 
						contextContainingVM ));
			
		}
		
		public Context createContext() {
			Context result = new Context();
			result.setContext(contextPrototype);
			return result;
		}
		
	}

    ListDOS arguments = new ListDOS();
    ObjectDOS object;
    
    public Context() {
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
