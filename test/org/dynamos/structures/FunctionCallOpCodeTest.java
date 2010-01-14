/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.dynamos.Environment;
import org.dynamos.OpCodeInterpreter;
import org.junit.Before;
import org.junit.Test;


/**
 *
 * @author tiestvilee
 */
public class FunctionCallOpCodeTest {

    ExecutableDOS aFunction;
    ExecutableDOS anotherFunction;
	
    Activation context;
    ListDOS arguments;
    ObjectDOS object;
    OpCodeInterpreter interpreter;
	Symbol symbol;
	StackFrame stackFrame;
	Environment environment;

    @Before
    public void setup() {
    	interpreter = new OpCodeInterpreter();
    	environment = interpreter.getEnvironment();
    	context = interpreter.newActivation();
    	symbol = Symbol.get("symbol");
    	stackFrame = new StackFrame();
    	aFunction = mock(ExecutableDOS.class);
    	anotherFunction = mock(ExecutableDOS.class);
    	object = environment.createNewObject();
    }
    
    @Test
    public void shouldGetFunctionFromThis() {
    	object = mock(ObjectDOS.class);
    	when(object.getFunction(symbol)).thenReturn(aFunction);
    	
    	new OpCode.FunctionCall(symbol).execute(interpreter, object, stackFrame);
    }
    
    @Test
    public void shouldGetFunctionFromObject() {
    	object = mock(ObjectDOS.class);
    	when(object.getFunction(symbol)).thenReturn(aFunction);
    	stackFrame.setObject(object);
    	
    	new OpCode.FunctionCall(symbol).execute(interpreter, context, stackFrame);
    }
    
    @Test
    public void shouldExecuteFunction() {
    	context = mock(Activation.class);
    	when(context.getFunction(symbol)).thenReturn(aFunction);

    	new OpCode.FunctionCall(symbol).execute(interpreter, context, stackFrame);
    	
    	verify(aFunction).execute(interpreter, context, stackFrame.arguments);
    }
    
    @Test
    public void shouldReturnResult() {
    	context = interpreter.newActivation();
    	context.setFunction(symbol, aFunction);

    	when(aFunction.execute((OpCodeInterpreter)anyObject(), (ObjectDOS)anyObject(), (ListDOS)anyObject())).thenReturn(object);

    	boolean shouldContinue = new OpCode.FunctionCall(symbol).execute(interpreter, context, stackFrame);
    	
    	assertThat(shouldContinue, is(true));
    	assertThat(context.getSlot(Symbol.RESULT), is(object));
    }
}