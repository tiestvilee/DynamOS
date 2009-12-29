/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.dynamos.structures.Activation;
import org.dynamos.structures.FunctionDOS;
import org.dynamos.structures.ObjectDOS;
import org.dynamos.structures.OpCode;
import org.dynamos.structures.Symbol;
import org.dynamos.types.NumberDOS;
import org.junit.Test;

/**
 *
 * @author tiestvilee
 */
public class FibonacciTestOriginal {
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
	Symbol numberFactory = Symbol.get("numberFactory");

	Symbol applicationObject = Symbol.get("application");

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

    @Test
    public void shouldCalculateFibonacciAt5() {
    	
        assetFibonacciAt(5, 8);
        
    }

	private void assetFibonacciAt(int sequenceIndex, int expectedResult) {
        
        System.out.println("******************************************************\n");
        
		OpCodeInterpreter interpreter = new OpCodeInterpreter();
		Environment environment = interpreter.getEnvironment();
		
        FunctionDOS anon1Function = environment.createFunction(new Symbol[] {}, new OpCode[] {
                new OpCode.PushSymbol(one),
                new OpCode.FunctionCall(Symbol.GET_SLOT_$),
	            new OpCode.Debug("returning (1) ", Symbol.RESULT)
        });

        FunctionDOS anon2Function = environment.createFunction(new Symbol[] {}, new OpCode[] {
            new OpCode.Push(one),  // result = index - 1
            new OpCode.SetObject(index),
            new OpCode.FunctionCall(minus$),

            new OpCode.Push(Symbol.RESULT), // result = fibonacci( result )
            new OpCode.FunctionCall(fibonacci$),

            new OpCode.PushSymbol(temp1),  // temp1 = result
            new OpCode.Push(Symbol.RESULT),
            new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),

            new OpCode.Push(two),  // result = index - 2
            new OpCode.SetObject(index),
            new OpCode.FunctionCall(minus$),

            new OpCode.Push(Symbol.RESULT), // result = fibonacci( result )
            new OpCode.FunctionCall(fibonacci$),

            new OpCode.Debug("processing index", index),
            new OpCode.Debug("left side", temp1),
            new OpCode.Debug("right side", Symbol.RESULT),
            new OpCode.Push(Symbol.RESULT), // temp1 = temp1 + result
            new OpCode.SetObject(temp1),
            new OpCode.FunctionCall(plus$),
            new OpCode.Debug("hence result", Symbol.RESULT),
        });

        
        FunctionDOS fibonacciFunction = environment.createFunction(new Symbol[] {index}, new OpCode[] {
	            new OpCode.Debug("in fibonacci with argument", index),
	            new OpCode.Push(two), // result = index isLessThan: two
	            new OpCode.SetObject(index),
	            new OpCode.FunctionCall(isLessThan$),
	
	            new OpCode.Push(anon1), // result = result ifTrue: [anon1] ifFalse: [anon2]
	            new OpCode.Push(anon2),
	            new OpCode.SetObject(Symbol.RESULT),
	            new OpCode.FunctionCall(ifTrue$IfFalse$),
	            new OpCode.Debug("true or false?", Symbol.RESULT),
	            
	            new OpCode.Push(Symbol.RESULT),  // contextualize anon function
	            new OpCode.Push(Symbol.CURRENT_CONTEXT),
	            new OpCode.FunctionCall(Symbol.CONTEXTUALIZE_FUNCTION_$_IN_$),
	            new OpCode.Debug("contextualized", Symbol.RESULT),
	            
	            new OpCode.SetObject(Symbol.RESULT), // call anon function
	            new OpCode.FunctionCall(Symbol.EXECUTE),
	            new OpCode.Debug("finished fibonacci, returning", Symbol.RESULT)
        	});

        ObjectDOS application = environment.createNewObject();

        application.setSlot(one, NumberDOS.numberDOS(interpreter.getEnvironment(), 1));
        application.setSlot(two, NumberDOS.numberDOS(interpreter.getEnvironment(), 2));
        application.setSlot(anon1, anon1Function);
        application.setSlot(anon2, anon2Function);
        application.setFunction(fibonacci$, fibonacciFunction);
        
        Activation activation = interpreter.newActivation();
        activation.setSlot(applicationObject, application);
        activation.setSlot(sequenceIndexSymbol, NumberDOS.numberDOS(interpreter.getEnvironment(), sequenceIndex));
        
        interpreter.interpret(activation, new OpCode[] {
        	new OpCode.Push(sequenceIndexSymbol),
        	new OpCode.SetObject(applicationObject),
        	new OpCode.Debug("calling fibonacci with", sequenceIndexSymbol),
        	new OpCode.FunctionCall(fibonacci$),
        	new OpCode.Debug("shell finished", Symbol.RESULT),
        });
        
        assertThat(((NumberDOS.ValueObject) activation.getSlot(Symbol.RESULT)).getValue(), is(expectedResult));
	}

}