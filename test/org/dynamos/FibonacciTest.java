/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.dynamos.structures.Context;
import org.dynamos.structures.FunctionDOS;
import org.dynamos.structures.FunctionDefinitionDOS;
import org.dynamos.structures.OpCode;
import org.dynamos.structures.StandardObjects;
import org.dynamos.structures.Symbol;
import org.dynamos.structures.VMObjectDOS;
import org.dynamos.structures.StandardObjects.ValueObject;
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
    Symbol sequenceIndexSymbol = Symbol.get("sequenceIndex");

    Symbol anon1 = Symbol.get("anon1");
    Symbol anon2 = Symbol.get("anon2");

    Symbol temp1 = Symbol.get("temp1");
    Symbol temp1Setter = Symbol.get("temp1:");
    Symbol temp2 = Symbol.get("temp2");

    @Test
    public void shouldCalculateFibonacciAt0() {
    	
    	assetFibonacciAt(0, 1);
        
    }

    @Test
    public void shouldCalculateFibonacciAt1() {
    	
        assetFibonacciAt(1, 1);
        
    }

    @Test
    public void shouldCalculateFibonacciAt2() {
    	
        assetFibonacciAt(2, 2);
        
    }

    @Test
    public void shouldCalculateFibonacciAt3() {
    	
        assetFibonacciAt(3, 3);
        
    }

    @Test
    public void shouldCalculateFibonacciAt4() {
    	
        assetFibonacciAt(4, 5);
        
    }

	private void assetFibonacciAt(int sequenceIndex, int expectedResult) {
        
        System.out.println("******************************************************\n");
        
		OpCodeInterpreter interpreter = new OpCodeInterpreter();
        Context context = interpreter.newContext();

        FunctionDefinitionDOS anon1Function = new FunctionDefinitionDOS(interpreter, new Symbol[] {}, new Symbol[] {}, new OpCode[] {
                new OpCode.Push(one),
                new OpCode.ContextCall(Symbol.SET_RESULT),
	            new OpCode.Debug("returning (1) ", Symbol.RESULT)
        });

        FunctionDefinitionDOS anon2Function = new FunctionDefinitionDOS(interpreter, new Symbol[] {}, new Symbol[] {temp1}, new OpCode[] {
            new OpCode.Push(one),  // result = index - 1
            new OpCode.SetObject(index),
            new OpCode.MethodCall(minus$),

            new OpCode.Push(Symbol.RESULT), // result = fibonacci( result )
            new OpCode.ContextCall(fibonacci$),

            new OpCode.Push(Symbol.RESULT),  // temp1 = result
            new OpCode.ContextCall(temp1Setter),  // temp1 = result

            new OpCode.Push(two),  // result = index - 2
            new OpCode.SetObject(index),
            new OpCode.MethodCall(minus$),

            new OpCode.Push(Symbol.RESULT), // result = fibonacci( result )
            new OpCode.ContextCall(fibonacci$),

            new OpCode.Debug("left side", temp1),
            new OpCode.Debug("right side", Symbol.RESULT),
            new OpCode.Push(Symbol.RESULT), // temp1 = temp1 + result
            new OpCode.SetObject(temp1),
            new OpCode.MethodCall(plus$)
        });

        
        FunctionDOS fibonacciFunction = new FunctionDOS(new FunctionDefinitionDOS(interpreter, new Symbol[] {index}, new Symbol[] {}, new OpCode[] {
	            new OpCode.Debug("in fibonacci with argument", index),
	            new OpCode.Push(two), // result = index isLessThan: two
	            new OpCode.SetObject(index),
	            new OpCode.MethodCall(isLessThan$),
	
	            new OpCode.Push(anon1), // result = result ifTrue: [anon1] ifFalse: [anon2]
	            new OpCode.Push(anon2),
	            new OpCode.SetObject(Symbol.RESULT),
	            new OpCode.MethodCall(ifTrue$IfFalse$),
	            new OpCode.Debug("true or false?", Symbol.RESULT),
	            
	            new OpCode.Push(Symbol.RESULT),  // contextualize anon function
	            new OpCode.Push(Symbol.CURRENT_CONTEXT),
	            new OpCode.ContextCall(Symbol.CONTEXTUALIZE_FUNCTION),
	            new OpCode.Debug("contextualized", Symbol.RESULT),
	            
	            new OpCode.CallFunctionInSlot(Symbol.RESULT), // call anon function
	            new OpCode.Debug("executed function", Symbol.RESULT)
        	}),
        	context);

        context.setSlot(one, StandardObjects.numberDOS(1));
        context.setSlot(two, StandardObjects.numberDOS(2));
        context.setSlot(sequenceIndexSymbol, StandardObjects.numberDOS(sequenceIndex));
        context.setSlot(anon1, anon1Function);
        context.setSlot(anon2, anon2Function);
        context.setFunction(fibonacci$, fibonacciFunction);
        context.setSlot(VMObjectDOS.VM, VMObjectDOS.getVMObject());
        
        interpreter.interpret(context, new OpCode[] {
        	new OpCode.Push(sequenceIndexSymbol),
        	new OpCode.Debug("calling fibonacci with", sequenceIndexSymbol),
        	new OpCode.ContextCall(fibonacci$)
        });
        
        assertThat(((ValueObject) context.getSlot(Symbol.RESULT)).getValue(), is(expectedResult));
	}

}