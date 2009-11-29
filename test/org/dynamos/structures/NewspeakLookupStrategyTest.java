package org.dynamos.structures;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.dynamos.Environment;
import org.dynamos.OpCodeInterpreter;
import org.junit.Before;
import org.junit.Test;


public class NewspeakLookupStrategyTest {

    /* first the easy ones */
    
	private Environment environment;
    private ObjectDOS context;
    private ObjectDOS object;
	private Symbol symbol;
	private ExecutableDOS aFunction;
	private ExecutableDOS anotherFunction;
	
    @Before
    public void setup() {
    	OpCodeInterpreter interpreter = new OpCodeInterpreter();
    	environment = interpreter.getEnvironment();
    	context = interpreter.newContext();
    	symbol = Symbol.get("symbol");
    	aFunction = mock(ExecutableDOS.class);
    	anotherFunction = mock(ExecutableDOS.class);
    	object = environment.createNewObject();
    }

	/* simple context searches, no objects involved, just treck up the context until you find it */
    @Test
    public void shouldFindFunctionInCurrentContext() {
    	context.setFunction(symbol, aFunction);

    	ExecutableDOS actualFunction = new NewspeakLookupStrategy().lookupFunction(context, symbol);
    	
    	assertThat(actualFunction, is(aFunction));
    }

    /* simple calls into an object.  Just find the function on the object or it's parent */
    @Test
    public void shouldFindFunctionInNestingContext() {
    	Context nestingContext = new Context();
    	nestingContext.setFunction(symbol, aFunction);
    	
    	context.setContext(nestingContext);
    	
    	ExecutableDOS actualFunction = new NewspeakLookupStrategy().lookupFunction(context, symbol);
    	
    	assertThat(actualFunction, is(aFunction));
    }

    @Test
    public void shouldFindFunctionOnSpecifiedObject() {
    	object.setFunction(symbol, aFunction);
    	
    	ExecutableDOS actualFunction = new NewspeakLookupStrategy().lookupFunction(object, symbol);
    	
    	assertThat(actualFunction, is(aFunction));
    }

    @Test
    public void shouldFindFunctionOnSuperObject() {
    	ObjectDOS superObject = environment.createNewObject();
    	superObject.setFunction(symbol, aFunction);
    	
    	object.setParent(superObject);
    	
    	ExecutableDOS actualFunction = new NewspeakLookupStrategy().lookupFunction(object, symbol);
    	
    	assertThat(actualFunction, is(aFunction));
    }
    
    /* now the harder ones... */

    /* these first few tests are executed as if doing stuff within the actual 
     * object context, as opposed to within a function within the object context.
     * Essentially these are constructor calls.
     */
    /* Inside an object context, making a call to a nesting context function */
    @Test
    public void shouldFindFunctionInNestingContextOfObjectContext() {
    	context.setFunction(symbol, aFunction);
    	
    	object.setContext(context);
    	
    	ExecutableDOS actualFunction = new NewspeakLookupStrategy().lookupFunction(object, symbol);
    	
    	assertThat(actualFunction, is(aFunction));
    }
    
    /* Inside an object context, making a call to a super function */
    @Test
    public void shouldFindFunctionInSuperObjectOfObjectContext() {
    	ObjectDOS parent = environment.createNewObject();
    	parent.setFunction(symbol, aFunction);
    	
    	object.setParent(parent);
    	
    	ExecutableDOS actualFunction = new NewspeakLookupStrategy().lookupFunction(object, symbol);
    	
    	assertThat(actualFunction, is(aFunction));
    }

    /* For good luck; inside an object context, making a call to a nesting object context function */
    @Test
    public void shouldFindFunctionInNestingObjectOfObjectContext() {
    	ObjectDOS nesting = environment.createNewObject();
    	nesting.setFunction(symbol, aFunction);
    	
    	object.setContext(nesting);
    	
    	ExecutableDOS actualFunction = new NewspeakLookupStrategy().lookupFunction(object, symbol);
    	
    	assertThat(actualFunction, is(aFunction));
    }
    
    /* These are inside functions that are defined within an object context,
     * essentially these are instance functions
     */
    /* Inside a function context making a call to nesting object context
     * ie this is an instance function calling another instance function 
     */
    @Test
    public void shouldFindFunctionInNestingObjectOfFunctionContext() {
    	ObjectDOS nesting = environment.createNewObject();
    	nesting.setFunction(symbol, aFunction);
    	
    	context.setContext(nesting);
    	
    	ExecutableDOS actualFunction = new NewspeakLookupStrategy().lookupFunction(context, symbol);
    	
    	assertThat(actualFunction, is(aFunction));
    }

    /* Inside a function context making a call to nesting object context's super
     * ie this is an instance function calling super function 
     */
    @Test
    public void shouldFindFunctionInFunctionContextHisNestingObjectHisSuperObject() {
    	ObjectDOS parent = environment.createNewObject();
    	parent.setFunction(symbol, aFunction);
    	
    	ObjectDOS nesting = environment.createNewObject();
    	nesting.setParent(parent);
    	
    	context.setContext(nesting);
    	
    	ExecutableDOS actualFunction = new NewspeakLookupStrategy().lookupFunction(context, symbol);
    	
    	assertThat(actualFunction, is(aFunction));
    }

    /* Inside a function context making a call to nesting object's nesting object
     * ie this is an instance function calling a function on the current classes containing object 
     */
    @Test
    public void shouldFindFunctionInFunctionContextHisNestingObjectHisNestingObject() {
    	ObjectDOS outside = environment.createNewObject();
    	outside.setFunction(symbol, aFunction);
    	
    	ObjectDOS nesting = environment.createNewObject();
    	nesting.setContext(outside);
    	
    	context.setContext(nesting);
    	
    	ExecutableDOS actualFunction = new NewspeakLookupStrategy().lookupFunction(context, symbol);
    	
    	assertThat(actualFunction, is(aFunction));
    }
    
    /* Should work its way up context before looking at super objects
     */
    @Test
    public void shouldFindFunctionInNestingContextBeforeSuperObject() {
    	ObjectDOS outside = environment.createNewObject();
    	outside.setFunction(symbol, aFunction);
    	
    	ObjectDOS parent = environment.createNewObject();
    	parent.setFunction(symbol, anotherFunction);
    	
    	ObjectDOS nesting = environment.createNewObject();
    	nesting.setContext(outside);
    	nesting.setParent(parent);
    	
    	context.setContext(nesting);
    	
    	ExecutableDOS actualFunction = new NewspeakLookupStrategy().lookupFunction(context, symbol);
    	
    	assertThat(actualFunction, is(aFunction));
    }
    
    /* Shouldn't look at the super objects of nesting object contexts...
     */
    @Test
    public void shouldFindFunctionInSuperObjectBeforeNestingObjectHisSuperObject() {
    	ObjectDOS outsideParent = environment.createNewObject();
    	outsideParent.setFunction(symbol, anotherFunction);
    	
    	ObjectDOS outside = environment.createNewObject();
    	outside.setParent(outsideParent);
    	
    	ObjectDOS parent = environment.createNewObject();
    	parent.setFunction(symbol, aFunction);
    	
    	ObjectDOS nesting = environment.createNewObject();
    	nesting.setContext(outside);
    	nesting.setParent(parent);
    	
    	context.setContext(nesting);
    	
    	ExecutableDOS actualFunction = new NewspeakLookupStrategy().lookupFunction(context, symbol);
    	
    	assertThat(actualFunction, is(aFunction));
    }
}
