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
    FunctionWithContext function = new FunctionWithContext(null, null);
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
    public void shouldUseSlotOnSecondTraitIfNotOnObject() {
        ObjectDOS trait1 = environment.createNewObject();
        ObjectDOS trait2 = environment.createNewObject();
        theObject.setTrait("trait1", trait1);
        theObject.setTrait("trait2", trait2);

        trait2.setSlot(symbol, value);

        assertThat(theObject.getSlot(symbol), is(value));
    }

    @Test
    public void shouldOverideSlotOnSecondTraitWithFirstTraitSlot() {
        ObjectDOS trait1 = environment.createNewObject();
        ObjectDOS trait2 = environment.createNewObject();
        theObject.setTrait("trait1", trait1);
        theObject.setTrait("trait2", trait2);

        ObjectDOS oldValue = environment.createNewObject();
        trait2.setSlot(symbol, oldValue);
        
        trait1.setSlot(symbol, value);

        assertThat(theObject.getSlot(symbol), is(value));
    }

    @Test
    public void shouldOverideSlotOnTraitsWithCurrentSlot() {
        ObjectDOS trait1 = environment.createNewObject();
        ObjectDOS trait2 = environment.createNewObject();
        theObject.setTrait("trait1", trait1);
        theObject.setTrait("trait2", trait2);
        
        trait2.setSlot(symbol, environment.createNewObject());
        trait1.setSlot(symbol, environment.createNewObject());
        
        theObject.setSlot(symbol, value);

        assertThat(theObject.getSlot(symbol), is(value));
    }


    @Test
    public void shouldReturnUndefinedIfNoSlot() {
    	theObject = environment.createNewObject(); // TODO again, this sux the big time
    	Environment env = new Environment(new OpCodeInterpreter());
		ObjectDOS.initialiseRootObject(env, theObject);
        assertSame(env.getUndefined(), theObject.getSlot(symbol));
    }
    
    @Test
    public void shouldAddAndReturnFunction() {
        theObject.setFunction(symbol, function);
        assertThat(theObject.getFunction(symbol), is((ExecutableDOS) function));
    }

    @Test
    public void shouldUseFunctionOnSecondTraitIfNotOnObject() {
        ObjectDOS trait1 = environment.createNewObject();
        ObjectDOS trait2 = environment.createNewObject();
        theObject.setTrait("trait1", trait1);
        theObject.setTrait("trait2", trait2);

        trait2.setFunction(symbol, function);

        assertThat(theObject.getFunction(symbol), is((ExecutableDOS) function));
    }

    @Test
    public void shouldOverideFunctionOnSecondTraitWithFirstTraitFunction() {
        ObjectDOS trait1 = environment.createNewObject();
        ObjectDOS trait2 = environment.createNewObject();
        theObject.setTrait("trait1", trait1);
        theObject.setTrait("trait2", trait2);

        trait2.setFunction(symbol, new FunctionWithContext(null, null));
        
        trait1.setFunction(symbol, function);

        assertThat(theObject.getFunction(symbol), is((ExecutableDOS) function));
    }

    @Test
    public void shouldOverideFunctionOnTraitsWithCurrentFunction() {
        ObjectDOS trait1 = environment.createNewObject();
        ObjectDOS trait2 = environment.createNewObject();
        theObject.setTrait("trait1", trait1);
        theObject.setTrait("trait2", trait2);
        
        trait2.setSlot(symbol, new FunctionWithContext(null, null));
        trait1.setSlot(symbol, new FunctionWithContext(null, null));

        theObject.setFunction(symbol, function);

        assertThat(theObject.getFunction(symbol), is((ExecutableDOS) function));
    }
}