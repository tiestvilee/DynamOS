/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import org.dynamos.OpCodeInterpreter;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author tiestvilee
 */
public class FunctionDOSTest {

    FunctionDefinitionDOS function;
    Context context;
    ListDOS arguments;
    ObjectDOS object;
    OpCodeInterpreter interpreter;

    @Before
    public void setup() {
    	interpreter = mock(OpCodeInterpreter.class);
    	context = new Context();
    	
        function = mock(FunctionDefinitionDOS.class);
        when(function.newContext()).thenReturn(new Context());
        
        arguments = new ListDOS();
        object = new ObjectDOS();
    }

    @Test
    public void shouldCallFunctionWithArgumentsAndObject() {
        FunctionDOS contextualFunction = new FunctionDOS(function, context);

        contextualFunction.execute(object, arguments);

        verify(function).execute(argThat(matchesContextWithValues(arguments, object, context)));
    }

    @Test
    public void shouldInterpretOpcodes() {
        OpCode[] opCodes = new OpCode[] {};
        
        FunctionDefinitionDOS actualFunction = new FunctionDefinitionDOS(interpreter, new Symbol[] {}, new Symbol[] {}, opCodes);

        actualFunction.execute(context);

        verify(interpreter).interpret(context, opCodes);
    }
    
    @Test
    public void shouldCreateLocals() {
    	Symbol local = Symbol.get("local");
        FunctionDefinitionDOS actualFunction = new FunctionDefinitionDOS(interpreter, new Symbol[] {}, new Symbol[] {local}, null);
		
        actualFunction.execute(context);
		
        assertThat(context.getSlot(local), is(StandardObjects.NULL));
    }
    
    @Test
    public void shouldPopulateProvidedArguments() {
    	ObjectDOS value = new ObjectDOS();
		arguments.add(value);
		context.setArguments(arguments);
		
    	Symbol argument = Symbol.get("argument");
        FunctionDefinitionDOS actualFunction = new FunctionDefinitionDOS(interpreter, new Symbol[] {argument}, new Symbol[] {}, null);
		
        actualFunction.execute(context);
		
        assertThat(context.getSlot(argument), is(value));
        assertThat(((ListDOS) context.getSlot(Symbol.ARGUMENTS)).at(0), is(value));
    }
    
    @Test
    public void shouldUndefineMissingArguments() {
		context.setArguments(arguments);
		
    	Symbol argument = Symbol.get("argument");
        FunctionDefinitionDOS actualFunction = new FunctionDefinitionDOS(interpreter, new Symbol[] {argument}, new Symbol[] {}, null);
		
        actualFunction.execute(context);
		
        assertThat(context.getSlot(argument), is(StandardObjects.UNDEFINED.getClass()));
        assertThat(((ListDOS) context.getSlot(Symbol.ARGUMENTS)).size(), is(0));
    }
    
    @Test
    public void shouldKeepSurplusArguments() {
    	ObjectDOS value = new ObjectDOS();
		arguments.add(value);
		context.setArguments(arguments);
		
        FunctionDefinitionDOS actualFunction = new FunctionDefinitionDOS(interpreter, new Symbol[] {}, new Symbol[] {}, null);
		
        actualFunction.execute(context);
		
        assertThat(((ListDOS) context.getSlot(Symbol.ARGUMENTS)).at(0), is(value));
    }

    private MatchesContextWithValues matchesContextWithValues(final ListDOS arguments, ObjectDOS object, final Context functionContext) {
        return new MatchesContextWithValues(arguments, object, functionContext);
    }

    private class MatchesContextWithValues extends BaseMatcher<Context> {

        private final ListDOS arguments;
        private final ObjectDOS object;
        private final Context functionContext;

        public MatchesContextWithValues(ListDOS arguments, ObjectDOS object, Context functionContext) {
            this.arguments = arguments;
            this.object = object;
            this.functionContext = functionContext;
        }
        String message = "";

        public boolean matches(Object item) {
            Context context = (Context) item;
            if (context.getArguments() != arguments) {
                message += "arguments doesn't match [" + context.getArguments() + "] <> [" + arguments + "]\n";
                return false;
            }
            if (context.getObject() != object) {
                message += "object doesn't match [" + context.getObject() + "] <> [" + object + "]\n";
                return false;
            }
            if (context.getParent() != functionContext) {
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