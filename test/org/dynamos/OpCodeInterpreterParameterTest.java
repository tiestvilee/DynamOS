/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.dynamos.structures.Context;
import org.dynamos.structures.FunctionDOS;
import org.dynamos.structures.FunctionDefinitionDOS;
import org.dynamos.structures.ObjectDOS;
import org.dynamos.structures.OpCode;
import org.dynamos.structures.StandardObjects;
import org.dynamos.structures.Symbol;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author tiestvilee
 */
public class OpCodeInterpreterParameterTest {

    OpCodeInterpreter interpreter;
    Context context;

    Symbol functionName = Symbol.get("functionName");

    Symbol localArgumentName = Symbol.get("argument");
    Symbol localObjectName = Symbol.get("object");
	private ObjectDOS argument1 = new ObjectDOS();
	private ObjectDOS argument2 = new ObjectDOS();
	private ObjectDOS argument3 = new ObjectDOS();
	private Symbol argument1Symbol = Symbol.get("argument1");
	private Symbol argument2Symbol = Symbol.get("argument2");
	private Symbol zeroSymbol = Symbol.get("zero");
	private Symbol oneSymbol = Symbol.get("one");
	private Symbol local1Symbol = Symbol.get("local1");
	private Symbol local2Symbol = Symbol.get("local2");
	private Symbol local3Symbol = Symbol.get("local3");
	private Symbol functionName2 = Symbol.get("functionName");

    @Before
    public void setUp() {
        interpreter = new OpCodeInterpreter();
        
        context = interpreter.newContext();
		context.setSlot(local1Symbol, argument1);
		context.setSlot(local2Symbol, argument2);
		context.setSlot(local3Symbol, argument3);
    }
    
    @Test
    public void shouldReturnFirstNamedArgument() {
    	
		shouldReturnNamedArgument(argument1Symbol, argument1);
    }
    
    @Test
    public void shouldReturnSecondNamedArgument() {
    	
		shouldReturnNamedArgument(argument2Symbol, argument2);
    }

	private void shouldReturnNamedArgument(Symbol argumentSymbol, ObjectDOS agumentValue) {
		OpCode[] receiverOpCodes = new OpCode[] {
        	new OpCode.ContextCall(argumentSymbol),
         	new OpCode.Push(Symbol.RESULT),
        	new OpCode.ContextCall(Symbol.SET_RESULT)
        };
		Symbol[] argumentSymbols = new Symbol[] {argumentSymbol, argument2Symbol};
		
		setUpReceiverFunctionWith(argumentSymbols, receiverOpCodes);

		
		OpCode[] callerOpCodes = new OpCode[] {
        	new OpCode.Push(local1Symbol),
        	new OpCode.Push(local2Symbol),
        	new OpCode.ContextCall(functionName2)
        };
		
        interpreter.interpret(context, callerOpCodes);

		assertThat( (ObjectDOS) context.getSlot(Symbol.RESULT), is(agumentValue));
	}
	
	@Test
	public void shouldReturnUndefinedIfArgumentNotProvided() {
		OpCode[] receiverOpCodes = new OpCode[] {
        	new OpCode.ContextCall(argument2Symbol),
         	new OpCode.Push(Symbol.RESULT),
        	new OpCode.ContextCall(Symbol.SET_RESULT)
        };
		Symbol[] argumentSymbols = new Symbol[] {argument1Symbol, argument2Symbol};
		
		setUpReceiverFunctionWith(argumentSymbols, receiverOpCodes);

		
		OpCode[] callerOpCodes = new OpCode[] {
        	new OpCode.Push(local1Symbol),
        	new OpCode.ContextCall(functionName2)
        };
		
        interpreter.interpret(context, callerOpCodes);

		assertThat( (StandardObjects.UndefinedDOS) context.getSlot(Symbol.RESULT), is(StandardObjects.UNDEFINED));
	}
	
	@Test
	public void shouldIgnoreExtraArguments() {
		OpCode[] receiverOpCodes = new OpCode[] {
        	new OpCode.ContextCall(argument2Symbol),
         	new OpCode.Push(Symbol.RESULT),
        	new OpCode.ContextCall(Symbol.SET_RESULT)
        };
		Symbol[] argumentSymbols = new Symbol[] {argument1Symbol, argument2Symbol};
		
		setUpReceiverFunctionWith(argumentSymbols, receiverOpCodes);


		OpCode[] callerOpCodes = new OpCode[] {
        	new OpCode.Push(local1Symbol),
        	new OpCode.Push(local2Symbol),
        	new OpCode.Push(local3Symbol),
        	new OpCode.ContextCall(functionName2)
        };

        interpreter.interpret(context, callerOpCodes);

		assertThat( (ObjectDOS) context.getSlot(Symbol.RESULT), is(argument2));
	}
    
	private void setUpReceiverFunctionWith(Symbol[] argumentSymbols, OpCode[] receiverOpCodes) {
		FunctionDefinitionDOS receiverFunction = new FunctionDefinitionDOS(interpreter, argumentSymbols, new Symbol[] {}, receiverOpCodes);
		
		Context emptyContext = interpreter.newContext();
		emptyContext.setSlot(zeroSymbol, StandardObjects.makeValueANumber(interpreter, new StandardObjects.ValueObject(0)));
		emptyContext.setSlot(oneSymbol, StandardObjects.makeValueANumber(interpreter, new StandardObjects.ValueObject(1)));
		
		FunctionDOS contextualReceiverFunction = new FunctionDOS(receiverFunction, emptyContext);
		context.setFunction(functionName2, contextualReceiverFunction);
	}

    @Test
    public void shouldHaveArgumentsListAvailable() {
		OpCode[] receiverOpCodes = new OpCode[] {
        	new OpCode.ContextCall(Symbol.ARGUMENTS),
         	new OpCode.Push(zeroSymbol),
         	new OpCode.SetObject(Symbol.RESULT),
        	new OpCode.MethodCall(Symbol.get("at:")),
         	new OpCode.Push(Symbol.RESULT),
        	new OpCode.ContextCall(Symbol.SET_RESULT)
        };
		Symbol[] argumentSymbols = new Symbol[] {argument1Symbol, argument2Symbol};
		
		setUpReceiverFunctionWith(argumentSymbols, receiverOpCodes);


		OpCode[] callerOpCodes = new OpCode[] {
        	new OpCode.Push(local1Symbol),
        	new OpCode.Push(local2Symbol),
        	new OpCode.Push(local3Symbol),
        	new OpCode.ContextCall(functionName2)
        };

        interpreter.interpret(context, callerOpCodes);

		assertThat( (ObjectDOS) context.getSlot(Symbol.RESULT), is(argument1));
	}

}