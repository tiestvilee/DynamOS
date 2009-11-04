/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos;


import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.dynamos.structures.Context;
import org.dynamos.structures.FunctionDOS;
import org.dynamos.structures.ObjectDOS;
import org.dynamos.structures.OpCode;
import org.dynamos.structures.Symbol;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author tiestvilee
 */
public class OpCodeInterpreterReturnTest {

    OpCodeInterpreter interpreter;
    Context context;
    ObjectDOS theObject;

    Symbol functionName = Symbol.get("functionName");
    Symbol returnTarget = Symbol.get("returnTarget");

    Symbol localArgumentName = Symbol.get("argument");
    Symbol localObjectName = Symbol.get("object");

    @Before
    public void setUp() {
        interpreter = new OpCodeInterpreter();
        context = new Context();
        theObject = new ObjectDOS();
    }

    @Test
    public void shouldReturnValue() {
        ObjectDOS result = new ObjectDOS();
        Symbol resultSymbol = Symbol.get("theResult");
        context.setSlot(resultSymbol, result);

        OpCode[] opCodes = new OpCode[] {
        	new OpCode.Push(resultSymbol),
        	new OpCode.ContextCall(Symbol.SET_RESULT)
        };

        interpreter.interpret(context, opCodes);

        assertThat( (ObjectDOS) context.getSlot(Symbol.RESULT), is(result));
    }

    @Test
    public void shouldReturnAValueIntoDefaultSlot() {
        final ObjectDOS result = new ObjectDOS();
        FunctionDOS function = new FunctionDOS(null, null, null) {

            @Override
            public void execute(Context context) {
                context.setSlot(Symbol.RESULT, result);
            }

        };
        context.setSlot(functionName, new FunctionDOS.ContextualFunctionDOS(function, context));

        OpCode[] opCodes = new OpCode[] {
            new OpCode.ContextCall(functionName)
        };

        interpreter.interpret(context, opCodes);

        assertThat((ObjectDOS) context.getSlot(Symbol.RESULT), is(result));
    }


}
