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

import org.dynamos.Environment;
import org.dynamos.OpCodeInterpreter;
import org.dynamos.types.StandardObjects;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author tiestvilee
 */
public class FunctionDOSTest {

    FunctionDOS function;
    Activation context;
    List<ObjectDOS> arguments;
    ObjectDOS object;
    ObjectDOS emptyList;
    ObjectDOS undefined = new StandardObjects.UndefinedDOS();
    ObjectDOS nullObject = new StandardObjects.NullDOS();
    OpCodeInterpreter interpreter;
    Environment environment;

    @Before
    public void setup() {
    	interpreter = mock(OpCodeInterpreter.class);
    	environment = mock(Environment.class);
    	
    	emptyList = new ObjectDOS();
    	emptyList.setSlot(Symbol.get("head"), undefined);
    	emptyList.setSlot(Symbol.get("tail"), undefined);
    	
    	object = new ObjectDOS();
    	when(interpreter.newActivation()).thenReturn(new Activation(emptyList));
    	when(interpreter.getEnvironment()).thenReturn(environment);
    	when(environment.getNull()).thenReturn(nullObject);
    	when(environment.getUndefined()).thenReturn(undefined);
    	when(environment.getEmptyList()).thenReturn(emptyList);
    	when(environment.createNewObject()).thenReturn(new ObjectDOS());
    	
    	context = new Activation(emptyList);
        function = new FunctionDOS(new Symbol[] {}, new OpCode[] {});
        
        arguments = new ArrayList<ObjectDOS>();
        
        StandardObjects.setEnvironment(environment);
    }

    @Test
    public void shouldCallFunctionWithArgumentsAndObject() {
        FunctionWithContext contextualFunction = new FunctionWithContext(function, context);

        contextualFunction.execute(interpreter, object, arguments);

        verify(interpreter).interpret(argThat(matchesContextWithValues(arguments, object, context)), (OpCode[]) anyObject());
    }

    @Test
    public void shouldInterpretOpcodes() {
        OpCode[] opCodes = new OpCode[] {};
        
        FunctionDOS actualFunction = new FunctionDOS(new Symbol[] {}, opCodes);

        actualFunction.execute(interpreter, arguments, context);

        verify(interpreter).interpret(context, opCodes);
    }
    
    @Test
    public void shouldPopulateProvidedArguments() {
    	ObjectDOS value = new ObjectDOS();
		arguments.add(value);
		context.setArguments(arguments);
		
    	Symbol argument = Symbol.get("argument");
        FunctionDOS actualFunction = new FunctionDOS(new Symbol[] {argument}, null);
		
        actualFunction.execute(interpreter, arguments, context);
		
        assertThat(context.getSlot(argument), is(value));
        assertThat(actualArguments().get(0), is(value));
    }

    @Test
    public void shouldUndefineMissingArguments() {
		context.setArguments(arguments);
		
    	Symbol argument = Symbol.get("argument");
        FunctionDOS actualFunction = new FunctionDOS(new Symbol[] {argument}, null);
		
        actualFunction.execute(interpreter, arguments, context);
		
        assertThat(context.getSlot(argument), is(undefined));
        assertThat(actualArguments().size(), is(0));
    }

    @Test
    public void shouldKeepSurplusArguments() {
    	ObjectDOS value = new ObjectDOS();
		arguments.add(value);
		context.setArguments(arguments);
		
        FunctionDOS actualFunction = new FunctionDOS(new Symbol[] {}, null);
		
        actualFunction.execute(interpreter, arguments, context);
		
        assertThat(actualArguments().get(0), is(value));
    }

	private List<ObjectDOS> actualArguments(){
		return StandardObjects.toJavaList(context.getSlot(Symbol.ARGUMENTS));
	}

    private MatchesContextWithValues matchesContextWithValues(final List<ObjectDOS> expectedArguments, ObjectDOS expectedObject, final ObjectDOS functionContext) {
        return new MatchesContextWithValues(expectedArguments, expectedObject, functionContext);
    }

    private class MatchesContextWithValues extends BaseMatcher<Activation> {

        private final List<ObjectDOS> expectedArguments;
        private final ObjectDOS expectedObject;
        private final ObjectDOS functionContext;

        public MatchesContextWithValues(List<ObjectDOS> arguments, ObjectDOS object, ObjectDOS functionContext) {
            this.expectedArguments = arguments;
            this.expectedObject = object;
            this.functionContext = functionContext;
        }
        String message = "";

        public boolean matches(Object item) {
            Activation matchingContext = (Activation) item;
            List<ObjectDOS> actualArguments = StandardObjects.toJavaList(matchingContext.getSlot(Symbol.ARGUMENTS));
			if (!actualArguments.equals(expectedArguments)) {
                message += "arguments doesn't match [" + actualArguments + "] <> [" + expectedArguments + "]\n";
                return false;
            }
            if (matchingContext.getVictim() != expectedObject) {
                message += "object doesn't match [" + matchingContext.getVictim() + "] <> [" + expectedObject + "]\n";
                return false;
            }
            if (matchingContext.getContext() != functionContext) {
                message += "context doesn't match\n";
                return false;
            }
            return true;
        }

        public void describeTo(Description description) {
            description.appendText(message);
        }
    }

}