/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

import org.dynamos.Environment;
import org.dynamos.OpCodeInterpreter;
import org.hamcrest.CoreMatchers;
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
    public void shouldReturnNullIfNoSlot() {
    	theObject = environment.createNewObject(); // TODO again, this sux the big time
    	Environment env = new Environment(new OpCodeInterpreter());
		ObjectDOS.initialiseRootObject(env, theObject);
        assertSame(env.getUndefined(), theObject.getSlot(symbol));
    }

    @Test
    public void shouldReturnFunction() {
        theObject.setFunction(symbol, function);
        assertThat(theObject.getFunction(symbol), is((ExecutableDOS)function));
    }
    
    @Test
    public void shouldReturnGetterFunctionByDefault() {
    	theObject.setSlot(symbol, value);
    	ExecutableDOS function = theObject.getFunction(symbol);
    	assertThat(function, CoreMatchers.instanceOf(StandardFunctions.Getter.class));
    	assertThat(((StandardFunctions.Getter) function).forSlot(), is(symbol));
    }
    
    @Test
    public void shouldReturnSetterFunctionByDefault() {
    	theObject.setSlot(symbol, value);
    	ExecutableDOS function = theObject.getFunction(symbol.toSetterSymbol());
    	assertThat(function, CoreMatchers.instanceOf(StandardFunctions.Setter.class));
    	assertThat(((StandardFunctions.Setter) function).forSlot(), is(symbol));
    }
}