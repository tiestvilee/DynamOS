/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.dynamos.Environment;
import org.dynamos.OpCodeInterpreter;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author tiestvilee
 */
public class ObjectDOSTest {

    ObjectDOS theObject;
    Symbol symbol = Symbol.get("MySymbol");
    ObjectDOS value;
    ObjectDOS nullDOS;
    FunctionDOS function = new FunctionDOS(null, null);
	private OpCodeInterpreter interpreter;
	private Environment environment;

    @Before
    public void setUp() {
    	interpreter = new OpCodeInterpreter();
    	environment = interpreter.getEnvironment();
    	nullDOS = environment.getNull();
    	value = environment.createNewObject();
    	theObject = environment.createNewObject();
    }

    @Test
    public void shouldAddAndReturnSlot() {
        theObject.setSlot(symbol, value);
        assertThat(theObject.getSlot(symbol), is(value));
    }

    @Test
    public void shouldUseContextSlotIfNotOnObject() {
        ObjectDOS context = environment.createNewObject();
        context.setSlot(symbol, value);
        theObject.setContext(context);

        assertThat(theObject.getSlot(symbol), is(value));
    }

    @Test
    public void shouldOverideContextSlotWithCurrentSlot() {
        ObjectDOS context = environment.createNewObject();

        ObjectDOS oldValue = environment.createNewObject();
        context.setSlot(symbol, oldValue);
        
        theObject.setContext(context);
        theObject.setSlot(symbol, value);

        assertThat(theObject.getSlot(symbol), is(value));
    }

    @Test
    public void shouldNotAccessSlotsOnParent() {
        ObjectDOS parent = environment.createNewObject();
        parent.setSlot(symbol, value);
        
        theObject.setParent(parent);

        assertThat(theObject.getSlot(symbol), is(environment.getUndefined()));
    }

    @Test
    public void shouldReturnUndefinedIfNoSlot() {
    	theObject = environment.createNewObject(); // TODO again, this sux the big time
    	Environment env = new Environment(new OpCodeInterpreter());
		ObjectDOS.initialiseRootObject(env, theObject);
        assertSame(env.getUndefined(), theObject.getSlot(symbol));
    }

    @Test
    public void shouldUseFunctionLookupToFindFunction() {
    	ObjectDOS.FUNCTION_LOOKUP = mock(FunctionLookupStrategy.class);
    	
        theObject.getFunction(symbol);
        
        verify(ObjectDOS.FUNCTION_LOOKUP).lookupFunction(theObject, symbol);
    }
}