/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author tiestvilee
 */
public class ObjectDOSTest {

    ObjectDOS theObject;
    Symbol symbol = Symbol.get("Symbol");
    ObjectDOS value = new ObjectDOS();
    FunctionDOS.ContextualFunctionDOS function = new FunctionDOS.ContextualFunctionDOS(null, null);

    @Before
    public void setUp() {
        theObject = new ObjectDOS();
    }

    @Test
    public void shouldAddAndReturnSlot() {
        theObject.setSlot(symbol, value);
        assertSame(value, theObject.getSlot(symbol));
    }

    @Test
    public void shouldUseParentSlotIfNotOnChild() {
        ObjectDOS parent = new ObjectDOS();
        parent.setSlot(symbol, value);
        theObject.setParent(parent);

        assertSame(value, theObject.getSlot(symbol));
    }

    @Test
    public void shouldOverideParentSlotWithChildSlot() {
        ObjectDOS parent = new ObjectDOS();
        ObjectDOS oldValue = new ObjectDOS();
        parent.setSlot(symbol, oldValue);
        theObject.setParent(parent);
        parent.setSlot(symbol, value);

        assertSame(value, theObject.getSlot(symbol));
    }

    @Test
    public void shouldReturnNullIfNoSlot() {
        assertSame(StandardObjects.NULL, theObject.getSlot(symbol));
    }

    @Test
    public void shouldReturnFunction() {
        theObject.setFunction(symbol, function);
        assertThat(theObject.getFunction(symbol), is(function));
    }
    
    @Test
    public void shouldReturnGetterFunctionByDefault() {
    	theObject.setSlot(symbol, value);
    	FunctionDOS.ContextualFunctionDOS function = theObject.getFunction(symbol);
    	assertThat(function, CoreMatchers.instanceOf(StandardFunctions.Getter.class));
    	assertThat(((StandardFunctions.Getter) function).forSlot(), is(symbol));
    }
    
    @Test
    public void shouldReturnSetterFunctionByDefault() {
    	theObject.setSlot(symbol, value);
    	FunctionDOS.ContextualFunctionDOS function = theObject.getFunction(symbol.toSetterSymbol());
    	assertThat(function, CoreMatchers.instanceOf(StandardFunctions.Setter.class));
    	assertThat(((StandardFunctions.Setter) function).forSlot(), is(symbol));
    }
}