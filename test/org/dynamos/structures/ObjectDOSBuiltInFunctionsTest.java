package org.dynamos.structures;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;

import org.dynamos.Environment;
import org.dynamos.OpCodeInterpreter;
import org.dynamos.types.NumberDOS.ValueObject;
import org.junit.Before;
import org.junit.Test;


public class ObjectDOSBuiltInFunctionsTest {

	ObjectDOS vm;
	ObjectDOS object;
	ObjectDOS value;
	Symbol local = Symbol.get("local");
	private OpCodeInterpreter interpreter;
	private Environment environment;
	private Activation context;
	
	@Before
	public void setUp() {
		interpreter = new OpCodeInterpreter();
		environment = interpreter.getEnvironment();
		vm = VMObjectDOS.getVMObject(environment);
		object = environment.createNewObject();
		value = environment.createNewObject();
    	context = interpreter.newActivation();
    	context.setVictim(environment.createNewObject());
	}
    
    @Test
    public void shouldSetSlotInCurrentFunction() {
    	FunctionDOS function = createFunctionThatSetsAndReturnsValueToLocal();

    	ObjectDOS result = function.execute(interpreter, object, new ArrayList<ObjectDOS>());
    	
    	assertThat(((ValueObject) result).getValue(), is(1234));
    }

    @Test
    public void shouldSetSlotInCurrentContextualFunction() {
    	
    	FunctionWithContext function = environment.createFunctionWithContext(
    			createFunctionThatSetsAndReturnsValueToLocal(),
    			context);

    	ObjectDOS result = function.execute(interpreter, object, new ArrayList<ObjectDOS>());
    	
    	assertThat(((ValueObject) result).getValue(), is(1234));
    	assertThat(context.getSlot(local), is(environment.getUndefined()));
    }
    
    @Test
    public void shouldSetSlotInFunctionsContext() {
    	context.setSlot(local, environment.getUndefined());
    	
    	FunctionWithContext function = createFunctionWithContextThatSetsLocalToValue(context);
    	function.execute(interpreter, object, new ArrayList<ObjectDOS>());
    	
    	assertThat(((ValueObject) context.getSlot(local)).getValue(), is(1234));
    }
    
    @Test
    public void shouldSetSlotInObject() {
    	object.setSlot(local, environment.getUndefined());

    	FunctionWithContext function = createFunctionWithContextThatSetsLocalToValue(context);
    	function.execute(interpreter, object, new ArrayList<ObjectDOS>());

    	assertThat(context.getSlot(local), is(environment.getUndefined()));
    	assertThat(((ValueObject) object.getSlot(local)).getValue(), is(1234));
    }

	private FunctionWithContext createFunctionWithContextThatSetsLocalToValue(Activation localContext) {
		FunctionWithContext function = environment.createFunctionWithContext(
    			new Symbol[] {},
    			new OpCode[] {
        			new OpCode.CreateValueObject(1234),
    				new OpCode.PushSymbol(local),
    				new OpCode.Push(Symbol.RESULT),
    				new OpCode.FunctionCall(Symbol.SET_SLOT_$_TO_$)
    			},
    			localContext);
		return function;
	}
    
	private FunctionDOS createFunctionThatSetsAndReturnsValueToLocal() {
		FunctionDOS function = environment.createFunction(
    			new Symbol[] {},
    			new OpCode[] {
        			new OpCode.CreateValueObject(1234),
    				new OpCode.PushSymbol(local),
    				new OpCode.Push(Symbol.RESULT),
    				new OpCode.FunctionCall(Symbol.SET_SLOT_$_TO_$),
    				new OpCode.PushSymbol(local),
    				new OpCode.FunctionCall(Symbol.GET_SLOT_$)
    			});
		return function;
	}
    
//    @Test
//    public void shouldSetSlotInParentContextIfDefinedThere() {
//    	ObjectDOS parentContext = interpreter.newContext();
//    	parentContext.setSlot(local, interpreter.getEnvironment().createNewObject());
//    	
//    	context.setContext(parentContext);
//    	
//    	arguments.add(new SymbolWrapper(local));
//    	arguments.add(object);
//    	context.getFunction(Symbol.SET_SLOT_$_TO_$).execute(context, arguments);
//    	
//    	assertThat(parentContext.getSlot(local), is(object));
//    	assertThat(context.getSlot(local), is(object));  // goes up to parent...
//    }
}
