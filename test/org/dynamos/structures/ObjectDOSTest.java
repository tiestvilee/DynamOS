/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

import org.dynamos.types.StandardObjects;
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
    FunctionWithContext function = new FunctionWithContext(null, null);

    @Before
    public void setUp() {
    	theObject = new ObjectDOS();
    	value = new ObjectDOS();
    	createMinimalEnvironment();
    }

    @Test
    public void shouldAddAndReturnSlot() {
        theObject.setSlot(symbol, value);
        assertThat(theObject.getSlot(symbol), is(value));
    }

    @Test
    public void shouldUseSlotOnSecondTraitIfNotOnObject() {
        ObjectDOS trait1 = new ObjectDOS();
        ObjectDOS trait2 = new ObjectDOS();
        theObject.setTrait("trait1", trait1);
        theObject.setTrait("trait2", trait2);

        trait2.setSlot(symbol, value);

        assertThat(theObject.getSlot(symbol), is(value));
    }

    @Test
    public void shouldOverideSlotOnSecondTraitWithFirstTraitSlot() {
        ObjectDOS trait1 = new ObjectDOS();
        ObjectDOS trait2 = new ObjectDOS();
        theObject.setTrait("trait1", trait1);
        theObject.setTrait("trait2", trait2);

        ObjectDOS oldValue = new ObjectDOS();
        trait2.setSlot(symbol, oldValue);
        
        trait1.setSlot(symbol, value);

        assertThat(theObject.getSlot(symbol), is(value));
    }

    @Test
    public void shouldOverideSlotOnTraitsWithCurrentSlot() {
        ObjectDOS trait1 = new ObjectDOS();
        ObjectDOS trait2 = new ObjectDOS();
        theObject.setTrait("trait1", trait1);
        theObject.setTrait("trait2", trait2);
        
        trait2.setSlot(symbol, new ObjectDOS());
        trait1.setSlot(symbol, new ObjectDOS());
        
        theObject.setSlot(symbol, value);

        assertThat(theObject.getSlot(symbol), is(value));
    }


    @Test
    public void shouldReturnUndefinedIfNoSlot() {
    	theObject = new ObjectDOS();
        assertSame(ObjectDOS.environment.getSlot(Symbol.PLATFORM).getSlot(Symbol.UNDEFINED), theObject.getSlot(symbol));
    }
    
    private void createMinimalEnvironment() {
    	ObjectDOS undefined = new StandardObjects.UndefinedDOS();
    	
    	ObjectDOS platform = new ObjectDOS();
    	platform.setSlot(Symbol.UNDEFINED, undefined);
    	
    	ObjectDOS.environment = new ObjectDOS();
    	ObjectDOS.environment.setSlot(Symbol.PLATFORM, platform);
	}

	@Test
    public void shouldAddAndReturnFunction() {
        theObject.setFunction(symbol, function);
        assertThat(theObject.getFunction(symbol), is((ExecutableDOS) function));
    }

    @Test
    public void shouldUseFunctionOnSecondTraitIfNotOnObject() {
        ObjectDOS trait1 = new ObjectDOS();
        ObjectDOS trait2 = new ObjectDOS();
        theObject.setTrait("trait1", trait1);
        theObject.setTrait("trait2", trait2);

        trait2.setFunction(symbol, function);

        assertThat(theObject.getFunction(symbol), is((ExecutableDOS) function));
    }

    @Test
    public void shouldOverideFunctionOnSecondTraitWithFirstTraitFunction() {
        ObjectDOS trait1 = new ObjectDOS();
        ObjectDOS trait2 = new ObjectDOS();
        theObject.setTrait("trait1", trait1);
        theObject.setTrait("trait2", trait2);

        trait2.setFunction(symbol, new FunctionWithContext(null, null));
        
        trait1.setFunction(symbol, function);

        assertThat(theObject.getFunction(symbol), is((ExecutableDOS) function));
    }

    @Test
    public void shouldOverideFunctionOnTraitsWithCurrentFunction() {
        ObjectDOS trait1 = new ObjectDOS();
        ObjectDOS trait2 = new ObjectDOS();
        theObject.setTrait("trait1", trait1);
        theObject.setTrait("trait2", trait2);
        
        trait2.setSlot(symbol, new FunctionWithContext(null, null));
        trait1.setSlot(symbol, new FunctionWithContext(null, null));

        theObject.setFunction(symbol, function);

        assertThat(theObject.getFunction(symbol), is((ExecutableDOS) function));
    }
}