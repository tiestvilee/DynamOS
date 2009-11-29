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
    Symbol temp2 = Symbol.get("temp2");

    Symbol numberFactory = Symbol.get("numberFactory");
	Symbol listFactory = Symbol.get("listFactory");
	
	Symbol fibonacciLibrarySlot = Symbol.get("fibonacciLibrary");
	Symbol fibonacciLibraryDefinition = Symbol.get("fibonacciLibraryDefinition");
	Symbol fibonacciDefinition = Symbol.get("fibonacciDefinition");
	Symbol argumentList = Symbol.get("argumentList");
	Symbol locals = Symbol.get("locals");
	Symbol opcodes = Symbol.get("opcodes");

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
        
        Context fibonacciLibraryContext = interpreter.newContext();
        
        FunctionDOS fibonacciLibrary = new FunctionDOS(new FunctionDefinitionDOS(interpreter, new Symbol[] {numberFactory, listFactory}, new Symbol[] {one, two, temp1, fibonacciLibrarySlot, argumentList, locals, opcodes, anon1, anon2}, new OpCode[] {
            	new OpCode.Debug("creating fibonacci library", numberFactory),

            	
            	new OpCode.CreateValueObject(interpreter, 1),  // create constant for '1'
            	new OpCode.Push(Symbol.RESULT),
            	new OpCode.SetObject(numberFactory),
            	new OpCode.FunctionCall(Symbol.get("numberFrom:")),
            	new OpCode.Push(Symbol.RESULT),
            	new OpCode.SetSlot(Symbol.get("one")),

            	new OpCode.CreateValueObject(interpreter, 2),  // create constant for '2'
            	new OpCode.Push(Symbol.RESULT),
            	new OpCode.SetObject(numberFactory),
            	new OpCode.FunctionCall(Symbol.get("numberFrom:")),
            	new OpCode.Push(Symbol.RESULT),
            	new OpCode.SetSlot(Symbol.get("two")),

            	// create second anonymous function
            	new OpCode.SetObject(listFactory), // empty symbol list
            	new OpCode.FunctionCall(Symbol.get("newList")),
            	new OpCode.Push(Symbol.RESULT), // copy into arguments
            	new OpCode.SetSlot(argumentList),
            	
            	new OpCode.SetObject(listFactory), // empty symbol list
            	new OpCode.FunctionCall(Symbol.get("newList")),
            	new OpCode.Push(Symbol.RESULT), // copy into locals
            	new OpCode.SetSlot(locals),
            	new OpCode.PushSymbol(temp1), // add 'temp1' local
            	new OpCode.SetObject(argumentList),
            	new OpCode.FunctionCall(Symbol.get("add:")),
            	
            	new OpCode.StartOpCodeList(),
		            new OpCode.Push(one),  // result = index - 1
		            new OpCode.SetObject(index),
		            new OpCode.FunctionCall(minus$),
		
		            new OpCode.Push(Symbol.RESULT), // result = fibonacci( result )
		            new OpCode.FunctionCall(fibonacci$),
		
		            new OpCode.Push(Symbol.RESULT),  // temp1 = result
		            new OpCode.SetSlot(temp1),
		
		            new OpCode.Push(two),  // result = index - 2
		            new OpCode.SetObject(index),
		            new OpCode.FunctionCall(minus$),
		
		            new OpCode.Push(Symbol.RESULT), // result = fibonacci( result )
		            new OpCode.FunctionCall(fibonacci$),
		
		            new OpCode.Debug("left side", temp1),
		            new OpCode.Debug("right side", Symbol.RESULT),
		            new OpCode.Push(Symbol.RESULT), // temp1 = temp1 + result
		            new OpCode.SetObject(temp1),
		            new OpCode.FunctionCall(plus$),
            	new OpCode.EndOpCodeList(),
	            new OpCode.Debug("got opcodes", Symbol.RESULT), 
            	
            	new OpCode.Push(argumentList), // create anon2 function
            	new OpCode.Push(locals),
            	new OpCode.Push(Symbol.RESULT),
            	new OpCode.FunctionCall(Symbol.CREATE_FUNCTION_WITH_ARGUMENTS_$_LOCALS_$_OPCODES_$),
	            new OpCode.Debug("created", Symbol.RESULT),
	            
	            new OpCode.Push(Symbol.RESULT),
	            new OpCode.SetSlot(anon2),

            	// Create fibonacci function
            	new OpCode.SetObject(listFactory), // empty symbol list
            	new OpCode.FunctionCall(Symbol.get("newList")),
            	new OpCode.Push(Symbol.RESULT), // copy into arguments
            	new OpCode.SetSlot(argumentList),
            	new OpCode.PushSymbol(index), // add 'index' parameter
            	new OpCode.SetObject(argumentList),
            	new OpCode.FunctionCall(Symbol.get("add:")),
            	
            	new OpCode.SetObject(listFactory), // empty symbol list
            	new OpCode.FunctionCall(Symbol.get("newList")),
            	new OpCode.Push(Symbol.RESULT), // copy into locals
            	new OpCode.SetSlot(locals),
            	
            	new OpCode.StartOpCodeList(),
	            	// create first anonymous function
            		// mainly here to make sure the nesting of op code lists works, otherwise would be in top context
	            	new OpCode.SetObject(listFactory), // empty symbol list
	            	new OpCode.FunctionCall(Symbol.get("newList")),
	            	new OpCode.Push(Symbol.RESULT), // copy into arguments
	            	new OpCode.SetSlot(argumentList),
	            	
	            	new OpCode.SetObject(listFactory), // empty symbol list
	            	new OpCode.FunctionCall(Symbol.get("newList")),
	            	new OpCode.Push(Symbol.RESULT), // copy into locals
	            	new OpCode.SetSlot(locals),
	            	
	            	new OpCode.StartOpCodeList(),
		                new OpCode.Push(one),
		                new OpCode.SetSlot(Symbol.RESULT),
			            new OpCode.Debug("returning (1) ", Symbol.RESULT),
	            	new OpCode.EndOpCodeList(),
		            new OpCode.Debug("got opcodes", Symbol.RESULT), 
	            	
	            	new OpCode.Push(argumentList), // create anon1 function
	            	new OpCode.Push(locals),
	            	new OpCode.Push(Symbol.RESULT),
	            	new OpCode.FunctionCall(Symbol.CREATE_FUNCTION_WITH_ARGUMENTS_$_LOCALS_$_OPCODES_$),
		            new OpCode.Debug("created", Symbol.RESULT),
		            
		            new OpCode.Push(Symbol.RESULT),
		            new OpCode.SetSlot(anon1),

		            // actual fibonacci function code
		            new OpCode.Debug("in fibonacci with argument", index),
		            new OpCode.Debug("******************************", numberFactory),
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
		            new OpCode.Debug("executed function", Symbol.RESULT),
            	new OpCode.EndOpCodeList(),
	            new OpCode.Debug("got opcodes", Symbol.RESULT), 
            	
            	new OpCode.Push(argumentList), // create fibonacci function
            	new OpCode.Push(locals),
            	new OpCode.Push(Symbol.RESULT),
            	new OpCode.FunctionCall(Symbol.CREATE_FUNCTION_WITH_ARGUMENTS_$_LOCALS_$_OPCODES_$),
	            new OpCode.Debug("created", Symbol.RESULT),
            	
            	new OpCode.Push(Symbol.RESULT),  // contextualise the newly created function
            	new OpCode.Push(Symbol.CURRENT_CONTEXT),
            	new OpCode.FunctionCall(Symbol.CONTEXTUALIZE_FUNCTION_$_IN_$),	  
	            new OpCode.Debug("contextualized", Symbol.RESULT), 
            	
	            new OpCode.Push(Symbol.RESULT),  // store fibonacci function temp
	            new OpCode.SetSlot(temp1),
	            
	            new OpCode.PushSymbol(fibonacci$),  // save fibonacci to context
	            new OpCode.Push(temp1),
	            new OpCode.FunctionCall(Symbol.SET_FUNCTION_$_TO_$),
            	
            	new OpCode.FunctionCall(Symbol.NEW_OBJECT), // create a new, empy object, move into fibonacciLibrarySlot
            	new OpCode.Push(Symbol.RESULT),
            	new OpCode.SetSlot(fibonacciLibrarySlot),
            	
            	new OpCode.PushSymbol(fibonacci$),  // and add to the fibonacci library
            	new OpCode.Push(temp1),
            	new OpCode.SetObject(fibonacciLibrarySlot),
            	new OpCode.FunctionCall(Symbol.SET_FUNCTION_$_TO_$),
	            new OpCode.Debug("created fibonacci library", fibonacciLibrarySlot),
            	
            	new OpCode.Push(fibonacciLibrarySlot),  // return the library
            	new OpCode.SetSlot(Symbol.RESULT)
    	}),
    	fibonacciLibraryContext);


        Context applicationContext = interpreter.newContext();
		applicationContext.setSlot(numberFactory, interpreter.getEnvironment().getNumberFactory());
		applicationContext.setSlot(listFactory, interpreter.getEnvironment().getListFactory());
		applicationContext.setFunction(fibonacciLibraryDefinition, fibonacciLibrary);
        applicationContext.setSlot(sequenceIndexSymbol, interpreter.getEnvironment().getNull());
        applicationContext.setSlot(fibonacciLibrarySlot, interpreter.getEnvironment().getNull());

        interpreter.interpret(applicationContext, new OpCode[] {
        	new OpCode.CreateValueObject(interpreter, sequenceIndex), // set up the index we want to get fibonacci for
        	new OpCode.Push(Symbol.RESULT),
        	new OpCode.SetObject(numberFactory),
        	new OpCode.FunctionCall(Symbol.get("numberFrom:")),
        	new OpCode.Push(Symbol.RESULT),
        	new OpCode.SetSlot(Symbol.get("sequenceIndex")),
        	
        	new OpCode.Debug("about to create fibonacci library", fibonacciLibraryDefinition),
        	new OpCode.Push(numberFactory),  // initialise the fibonacciLibrary
        	new OpCode.Push(listFactory),
        	new OpCode.FunctionCall(fibonacciLibraryDefinition),
        	
        	new OpCode.Push(Symbol.RESULT),  // and store it in a slot
        	new OpCode.SetSlot(fibonacciLibrarySlot),

        	new OpCode.Push(sequenceIndexSymbol),  // now call fibonacci function
        	new OpCode.SetObject(fibonacciLibrarySlot),
        	new OpCode.Debug("calling fibonacci with", sequenceIndexSymbol),
        	new OpCode.FunctionCall(fibonacci$)
        });
        
        assertThat(((ValueObject) applicationContext.getSlot(Symbol.RESULT)).getValue(), is(expectedResult));
	}

}