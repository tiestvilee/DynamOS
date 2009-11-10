/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.dynamos.OpCodeInterpreter;
import org.dynamos.structures.StandardObjects.ValueObject;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author tiestvilee
 */
public class OpCodeTest {

    FunctionDefinitionDOS function;
    Context context;
    ListDOS arguments;
    ObjectDOS object;
    OpCodeInterpreter interpreter;

    @Before
    public void setup() {
    	interpreter = new OpCodeInterpreter();
    	context = interpreter.newContext();
    }

    @Test
    public void shouldCreateValueObject() {
    	FunctionDefinitionDOS functionDefinition = new FunctionDefinitionDOS(interpreter,
		    				new Symbol[] {},
		    				new Symbol[] {},
		    				new OpCode[] {
		    					new OpCode.CreateValueObject(interpreter, 345)
		    			});
    	
    	functionDefinition.execute(context);
    	
    	assertThat(((ValueObject) context.getSlot(Symbol.RESULT)).getValue(), is(345));
    }

}