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

import java.util.List;

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
	
    ObjectDOS context;
    ObjectDOS object;
    OpCodeInterpreter interpreter;
	Symbol symbol;
	StackFrame stackFrame;
	Environment environment;

    @Before
    public void setup() {
    	interpreter = new OpCodeInterpreter();
    	symbol = Symbol.get("symbol");
    	stackFrame = new StackFrame();
    	aFunction = mock(ExecutableDOS.class);
    	object = new ObjectDOS();
    	context = new ObjectDOS();
    }
    
    @Test
    public void shouldGetFunctionFromContext() {
    	context = mock(ObjectDOS.class);
    	when(context.getFunction(symbol)).thenReturn(aFunction);
    	
    	new OpCode.FunctionCall(symbol).execute(interpreter, context, stackFrame);
    }
    
    @Test
    public void shouldGetFunctionFromSetObject() {
    	object = mock(ObjectDOS.class);
    	when(object.getFunction(symbol)).thenReturn(aFunction);
    	stackFrame.setObject(object);
    	
    	new OpCode.FunctionCall(symbol).execute(interpreter, context, stackFrame);
    }
    
    @Test
    public void shouldExecuteFunctionOnThis() {
    	context = mock(Activation.class);
    	when(context.getFunction(symbol)).thenReturn(aFunction);
    	when(context.getSlot(Symbol.THIS)).thenReturn(object);

    	new OpCode.FunctionCall(symbol).execute(interpreter, context, stackFrame);
    	
    	verify(aFunction).execute(interpreter, object, stackFrame.arguments);
    }
    
    @Test
    public void shouldExecuteFunctionOnSetObject() {
    	object = mock(ObjectDOS.class);
    	when(object.getFunction(symbol)).thenReturn(aFunction);
    	stackFrame.setObject(object);
    	
    	new OpCode.FunctionCall(symbol).execute(interpreter, context, stackFrame);
    	
    	verify(aFunction).execute(interpreter, object, stackFrame.arguments);
    }
    
    @SuppressWarnings("unchecked")
	@Test
    public void shouldReturnResult() {
    	context.setFunction(symbol, aFunction);

    	when(aFunction.execute((OpCodeInterpreter)anyObject(), (ObjectDOS)anyObject(), (List<ObjectDOS>)anyObject())).thenReturn(object);

    	boolean shouldContinue = new OpCode.FunctionCall(symbol).execute(interpreter, context, stackFrame);
    	
    	assertThat(shouldContinue, is(true));
    	assertThat(context.getSlot(Symbol.RESULT), is(object));
    }
}