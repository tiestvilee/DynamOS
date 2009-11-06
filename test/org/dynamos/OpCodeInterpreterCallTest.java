/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.dynamos.structures.Context;
import org.dynamos.structures.FunctionDOS;
import org.dynamos.structures.ListDOS;
import org.dynamos.structures.ObjectDOS;
import org.dynamos.structures.OpCode;
import org.dynamos.structures.Symbol;
import org.dynamos.structures.VMObjectDOS;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author tiestvilee
 */
public class OpCodeInterpreterCallTest {

    OpCodeInterpreter interpreter;
    Context context;
    ObjectDOS theObject;
    FunctionDOS.ContextualFunctionDOS function;

    Symbol functionName = Symbol.get("functionName");

    Symbol localArgumentName = Symbol.get("argument");
    Symbol localObjectName = Symbol.get("object");
    
    ListDOS expectedArgumentList;

    @Before
    public void setUp() {
        interpreter = new OpCodeInterpreter();
        context = new Context();
        theObject = new ObjectDOS();
        function = mock(FunctionDOS.ContextualFunctionDOS.class);
        expectedArgumentList = new ListDOS();
    }

    /* When a context call is made, it is essentially a method call but with the context object */
    @Test
    public void shouldCallAMethodInContext() {
        context.setFunction(functionName, function);

        OpCode[] opCodes = new OpCode[] {
            new OpCode.ContextCall(functionName)
        };

        interpreter.interpret(context, opCodes);

        verify(function).execute(argThat(is(context)), argThat(matchArgumentListTo(expectedArgumentList)));
    }

    @Test
    public void shouldCallAMethodInContextWithParameter() {
        context.setFunction(functionName, function);
        context.setSlot(localArgumentName, theObject);

        OpCode[] opCodes = new OpCode[] {
            new OpCode.Push(localArgumentName),
            new OpCode.ContextCall(functionName)
        };

        interpreter.interpret(context, opCodes);

        expectedArgumentList.add(theObject);
        verify(function).execute(argThat(is(context)), argThat(matchArgumentListTo(expectedArgumentList)));
    }

    @Test
    public void shouldCallAMethodOnAnObject() {
        theObject.setFunction(functionName, function);
        context.setSlot(localObjectName, theObject);

        OpCode[] opCodes = new OpCode[] {
            new OpCode.SetObject(localObjectName),
            new OpCode.MethodCall(functionName)
        };

        interpreter.interpret(context, opCodes);

        verify(function).execute(argThat(is(theObject)), argThat(matchArgumentListTo(expectedArgumentList)));
    }

    @Test
    public void shouldCallAMethodOnObjectWithParameter() {
        final ObjectDOS expectedArgument = new ObjectDOS();
        theObject.setFunction(functionName, function);
        context.setSlot(localObjectName, theObject);
        context.setSlot(localArgumentName, expectedArgument);

        OpCode[] opCodes = new OpCode[] {
            new OpCode.Push(localArgumentName),
            new OpCode.SetObject(localObjectName),
            new OpCode.MethodCall(functionName)
        };

        interpreter.interpret(context, opCodes);

        expectedArgumentList.add(expectedArgument);
        verify(function).execute(argThat(is(theObject)), argThat(matchArgumentListTo(expectedArgumentList)));
    }

    @Test
    public void shouldCallAMethodOnThis() {
        theObject.setFunction(functionName, function);
        context.setObject(theObject);

        OpCode[] opCodes = new OpCode[] {
            new OpCode.SetObject(Symbol.THIS),
            new OpCode.MethodCall(functionName)
        };

        interpreter.interpret(context, opCodes);
        
        verify(function).execute(argThat(is(theObject)), argThat(matchArgumentListTo(expectedArgumentList)));
    }

    private Matcher<ListDOS> matchArgumentListTo(final ListDOS expected) {
		return new BaseMatcher<ListDOS>() {

			public boolean matches(Object object) {
				ListDOS actual = (ListDOS) object;
				return actual.getRawList().equals(expected.getRawList());
			}

			public void describeTo(Description description) {
				// TODO Auto-generated method stub
				
			}
			
		};
	}

//    @Test
//    public void shouldThrowExceptionIfMethodDoesntExist() {
//        ObjectDOS context = new ObjectDOS();
//        ObjectDOS theObject = new ObjectDOS();
//        OpCode[] opCodes = new OpCode[] {};
//
//        interpreter.interpret(context, theObject, opCodes);
//    }


    @Test
    public void shouldCallThroughToVM() {
        Symbol vm = Symbol.get("VM");
        context.setSlot(vm, VMObjectDOS.VM);
        context.setSlot(localArgumentName, theObject);

        OpCode[] opCodes = new OpCode[] {
            new OpCode.Push(localArgumentName),
            new OpCode.SetObject(vm),
            new OpCode.MethodCall(Symbol.get("print"))
        };

        interpreter.interpret(context, opCodes);
    }

}