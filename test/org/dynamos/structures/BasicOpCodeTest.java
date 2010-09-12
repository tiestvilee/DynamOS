/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.dynamos.OpCodeInterpreter;
import org.dynamos.types.ValueObject;
import org.junit.Before;
import org.junit.Test;


/**
 *
 * @author tiestvilee
 */
public class BasicOpCodeTest {

    FunctionDOS function;
    ObjectDOS context;
    ObjectDOS object;
    OpCodeInterpreter interpreter;
	Symbol local;
	StackFrame stackFrame;

    @Before
    public void setup() {
    	interpreter = new OpCodeInterpreter();
    	context = new ObjectDOS();
    	local = Symbol.get("local");
    	stackFrame = new StackFrame();
    }

    @Test
    public void shouldCreateValueObject() {
    	boolean newStackFrame = new OpCode.CreateValueObject(345).execute(interpreter, context, stackFrame);
    	
    	assertThat(newStackFrame, is(false));
    	assertThat(((ValueObject) context.getSlot(Symbol.RESULT)).getValue(), is(345));
    }

    @Test
    public void shouldSetObjectOnStackFrame() {
    	ObjectDOS expectedObject = new ObjectDOS();

    	context.setSlot(local, expectedObject);
    	
    	boolean newStackFrame = new OpCode.SetObject(local).execute(interpreter, context, stackFrame);

    	assertThat(newStackFrame, is(false));
    	assertThat(stackFrame.getObject(), is(expectedObject));
    }

    @Test
    public void shouldPushObjectOntoStackFrame() {
    	ObjectDOS expectedObject = new ObjectDOS();

    	context.setSlot(local, expectedObject);
    	
    	boolean newStackFrame = new OpCode.Push(local).execute(interpreter, context, stackFrame);

    	assertThat(newStackFrame, is(false));
    	assertThat(stackFrame.getArguments().get(0), is(expectedObject));
    }

    @Test
    public void shouldPushSymbolOntoStackFrame() {
    	
    	boolean newStackFrame = new OpCode.PushSymbol(local).execute(interpreter, context, stackFrame);

    	assertThat(newStackFrame, is(false));
    	assertThat(((SymbolWrapper) stackFrame.getArguments().get(0)).getSymbol(), is(local));
    }
    
    @Test
    public void shouldDoNothingWhenStartingOpcodeList() {
    	boolean newStackFrame = new OpCode.StartOpCodeList().execute(null, null, null);
    	assertThat(newStackFrame, is(false));
    }

    @Test
    public void shouldDoNothingWhenEndingOpcodeList() {
    	boolean newStackFrame = new OpCode.EndOpCodeList().execute(null, null, null);
    	assertThat(newStackFrame, is(false));
    }
}
