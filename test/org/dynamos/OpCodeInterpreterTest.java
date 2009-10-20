/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos;

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

    Symbol localArgument = Symbol.get("argument");
    Symbol localObject = Symbol.get("object");

    @Before
    public void setUp() {
        interpreter = new OpCodeInterpreter();
        context = new Context();
        theObject = new ObjectDOS();
    }

    @Test
    public void shouldCallAMethodInContext() {
        FunctionDOS function = mock(FunctionDOS.class);
        context.setSlot(functionName, function);

        OpCode[] opCodes = new OpCode[] {
            new OpCode.ContextCall(functionName)
        };

        interpreter.interpret(context, opCodes);
        verify(function).execute(any(Context.class));
    }

    @Test
    public void shouldCallAMethodInContextWithParameter() {
        FunctionDOS function = mock(FunctionDOS.class);
        context.setSlot(functionName, function);
        context.setSlot(localArgument, theObject);

        OpCode[] opCodes = new OpCode[] {
            new OpCode.Push(localArgument),
            new OpCode.ContextCall(functionName)
        };

        interpreter.interpret(context, opCodes);

        verify(function).execute(argThat(new BaseMatcher<Context>() {

            public boolean matches(Object item) {
                Context context = (Context) item;
                List slot = (List) context.getSlot(Symbol.ARGUMENTS);
                return slot.contains(theObject);
            }

            public void describeTo(Description description) {
                description.appendText("theObject was not found as an argument");
            }
        }));
    }

    @Test
    public void shouldCallAMethodOnAnObject() {
        FunctionDOS function = mock(FunctionDOS.class);
        theObject.setSlot(functionName, function);
        context.setSlot(localObject, theObject);

        OpCode[] opCodes = new OpCode[] {
            new OpCode.SetObject(localObject),
            new OpCode.MethodCall(functionName)
        };

        interpreter.interpret(context, opCodes);
        verify(function).execute(any(Context.class));
    }

    @Test
    public void shouldCallAMethodOnObjectWithParameter() {
        final FunctionDOS function = mock(FunctionDOS.class);
        final ObjectDOS expectedArgument = new ObjectDOS();
        theObject.setSlot(functionName, function);
        context.setSlot(localObject, theObject);
        context.setSlot(localArgument, expectedArgument);

        OpCode[] opCodes = new OpCode[] {
            new OpCode.Push(localArgument),
            new OpCode.SetObject(localObject),
            new OpCode.MethodCall(functionName)
        };

        interpreter.interpret(context, opCodes);

        verify(function).execute(argThat(new BaseMatcher<Context>() {

            public boolean matches(Object item) {
                Context context = (Context) item;
                List slot = (List) context.getSlot(Symbol.ARGUMENTS);
                return slot.contains(expectedArgument);
            }

            public void describeTo(Description description) {
                description.appendText("theObject was not found as an argument");
            }
        }));
    }


    @Test
    public void shouldCallThroughToVM() {
        Symbol vm = Symbol.get("VM");
        context.setSlot(vm, VMObjectDOS.VM);
        context.setSlot(localArgument, theObject);

        OpCode[] opCodes = new OpCode[] {
            new OpCode.Push(localArgument),
            new OpCode.SetObject(vm),
            new OpCode.MethodCall(Symbol.get("print"))
        };

        interpreter.interpret(context, opCodes);
    }

    @Test
    public void shouldCallAMethodOnThis() {
        FunctionDOS function = mock(FunctionDOS.class);
        theObject.setSlot(functionName, function);
        context.setObject(theObject);

        OpCode[] opCodes = new OpCode[] {
            new OpCode.SetObjectToThis(),
            new OpCode.MethodCall(functionName)
        };

        interpreter.interpret(context, opCodes);
        verify(function).execute(any(Context.class));
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