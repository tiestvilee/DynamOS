/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos;

import java.util.Collections;
import org.dynamos.structures.Context;
import org.dynamos.structures.FunctionDOS;
import org.dynamos.structures.ObjectDOS;
import org.dynamos.structures.OpCode;
import org.dynamos.structures.Symbol;
import org.dynamos.structures.VMObjectDOS;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

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

    @Before
    public void setUp() {
        interpreter = new OpCodeInterpreter();
        context = new Context();
        theObject = new ObjectDOS();
        function = mock(FunctionDOS.ContextualFunctionDOS.class);
    }

    @Test
    public void shouldCallAMethodInContext() {
        context.setSlot(functionName, function);

        OpCode[] opCodes = new OpCode[] {
            new OpCode.ContextCall(functionName)
        };

        interpreter.interpret(context, opCodes);

        verify(function).execute(eq(Collections.emptyList()));
    }

    @Test
    public void shouldCallAMethodInContextWithParameter() {
        context.setSlot(functionName, function);
        context.setSlot(localArgumentName, theObject);

        OpCode[] opCodes = new OpCode[] {
            new OpCode.Push(localArgumentName),
            new OpCode.ContextCall(functionName)
        };

        interpreter.interpret(context, opCodes);

        verify(function).execute(java.util.Arrays.asList( (Object) theObject));
    }

    @Test
    public void shouldCallAMethodOnAnObject() {
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

        verify(function).execute(theObject, java.util.Arrays.asList( (Object) expectedArgument));
    }

    @Test
    public void shouldCallAMethodOnThis() {
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