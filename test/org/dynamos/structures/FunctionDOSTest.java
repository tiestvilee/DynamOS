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
    ListDOS arguments;
    ObjectDOS object;
    ObjectDOS undefined = new StandardObjects.UndefinedDOS();
    ObjectDOS nullObject = new StandardObjects.NullDOS();
    OpCodeInterpreter interpreter;
    Environment environment;

    @Before
    public void setup() {
    	interpreter = mock(OpCodeInterpreter.class);
    	environment = mock(Environment.class);
    	when(interpreter.newActivation()).thenReturn(new Activation());
    	when(interpreter.getEnvironment()).thenReturn(environment);
    	when(environment.getNull()).thenReturn(nullObject);
    	when(environment.getUndefined()).thenReturn(undefined);
    	
    	context = new Activation();
        function = new FunctionDOS(new Symbol[] {}, new OpCode[] {});
        
        arguments = new ListDOS();
        object = new ObjectDOS();
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

        actualFunction.execute(interpreter, context);

        verify(interpreter).interpret(context, opCodes);
    }
    
    @Test
    public void shouldPopulateProvidedArguments() {
    	ObjectDOS value = new ObjectDOS();
		arguments.add(value);
		context.setArguments(arguments);
		
    	Symbol argument = Symbol.get("argument");
        FunctionDOS actualFunction = new FunctionDOS(new Symbol[] {argument}, null);
		
        actualFunction.execute(interpreter, context);
		
        assertThat(context.getSlot(argument), is(value));
        assertThat(((ListDOS) context.getSlot(Symbol.ARGUMENTS)).at(0), is(value));
    }
    
    @Test
    public void shouldUndefineMissingArguments() {
		context.setArguments(arguments);
		
    	Symbol argument = Symbol.get("argument");
        FunctionDOS actualFunction = new FunctionDOS(new Symbol[] {argument}, null);
		
        actualFunction.execute(interpreter, context);
		
        assertThat(context.getSlot(argument), is(undefined));
        assertThat(((ListDOS) context.getSlot(Symbol.ARGUMENTS)).size(), is(0));
    }
    
    @Test
    public void shouldKeepSurplusArguments() {
    	ObjectDOS value = new ObjectDOS();
		arguments.add(value);
		context.setArguments(arguments);
		
        FunctionDOS actualFunction = new FunctionDOS(new Symbol[] {}, null);
		
        actualFunction.execute(interpreter, context);
		
        assertThat(((ListDOS) context.getSlot(Symbol.ARGUMENTS)).at(0), is(value));
    }

    private MatchesContextWithValues matchesContextWithValues(final ListDOS expectedArguments, ObjectDOS expectedObject, final ObjectDOS functionContext) {
        return new MatchesContextWithValues(expectedArguments, expectedObject, functionContext);
    }

    private class MatchesContextWithValues extends BaseMatcher<Activation> {

        private final ListDOS expectedArguments;
        private final ObjectDOS expectedObject;
        private final ObjectDOS functionContext;

        public MatchesContextWithValues(ListDOS arguments, ObjectDOS object, ObjectDOS functionContext) {
            this.expectedArguments = arguments;
            this.expectedObject = object;
            this.functionContext = functionContext;
        }
        String message = "";

        public boolean matches(Object item) {
            Activation matchingContext = (Activation) item;
            if (matchingContext.getSlot(Symbol.ARGUMENTS) != expectedArguments) {
                message += "arguments doesn't match [" + ((ListDOS) matchingContext.getSlot(Symbol.ARGUMENTS)).list + "] <> [" + expectedArguments.list + "]\n";
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