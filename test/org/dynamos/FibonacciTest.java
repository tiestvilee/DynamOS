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
            new OpCode.Return(index)
        });

        FunctionDOS anon2Function = new FunctionDOS(interpreter, new OpCode[] {

            new OpCode.Push(one),  // temp1 = index - 1
            new OpCode.SetObject(index),
            new OpCode.MethodCall(minus$), // return into temp1

            new OpCode.Push(temp1), // temp1 = fibonacci( temp1 )
            new OpCode.ContextCall(fibonacci$), // return into temp1...

            new OpCode.Push(two),  // temp2 = index - 2
            new OpCode.SetObject(index),
            new OpCode.MethodCall(minus$), // return into temp2

            new OpCode.Push(temp2), // temp2 = fibonacci( temp2 )
            new OpCode.ContextCall(fibonacci$), // return into temp2...

            new OpCode.Push(temp2), // temp1 = temp1 + temp2
            new OpCode.SetObject(temp1),
            new OpCode.MethodCall(plus$), // return into temp1...

            new OpCode.Return(temp1)
        });

        FunctionDOS fibonacciFunction = new FunctionDOS(interpreter, new OpCode[] {
            new OpCode.Push(two), // temp1 = index isLessThan: two
            new OpCode.SetObject(index),
            new OpCode.ContextCall(isLessThan$), // return into temp1...

            new OpCode.Push(anon1), // temp1 = temp1 ifTrue: anon1 ifFalse: anon2
            new OpCode.Push(anon2),
            new OpCode.SetObject(temp1),
            new OpCode.MethodCall(ifTrue$IfFalse$), // return into temp1...

            new OpCode.Return(temp1)
        });
    }

}