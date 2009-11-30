package org.dynamos.structures;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.dynamos.Environment;
import org.dynamos.OpCodeInterpreter;
import org.junit.Before;
import org.junit.Test;


public class ObjectDOSBuiltInFunctionsTest {

	ObjectDOS vm;
	Context context;
	ObjectDOS object;
	ListDOS arguments;
	Symbol local = Symbol.get("local");
	private OpCodeInterpreter interpreter;
	
	@Before
	public void setUp() {
		interpreter = new OpCodeInterpreter();
		Environment environment = interpreter.getEnvironment();
		vm = VMObjectDOS.getVMObject(environment);
		context = interpreter.newContext();
		object = environment.createNewObject();
		arguments = new ListDOS();
	}
    
    @Test
    public void shouldSetSlot() {
    	arguments.add(new SymbolWrapper(local));
    	arguments.add(object);
    	
    	context.getFunction(Symbol.SET_SLOT_$_TO_$).execute(context, arguments);
    	
    	assertThat(context.getSlot(local), is(object));
    }
    
    @Test
    public void shouldSetSlotInParentContextIfDefinedThere() {
    	Context parentContext = interpreter.newContext();
    	parentContext.setSlot(local, interpreter.getEnvironment().createNewObject());
    	
    	context.setContext(parentContext);
    	
    	arguments.add(new SymbolWrapper(local));
    	arguments.add(object);
    	context.getFunction(Symbol.SET_SLOT_$_TO_$).execute(context, arguments);
    	
    	assertThat(parentContext.getSlot(local), is(object));
    	assertThat(context.getSlot(local), is(object));  // goes up to parent...
    }
}
