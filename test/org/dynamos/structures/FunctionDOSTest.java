/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.dynamos.OpCodeInterpreter;
import org.dynamos.structures.Activation.ActivationBuilder;
import org.dynamos.types.StandardObjects;
import org.junit.Before;
import org.junit.Test;

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
    	when(undefinedFunction.execute( (OpCodeInterpreter) anyObject(), (ObjectDOS) anyObject(), (List<ObjectDOS>) anyObject())).thenReturn(undefined);
    	
    	newActivationBuilder = mock(ActivationBuilder.class);
    	activation = new Activation();
    	when(newActivationBuilder.createActivation((List<ObjectDOS>) anyObject(), (ObjectDOS) anyObject())).thenReturn(activation);
    	
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
        
        ObjectDOS expectedObject = new ObjectDOS();
		activation.setSlot(Symbol.RESULT, expectedObject);

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