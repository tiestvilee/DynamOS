/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tiestvilee
 */
public class ObjectDOSTest {

    ObjectDOS theObject;
    Symbol symbol = Symbol.get("Symbol");
    String value = "doesnt matter";
    FunctionDOS function = new FunctionDOS();

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
        String oldValue = "shouldnt appear";
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
        theObject.setSlot(symbol, function);
        assertSame(function, theObject.getFunction(symbol));
    }
}