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
	
    ObjectDOS context;
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
    	ObjectDOS object = mock(ObjectDOS.class);
    	when(object.getFunction(symbol)).thenReturn(aFunction);
    	
    	new OpCode.FunctionCall(symbol).execute(object, stackFrame);
    }
    
    @Test
    public void shouldGetFunctionFromObject() {
    	ObjectDOS object = mock(ObjectDOS.class);
    	when(object.getFunction(symbol)).thenReturn(aFunction);
    	stackFrame.setObject(object);
    	
    	new OpCode.FunctionCall(symbol).execute(context, stackFrame);
    }
    
    @Test
    public void shouldExecuteFunction() {
    	ObjectDOS context = mock(Activation.class);
    	when(context.getFunction(symbol)).thenReturn(aFunction);

    	new OpCode.FunctionCall(symbol).execute(context, stackFrame);
    	
    	verify(aFunction).execute(context, stackFrame.arguments);
    }
    
    @Test
    public void shouldReturnResult() {
    	ObjectDOS context = interpreter.newActivation();
    	context.setFunction(symbol, aFunction);

    	when(aFunction.execute((ObjectDOS)anyObject(), (ListDOS)anyObject())).thenReturn(object);

    	boolean shouldContinue = new OpCode.FunctionCall(symbol).execute(context, stackFrame);
    	
    	assertThat(shouldContinue, is(true));
    	assertThat(context.getSlot(Symbol.RESULT), is(object));
    }
}