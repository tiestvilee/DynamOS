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
import org.dynamos.structures.Symbol;
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
	Symbol numberFactory = Symbol.get("numberFactory");;

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
		
        Context applicationContext = interpreter.newContext();
		applicationContext.setSlot(numberFactory, interpreter.getEnvironment().getNumberFactory());

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
	            
	            new OpCode.SetObject(Symbol.RESULT), // call anon function
	            new OpCode.MethodCall(Symbol.EXECUTE),
	            new OpCode.Debug("executed function", Symbol.RESULT)
        	}),
        	applicationContext);

        applicationContext.setSlot(one, interpreter.getEnvironment().getNull());
        applicationContext.setSlot(two, interpreter.getEnvironment().getNull());
        applicationContext.setSlot(sequenceIndexSymbol, interpreter.getEnvironment().getNull());
        applicationContext.setSlot(anon1, anon1Function);
        applicationContext.setSlot(anon2, anon2Function);
        applicationContext.setFunction(fibonacci$, fibonacciFunction);
        
        interpreter.interpret(applicationContext, new OpCode[] {
        	new OpCode.CreateValueObject(interpreter, 1),
        	new OpCode.Push(Symbol.RESULT),
        	new OpCode.SetObject(numberFactory),
        	new OpCode.MethodCall(Symbol.get("numberFrom:")),
        	new OpCode.Push(Symbol.RESULT),
        	new OpCode.ContextCall(Symbol.get("one:")),

        	new OpCode.CreateValueObject(interpreter, 2),
        	new OpCode.Push(Symbol.RESULT),
        	new OpCode.SetObject(numberFactory),
        	new OpCode.MethodCall(Symbol.get("numberFrom:")),
        	new OpCode.Push(Symbol.RESULT),
        	new OpCode.ContextCall(Symbol.get("two:")),
        	
        	new OpCode.CreateValueObject(interpreter, sequenceIndex),
        	new OpCode.Push(Symbol.RESULT),
        	new OpCode.SetObject(numberFactory),
        	new OpCode.MethodCall(Symbol.get("numberFrom:")),
        	new OpCode.Push(Symbol.RESULT),
        	new OpCode.ContextCall(Symbol.get("sequenceIndex:")),

        	new OpCode.Push(sequenceIndexSymbol),
        	new OpCode.Debug("calling fibonacci with", sequenceIndexSymbol),
        	new OpCode.ContextCall(fibonacci$)
        });
        
        assertThat(((ValueObject) applicationContext.getSlot(Symbol.RESULT)).getValue(), is(expectedResult));
	}

}