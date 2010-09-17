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
import static org.mockito.Mockito.doAnswer;

import java.util.ArrayList;
import java.util.List;

import org.dynamos.OpCodeInterpreter;
import org.dynamos.structures.Activation.ActivationBuilder;
import org.dynamos.types.StandardObjects;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 *
 * @author tiestvilee
 */
public class FunctionDOSTest {

    List<ObjectDOS> arguments;
    ObjectDOS object;
    OpCodeInterpreter interpreter;
	private ObjectDOS functionPrototype;
	
	ObjectDOS undefined = new StandardObjects.UndefinedDOS();
	private ExecutableDOS undefinedFunction;
	
	private Activation activation;
	private ActivationBuilder newActivationBuilder;

    @Before
    public void setup() {
    	interpreter = mock(OpCodeInterpreter.class);
    	
    	object = new ObjectDOS();
    	
    	undefinedFunction = mock(ExecutableDOS.class);
    	List<ObjectDOS> anyList = anyObject();
    	when(undefinedFunction.execute( (OpCodeInterpreter) anyObject(), (ObjectDOS) anyObject(), anyList)).thenReturn(undefined);
    	
    	newActivationBuilder = mock(ActivationBuilder.class);
    	activation = new Activation();

    	anyList = anyObject();
		when(newActivationBuilder.createActivation(anyList, (ObjectDOS) anyObject())).thenReturn(activation);
    	
    	functionPrototype = new ObjectDOS();
    	functionPrototype.setFunction(Symbol.UNDEFINED, undefinedFunction);
    	functionPrototype.setFunction(Symbol.get("newActivationWithArguments:andVictim:"), newActivationBuilder);
        
        arguments = new ArrayList<ObjectDOS>();
    }

    @Test
    public void shouldCallContextualFunctionWithArgumentsAndObject() {
        Activation context = new Activation();
    	FunctionDOS function = new FunctionDOS(new Symbol[] {}, new OpCode[] {});
    	function.setParent(functionPrototype);
        
        FunctionWithContext contextualFunction = new FunctionWithContext(function, context);

        contextualFunction.execute(interpreter, object, arguments);

        verify(interpreter).interpret(activation, new OpCode[] {});
        assertThat(activation.getContext(), is(context));
    }

    @Test
    public void shouldCallFunctionWithArgumentsAndObjectAndKeepSurplusArguments() {
        FunctionDOS actualFunction = new FunctionDOS(new Symbol[] {}, new OpCode[] {});
        actualFunction.setParent(functionPrototype);

        actualFunction.execute(interpreter, object, arguments);

        verify(interpreter).interpret(activation, new OpCode[] {});
        verify(newActivationBuilder).createActivation(arguments, object);
    }

    @Test
    public void shouldInterpretOpcodes() {
        OpCode[] opCodes = new OpCode[] {};
        
        FunctionDOS actualFunction = new FunctionDOS(new Symbol[] {}, opCodes);
        actualFunction.setParent(functionPrototype);

        actualFunction.execute(interpreter, object, arguments);

        verify(interpreter).interpret(activation, opCodes);
    }

    @Test
    public void shouldReturnObjectByDefult() {
        FunctionDOS actualFunction = new FunctionDOS(new Symbol[] {}, new OpCode[] {});
        actualFunction.setParent(functionPrototype);
        
        ObjectDOS actualObject = actualFunction.execute(interpreter, object, arguments);

        assertThat(actualObject, is(object));
    }
    
    @Test
    public void shouldReturnResult() {
        FunctionDOS actualFunction = new FunctionDOS(new Symbol[] {}, new OpCode[] {});
        actualFunction.setParent(functionPrototype);
        
        final ObjectDOS expectedObject = new ObjectDOS();
        doAnswer(new Answer<OpCodeInterpreter>() {
            public OpCodeInterpreter answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                ((Activation) args[0]).setSlot(Symbol.RESULT, expectedObject);
                System.out.println("what the?");
                return null;
            }})
        .when(interpreter).interpret( (Activation) anyObject(), (OpCode[]) anyObject());

        ObjectDOS actualObject = actualFunction.execute(interpreter, object, arguments);

        assertThat(actualObject, is(expectedObject));
    }

    @Test
    public void shouldPopulateProvidedArguments() {
    	ObjectDOS value = new ObjectDOS();
		arguments.add(value);
		
    	Symbol argument = Symbol.get("argument");
        FunctionDOS actualFunction = new FunctionDOS(new Symbol[] {argument}, null);
        actualFunction.setParent(functionPrototype);
        
        actualFunction.execute(interpreter, object, arguments);
		
        assertThat(activation.getSlot(argument), is(value));
    }

    @Test
    public void shouldUndefineMissingArguments() {
		
    	Symbol argument = Symbol.get("argument");
        FunctionDOS actualFunction = new FunctionDOS(new Symbol[] {argument}, null);
        actualFunction.setParent(functionPrototype);
        
        actualFunction.execute(interpreter, object, arguments);
		
        assertThat(activation.getSlot(argument), is(undefined));
    }

}