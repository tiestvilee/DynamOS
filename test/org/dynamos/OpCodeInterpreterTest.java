/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.dynamos.structures.Context;
import org.dynamos.structures.FunctionDOS;
import org.dynamos.structures.ObjectDOS;
import org.dynamos.structures.OpCode;
import org.dynamos.structures.Symbol;
import org.dynamos.structures.VMObjectDOS;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Matchers;
import org.mozilla.classfile.ByteCode;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author tiestvilee
 */
public class OpCodeInterpreterTest {

    OpCodeInterpreter interpreter;
    Context context;
    ObjectDOS theObject;
    Symbol functionName = Symbol.get("functionName");

    Symbol localArgumentName = Symbol.get("argument");
    Symbol localObjectName = Symbol.get("object");

    @Before
    public void setUp() {
        interpreter = new OpCodeInterpreter();
        context = new Context();
        theObject = new ObjectDOS();
    }

    @Test
    public void shouldCallAMethodInContext() {
        FunctionDOS.ContextualFunctionDOS function = mock(FunctionDOS.ContextualFunctionDOS.class);
        context.setSlot(functionName, function);

        OpCode[] opCodes = new OpCode[] {
            new OpCode.ContextCall(functionName)
        };

        interpreter.interpret(context, opCodes);
        verify(function).execute(eq(Collections.emptyList()));
    }

    @Test
    public void shouldCallAMethodInContextWithParameter() {
        FunctionDOS.ContextualFunctionDOS function = mock(FunctionDOS.ContextualFunctionDOS.class);
        context.setSlot(functionName, function);
        context.setSlot(localArgumentName, theObject);

        OpCode[] opCodes = new OpCode[] {
            new OpCode.Push(localArgumentName),
            new OpCode.ContextCall(functionName)
        };

        interpreter.interpret(context, opCodes);

        verify(function).execute(argThat(new BaseMatcher<List>() {

            public boolean matches(Object item) {
                List arguments = (List) item;
                return arguments.contains(theObject);
            }

            public void describeTo(Description description) {
                description.appendText("theObject was not found as an argument");
            }
        }));
    }

    @Test
    public void shouldCallAMethodOnAnObject() {
        FunctionDOS.ContextualFunctionDOS function = mock(FunctionDOS.ContextualFunctionDOS.class);
        theObject.setSlot(functionName, function);
        context.setSlot(localObjectName, theObject);

        OpCode[] opCodes = new OpCode[] {
            new OpCode.SetObject(localObjectName),
            new OpCode.MethodCall(functionName)
        };

        interpreter.interpret(context, opCodes);
        verify(function).execute(theObject, Collections.emptyList());
    }

    @Test
    public void shouldCallAMethodOnObjectWithParameter() {
        final FunctionDOS.ContextualFunctionDOS function = mock(FunctionDOS.ContextualFunctionDOS.class);
        final ObjectDOS expectedArgument = new ObjectDOS();
        theObject.setSlot(functionName, function);
        context.setSlot(localObjectName, theObject);
        context.setSlot(localArgumentName, expectedArgument);

        OpCode[] opCodes = new OpCode[] {
            new OpCode.Push(localArgumentName),
            new OpCode.SetObject(localObjectName),
            new OpCode.MethodCall(functionName)
        };

        interpreter.interpret(context, opCodes);

        verify(function).execute(eq(theObject), argThat(new BaseMatcher<List>() {

            List foundList;

            public boolean matches(Object item) {
                List arguments = (List) item;
                foundList = arguments;
                return arguments.contains(expectedArgument);
            }

            public void describeTo(Description description) {
                description.appendText("Expected argument was not found as an argument..." + foundList);
            }
        }));
    }


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

    @Test
    public void shouldCallAMethodOnThis() {
        FunctionDOS.ContextualFunctionDOS function = mock(FunctionDOS.ContextualFunctionDOS.class);
        theObject.setSlot(functionName, function);
        context.setObject(theObject);

        OpCode[] opCodes = new OpCode[] {
            new OpCode.SetObjectToThis(),
            new OpCode.MethodCall(functionName)
        };

        interpreter.interpret(context, opCodes);
        verify(function).execute(theObject, Collections.emptyList());
    }
//    @Test
//    public void shouldThrowExceptionIfMethodDoesntExist() {
//        ObjectDOS context = new ObjectDOS();
//        ObjectDOS theObject = new ObjectDOS();
//        OpCode[] opCodes = new OpCode[] {};
//
//        interpreter.interpret(context, theObject, opCodes);
//    }

}