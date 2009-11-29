/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.dynamos.OpCodeInterpreter;
import org.junit.Before;
import org.junit.Test;


/**
 *
 * @author tiestvilee
 */
public class FunctionCallOpCodeTest {

    ExecutableDOS aFunction;
	
    Context context;
    ListDOS arguments;
    ObjectDOS object;
    OpCodeInterpreter interpreter;
	Symbol symbol;
	StackFrame stackFrame;

    @Before
    public void setup() {
    	interpreter = new OpCodeInterpreter();
    	context = interpreter.newContext();
    	symbol = Symbol.get("symbol");
    	stackFrame = new StackFrame();
    	aFunction = mock(ExecutableDOS.class);
    	object = new ObjectDOS();
    }

    /* first the easy ones */
    
    /* simple context searches, no objects involved, just treck up the context until you find it */
    @Test
    public void shouldRequestNewStackFrame() {
    	context.setFunction(symbol, aFunction);
    	
    	boolean newStackFrame = new OpCode.FunctionCall(symbol).execute(context, stackFrame);
    	
    	assertThat(newStackFrame, is(true));
    }

    @Test
    public void shouldCallFunctionInCurrentContext() {
    	context.setFunction(symbol, aFunction);
    	
    	new OpCode.FunctionCall(symbol).execute(context, stackFrame);
    	
    	verify(aFunction).execute(context, stackFrame.getArguments());
    }

    /* simple calls into an object.  Just find the function on the object or it's parent */
    @Test
    public void shouldCallFunctionInNestingContext() {
    	Context nestingContext = new Context();
    	nestingContext.setFunction(symbol, aFunction);
    	
    	context.setParent(nestingContext);
    	
    	new OpCode.FunctionCall(symbol).execute(context, stackFrame);
    	
    	verify(aFunction).execute(context, stackFrame.getArguments());
    }

    @Test
    public void shouldCallFunctionOnSpecifiedObject() {
    	object.setFunction(symbol, aFunction);
    	stackFrame.setObject(object);
    	
    	new OpCode.FunctionCall(symbol).execute(context, stackFrame);
    	
    	verify(aFunction).execute(object, stackFrame.getArguments());
    }

    @Test
    public void shouldCallFunctionOnSuperObject() {
    	ObjectDOS superObject = new ObjectDOS();
    	superObject.setFunction(symbol, aFunction);
    	
    	object.setParent(superObject);
    	stackFrame.setObject(object);
    	
    	new OpCode.FunctionCall(symbol).execute(context, stackFrame);
    	
    	verify(aFunction).execute(object, stackFrame.getArguments());
    }
    
    /* now the harder ones... */

    /* these first few tests are executed as if doing stuff within the actual 
     * object context, as opposed to within a function within the object context.
     * Essentially these are constructor calls.
     */
    /* Inside an object context, making a call to a nesting context function */
    @Test
    public void shouldCallFunctionInNestingContextOfObjectContext() {
    	context.setFunction(symbol, aFunction);
    	object.setContext(context);
    	
    	new OpCode.FunctionCall(symbol).execute(object, stackFrame);
    	
    	verify(aFunction).execute(object, stackFrame.getArguments());
    }
    
    /* Inside an object context, making a call to a super function */
    @Test
    public void shouldCallFunctionInSuperObjectOfObjectContext() {
    }

    /* For good luck; inside an object context, making a call to a nesting object context function */
    @Test
    public void shouldCallFunctionInNestingObjectOfObjectContext() {
    }
    
    /* These are inside functions that are defined within an object context,
     * essentially these are instance functions
     */
    /* Inside a function context making a call to nesting object context
     * ie this is an instance function calling another instance function 
     */

    /* Inside a function context making a call to nesting object context's super
     * ie this is an instance function calling super function 
     */

    /* Inside a function context making a call to nesting object's nesting object
     * ie this is an instance function calling a function on the current classes containing object 
     */
    
    /* TODO precedence tests
     * TODO doesn't do comb tests
     */
}