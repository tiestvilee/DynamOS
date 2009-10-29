/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos;

import org.dynamos.structures.Context;
import org.dynamos.structures.FunctionDOS;
import org.dynamos.structures.OpCode;
import org.dynamos.structures.StandardObjects;
import org.dynamos.structures.Symbol;
import org.junit.Test;

/**
 *
 * @author tiestvilee
 */
public class FibonacciTest {
/*
 * function fibonacci: index
 *   (index isLessThan: 2)
 *     ifTrue:
 *       [ ^ index ]
 *     ifFalse:
 *       [ ^ (fibonacci: (index minus: 1)) plus: (fibonacci: (index minus: 2)) ]
 * 
 */
    Symbol fibonacci$ = Symbol.get("fibonacci:");
    Symbol index = Symbol.get("index");
    Symbol isLessThan$ = Symbol.get("isLessThan:");
    Symbol ifTrue$IfFalse$ = Symbol.get("ifTrue:ifFalse:");
    Symbol minus$ = Symbol.get("minus:");
    Symbol plus$ = Symbol.get("plus:");

    Symbol one = Symbol.get("one");
    Symbol two = Symbol.get("two");

    Symbol anon1 = Symbol.get("anon1");
    Symbol anon2 = Symbol.get("anon2");

    Symbol temp1 = Symbol.get("temp1");
    Symbol temp2 = Symbol.get("temp2");

    @Test
    public void shouldCallAMethodInContext() {
        OpCodeInterpreter interpreter = new OpCodeInterpreter();

        FunctionDOS anon1Function = new FunctionDOS(interpreter, new OpCode[] {
            //new OpCode.Return(index)
        });

        FunctionDOS anon2Function = new FunctionDOS(interpreter, new OpCode[] {

            new OpCode.Push(one),  // result = index - 1
            new OpCode.SetObject(index),
            new OpCode.MethodCall(minus$),

            new OpCode.Push(Symbol.RESULT), // temp1 = fibonacci( result )
            //new OpCode.SetResultTarget(temp1),
            new OpCode.ContextCall(fibonacci$),
            // TODO need to more result into temp1, should this be SetResultTarget or a separate call, maybe a 'set context value' call.

            new OpCode.Push(two),  // result = index - 2
            new OpCode.SetObject(index),
            new OpCode.MethodCall(minus$),

            new OpCode.Push(Symbol.RESULT), // result = fibonacci( result )
            new OpCode.ContextCall(fibonacci$),

            new OpCode.Push(Symbol.RESULT), // temp1 = temp1 + result
            new OpCode.SetObject(temp1),
            new OpCode.MethodCall(plus$),

            //new OpCode.Return(Symbol.RESULT)
        });

        FunctionDOS fibonacciFunction = new FunctionDOS(interpreter, new OpCode[] {
            new OpCode.Push(two), // result = index isLessThan: two
            new OpCode.SetObject(index),
            new OpCode.ContextCall(isLessThan$),

            new OpCode.Push(anon1), // result = result ifTrue: [anon1] ifFalse: [anon2]
            new OpCode.Push(anon2),
            new OpCode.SetObject(Symbol.RESULT),
            new OpCode.MethodCall(ifTrue$IfFalse$),

            //new OpCode.Return(Symbol.RESULT)
        });

        Context context = new Context();
        context.setSlot(one, StandardObjects.numberDOS(interpreter, 1));
        context.setSlot(two, StandardObjects.numberDOS(interpreter, 2));
        context.setSlot(anon1, anon1Function);
        context.setSlot(anon2, anon2Function);
        context.setSlot(fibonacci$, fibonacciFunction);
    }

}