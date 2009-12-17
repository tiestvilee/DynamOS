/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.dynamos.OpCodeInterpreter;
import org.dynamos.structures.StandardObjects.ValueObject;
import org.junit.Before;
import org.junit.Test;


/**
 *
 * @author tiestvilee
 */
public class BasicOpCodeTest {

    FunctionDOS function;
    ObjectDOS context;
    ListDOS arguments;
    ObjectDOS object;
    OpCodeInterpreter interpreter;
	Symbol local;
	StackFrame stackFrame;

    @Before
    public void setup() {
    	interpreter = new OpCodeInterpreter();
    	context = interpreter.newActivation();
    	local = Symbol.get("local");
    	stackFrame = new StackFrame();
    	object = interpreter.getEnvironment().createNewObject();
    }

    @Test
    public void shouldCreateValueObject() {
    	boolean newStackFrame = new OpCode.CreateValueObject(interpreter, 345).execute(context, stackFrame);
    	
    	assertThat(newStackFrame, is(false));
    	assertThat(((ValueObject) context.getSlot(Symbol.RESULT)).getValue(), is(345));
    }

    @Test
    public void shouldSetObjectOnStackFrame() {
    	ObjectDOS expectedObject = interpreter.getEnvironment().createNewObject();

    	context.setSlot(local, expectedObject);
    	
    	boolean newStackFrame = new OpCode.SetObject(local).execute(context, stackFrame);

    	assertThat(newStackFrame, is(false));
    	assertThat(stackFrame.getObject(), is(expectedObject));
    }

    @Test
    public void shouldPushObjectOntoStackFrame() {
    	ObjectDOS expectedObject = interpreter.getEnvironment().createNewObject();

    	context.setSlot(local, expectedObject);
    	
    	boolean newStackFrame = new OpCode.Push(local).execute(context, stackFrame);

    	assertThat(newStackFrame, is(false));
    	assertThat(stackFrame.getArguments().getRawList().get(0), is(expectedObject));
    }

    @Test
    public void shouldPushSymbolOntoStackFrame() {
    	
    	boolean newStackFrame = new OpCode.PushSymbol(local).execute(context, stackFrame);

    	assertThat(newStackFrame, is(false));
    	assertThat(((SymbolWrapper) stackFrame.getArguments().getRawList().get(0)).getSymbol(), is(local));
    }
    
    @Test
    public void shouldDoNothingWhenStartingOpcodeList() {
    	boolean newStackFrame = new OpCode.StartOpCodeList().execute(null, null);
    	assertThat(newStackFrame, is(false));
    }

    @Test
    public void shouldDoNothingWhenEndingOpcodeList() {
    	boolean newStackFrame = new OpCode.EndOpCodeList().execute(null, null);
    	assertThat(newStackFrame, is(false));
    }
}
