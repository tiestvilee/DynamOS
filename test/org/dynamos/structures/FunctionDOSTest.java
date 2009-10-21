/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import static org.junit.Assert.assertThat;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.hamcrest.CoreMatchers.*;

/**
 *
 * @author tiestvilee
 */
public class FunctionDOSTest {

    @Test
    public void testShouldCallFunctionWithArguments() {
        FunctionDOS function = mock(FunctionDOS.class);
        final Context functionContext = new Context();
        FunctionDOS.ContextualFunctionDOS contextualFunction = new FunctionDOS.ContextualFunctionDOS(function, functionContext);
        final List<Object> arguments = new ArrayList<Object>();

        contextualFunction.execute(arguments);

        verify(function).execute(argThat(new BaseMatcherImpl(arguments, null, functionContext)));
    }

    private class BaseMatcherImpl extends BaseMatcher<Context> {

        private final List<Object> arguments;
        private final ObjectDOS object;
        private final Context functionContext;

        public BaseMatcherImpl(List<Object> arguments, ObjectDOS object, Context functionContext) {
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
                message += "object doesn't match\n";
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