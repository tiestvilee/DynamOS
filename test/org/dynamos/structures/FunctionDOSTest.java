/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

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

    FunctionDOS function;
    Context functionContext;
    List<ObjectDOS> arguments;
    ObjectDOS object;

    @Before
    public void setup() {
        function = mock(FunctionDOS.class);
        functionContext = new Context();
        arguments = new ArrayList<ObjectDOS>();
        object = new ObjectDOS();
    }

    @Test
    public void shouldCallFunctionWithArgumentsAndObject() {
        FunctionDOS.ContextualFunctionDOS contextualFunction = new FunctionDOS.ContextualFunctionDOS(function, functionContext);

        contextualFunction.execute(object, arguments);

        verify(function).execute(argThat(matchesContextWithValues(arguments, object, functionContext)));
    }

    @Test
    public void shouldInterpretOpcodes() {
        Context context = new Context();
        OpCodeInterpreter interpreter = mock(OpCodeInterpreter.class);
        OpCode[] opCodes = new OpCode[] {};
        
        FunctionDOS actualFunction = new FunctionDOS(interpreter, opCodes);

        actualFunction.execute(context);

        verify(interpreter).interpret(context, opCodes);
    }

    private MatchesContextWithValues matchesContextWithValues(final List<ObjectDOS> arguments, ObjectDOS object, final Context functionContext) {
        return new MatchesContextWithValues(arguments, object, functionContext);
    }

    private class MatchesContextWithValues extends BaseMatcher<Context> {

        private final List<ObjectDOS> arguments;
        private final ObjectDOS object;
        private final Context functionContext;

        public MatchesContextWithValues(List<ObjectDOS> arguments, ObjectDOS object, Context functionContext) {
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