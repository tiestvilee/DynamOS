/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.dynamos.structures.Activation;
import org.dynamos.structures.FunctionWithContext;
import org.dynamos.structures.ObjectDOS;
import org.dynamos.structures.OpCode;
import org.dynamos.structures.Symbol;
import org.dynamos.structures.VMObjectDOS;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author tiestvilee
 */
public class OpCodeInterpreterCallTest {

    OpCodeInterpreter interpreter;
    Activation context;
    ObjectDOS theObject;
    FunctionWithContext function;

    Symbol functionName = Symbol.get("functionName");

    Symbol localArgumentName = Symbol.get("argument");
    Symbol localObjectName = Symbol.get("object");
    
    List<ObjectDOS> expectedArgumentList;

    @Before
    public void setUp() {
        interpreter = new OpCodeInterpreter();
        context = interpreter.newActivation();
        theObject = interpreter.getEnvironment().createNewObject();
        function = mock(FunctionWithContext.class);
        expectedArgumentList = new ArrayList<ObjectDOS>();
    }

    /* When a context call is made, it is essentially a method call but with the context object */
    @Test
    public void shouldCallAMethodOnThis() {
        context.setFunction(functionName, function);

        OpCode[] opCodes = new OpCode[] {
            new OpCode.FunctionCall(functionName)
        };

        interpreter.interpret(context, opCodes);

        verify(function).execute(argThat(is(interpreter)), argThat(is(context)), argThat(is(expectedArgumentList)));
    }

    @Test
    public void shouldCallAMethodOnThisWithParameter() {
        context.setFunction(functionName, function);
        context.setSlot(localArgumentName, theObject);

        OpCode[] opCodes = new OpCode[] {
            new OpCode.Push(localArgumentName),
            new OpCode.FunctionCall(functionName)
        };

        interpreter.interpret(context, opCodes);

        expectedArgumentList.add(theObject);
        verify(function).execute(argThat(is(interpreter)), argThat(is(context)), argThat(is(expectedArgumentList)));
    }

    @Test
    public void shouldCallAMethodOnAnObject() {
        theObject.setFunction(functionName, function);
        context.setSlot(localObjectName, theObject);

        OpCode[] opCodes = new OpCode[] {
            new OpCode.SetObject(localObjectName),
            new OpCode.FunctionCall(functionName)
        };

        interpreter.interpret(context, opCodes);

        verify(function).execute(argThat(is(interpreter)), argThat(is(theObject)), argThat(is(expectedArgumentList)));
    }

    @Test
    public void shouldCallAMethodOnObjectWithParameter() {
        final ObjectDOS expectedArgument = interpreter.getEnvironment().createNewObject();
        theObject.setFunction(functionName, function);
        context.setSlot(localObjectName, theObject);
        context.setSlot(localArgumentName, expectedArgument);

        OpCode[] opCodes = new OpCode[] {
            new OpCode.Push(localArgumentName),
            new OpCode.SetObject(localObjectName),
            new OpCode.FunctionCall(functionName)
        };

        interpreter.interpret(context, opCodes);

        expectedArgumentList.add(expectedArgument);
        verify(function).execute(argThat(is(interpreter)), argThat(is(theObject)), argThat(is(expectedArgumentList)));
    }

    @Test
    public void shouldCallAMethodOnSuperObject() {
        ObjectDOS superObject = interpreter.getEnvironment().createNewObject();
    	superObject.setFunction(functionName, function);
        theObject.setParent(superObject);
        context.setSlot(localObjectName, theObject);

        OpCode[] opCodes = new OpCode[] {
            new OpCode.SetObject(localObjectName),
            new OpCode.FunctionCall(functionName)
        };

        interpreter.interpret(context, opCodes);

        verify(function).execute(argThat(is(interpreter)), argThat(is(theObject)), argThat(is(expectedArgumentList)));
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
        context.setSlot(vm, VMObjectDOS.getVMObject(interpreter.getEnvironment()));
        context.setSlot(localArgumentName, theObject);

        OpCode[] opCodes = new OpCode[] {
            new OpCode.Push(localArgumentName),
            new OpCode.SetObject(vm),
            new OpCode.FunctionCall(Symbol.get("print"))
        };

        interpreter.interpret(context, opCodes);
    }

}