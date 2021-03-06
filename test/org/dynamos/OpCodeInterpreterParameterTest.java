/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.dynamos.structures.Activation;
import org.dynamos.structures.FunctionDOS;
import org.dynamos.structures.ObjectDOS;
import org.dynamos.structures.OpCode;
import org.dynamos.structures.Symbol;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author tiestvilee
 */
public class OpCodeInterpreterParameterTest {

    OpCodeInterpreter interpreter;
    Activation context;

    Symbol functionName = Symbol.get("functionName");

    Symbol localArgumentName = Symbol.get("argument");
    Symbol localObjectName = Symbol.get("object");
	private ObjectDOS argument1;
	private ObjectDOS argument2;
	private ObjectDOS argument3;
	private Symbol argument1Symbol = Symbol.get("argument1");
	private Symbol argument2Symbol = Symbol.get("argument2");
	private Symbol zeroSymbol = Symbol.get("zero");
	private Symbol oneSymbol = Symbol.get("one");
	private Symbol local1Symbol = Symbol.get("local1");
	private Symbol local2Symbol = Symbol.get("local2");
	private Symbol local3Symbol = Symbol.get("local3");
	private Symbol functionName2 = Symbol.get("functionName2");
	
	private ObjectDOS emptyObject;
	private Symbol emptyObjectSymbol = Symbol.get("emptyObject");

    @Before
    public void setUp() {
        interpreter = new OpCodeInterpreter();
		
		argument1 = interpreter.getEnvironment().createNewObject();
		argument2 = interpreter.getEnvironment().createNewObject();
		argument3 = interpreter.getEnvironment().createNewObject();
        
		emptyObject = interpreter.getEnvironment().createNewObject();
//		emptyObject.setSlot(zeroSymbol, NumberDOS.numberDOS(interpreter, 0));
//		emptyObject.setSlot(oneSymbol, NumberDOS.numberDOS(interpreter, 1));
		
        context = interpreter.newActivation();
		context.setSlot(local1Symbol, argument1);
		context.setSlot(local2Symbol, argument2);
		context.setSlot(local3Symbol, argument3);
		context.setSlot(emptyObjectSymbol, emptyObject);
		context.setVictim(interpreter.getEnvironment().createNewObject());
		
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
			new OpCode.PushSymbol(argumentSymbol),
        	new OpCode.FunctionCall(Symbol.GET_SLOT_$)
        };
		Symbol[] argumentSymbols = new Symbol[] {argument1Symbol, argument2Symbol};
		
		FunctionDOS receiverFunction = new FunctionDOS(argumentSymbols, receiverOpCodes);
		emptyObject.setFunction(functionName2, receiverFunction);
		
		OpCode[] callerOpCodes = new OpCode[] {
        	new OpCode.Push(local1Symbol),
        	new OpCode.Push(local2Symbol),
        	new OpCode.SetObject(emptyObjectSymbol),
        	new OpCode.FunctionCall(functionName2)
        };
		
        interpreter.interpret(context, callerOpCodes);

		assertThat( context.getSlot(Symbol.RESULT), is(agumentValue));
	}
	
	@Test
	public void shouldReturnUndefinedIfArgumentNotProvided() {
		OpCode[] receiverOpCodes = new OpCode[] {
			new OpCode.PushSymbol(argument2Symbol),
        	new OpCode.FunctionCall(Symbol.GET_SLOT_$)
        };
		Symbol[] argumentSymbols = new Symbol[] {argument1Symbol, argument2Symbol};
		
		setUpReceiverFunctionWith(argumentSymbols, receiverOpCodes);

		
		OpCode[] callerOpCodes = new OpCode[] {
        	new OpCode.Push(local1Symbol),
        	new OpCode.SetObject(emptyObjectSymbol),
        	new OpCode.FunctionCall(functionName2)
        };
		
        interpreter.interpret(context, callerOpCodes);

		assertThat( context.getSlot(Symbol.RESULT), is(interpreter.getEnvironment().getUndefined()));
	}
	
	@Test
	public void shouldIgnoreExtraArguments() {
		OpCode[] receiverOpCodes = new OpCode[] {
			new OpCode.PushSymbol(argument2Symbol),
        	new OpCode.FunctionCall(Symbol.GET_SLOT_$)
        };
		Symbol[] argumentSymbols = new Symbol[] {argument1Symbol, argument2Symbol};
		
		setUpReceiverFunctionWith(argumentSymbols, receiverOpCodes);


		OpCode[] callerOpCodes = new OpCode[] {
        	new OpCode.Push(local1Symbol),
        	new OpCode.Push(local2Symbol),
        	new OpCode.Push(local3Symbol),
        	new OpCode.SetObject(emptyObjectSymbol),
        	new OpCode.FunctionCall(functionName2)
        };

        interpreter.interpret(context, callerOpCodes);

		assertThat( context.getSlot(Symbol.RESULT), is(argument2));
	}
    
	private void setUpReceiverFunctionWith(Symbol[] argumentSymbols, OpCode[] receiverOpCodes) {
		FunctionDOS receiverFunction = new FunctionDOS(argumentSymbols, receiverOpCodes);
		emptyObject.setFunction(functionName2, receiverFunction);
	}

    @Test
    public void shouldHaveArgumentsListAvailable() {
		OpCode[] receiverOpCodes = new OpCode[] {
         	new OpCode.Push(zeroSymbol),
         	new OpCode.SetObject(Symbol.ARGUMENTS),
        	new OpCode.FunctionCall(Symbol.get("at:")),
        };
		Symbol[] argumentSymbols = new Symbol[] {argument1Symbol, argument2Symbol};
		
		setUpReceiverFunctionWith(argumentSymbols, receiverOpCodes);


		OpCode[] callerOpCodes = new OpCode[] {
        	new OpCode.Push(local1Symbol),
        	new OpCode.Push(local2Symbol),
        	new OpCode.Push(local3Symbol),
        	new OpCode.SetObject(emptyObjectSymbol),
        	new OpCode.FunctionCall(functionName2)
        };

        interpreter.interpret(context, callerOpCodes);

		assertThat( context.getSlot(Symbol.RESULT), is(argument1));
	}

}