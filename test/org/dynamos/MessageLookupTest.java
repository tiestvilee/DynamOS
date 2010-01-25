package org.dynamos;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dynamos.structures.Activation;
import org.dynamos.structures.ExecutableDOS;
import org.dynamos.structures.ObjectDOS;
import org.dynamos.structures.OpCode;
import org.dynamos.structures.Symbol;
import org.dynamos.types.NumberDOS.ValueObject;
import org.junit.Before;
import org.junit.Test;


public class MessageLookupTest {

	private static final int END_OF_OBJ1_CONSTRUCTOR = 1;
	private static final int END_OF_SHELL = 1;

	Symbol obj0 = Symbol.get("obj0");
	Symbol obj0slot = Symbol.get("obj0slot");
	Symbol obj0message = Symbol.get("obj0message");
	Symbol obj0setter = Symbol.get("obj0setter");

	Symbol obj1 = Symbol.get("obj1");
	Symbol obj1slot = Symbol.get("obj1slot");
	Symbol obj1message = Symbol.get("obj1message");
	
	Symbol obj1custom = Symbol.get("obj1custom");
	
	Symbol listFactory = Symbol.get("listFactory");
	Symbol emptyList = Symbol.get("emptyList");
	Symbol argumentList = Symbol.get("argumentList");

	Symbol obj1ConstructorSymbol = Symbol.get("obj1WithParent:listFactory:");
	
	
	
	private OpCodeInterpreter interpreter;
	private List<OpCode> opCodeList;
	private List<OpCode> opCodeShellList;
	private ObjectDOS object0;
	


	@Before
	public void setUp() {
		interpreter = new OpCodeInterpreter();
		opCodeList = setupBasicObj1ConstructorOpCodes();
		opCodeShellList = setupBasicShellOpCodes();
	}
	
	@Test
	public void executes() {
		ObjectDOS result = executeOpCodes();
		assertThat(((ValueObject)result.getSlot(Symbol.RESULT).getSlot(obj1slot)).getValue(), is(1234));
	}

	@Test
	public void obj1MessageCanAccessObj1Slot() {
		List<OpCode> customOpCodes = new ArrayList<OpCode>();
		Collections.addAll(customOpCodes,
				new OpCode.SetObject(Symbol.RESULT),
				new OpCode.FunctionCall(obj1message)
			);
		updateOpcodeList(opCodeShellList, END_OF_SHELL, customOpCodes);
		
		ObjectDOS result = executeOpCodes();
		assertThat(((ValueObject)result.getSlot(Symbol.RESULT).getSlot(obj1slot)).getValue(), is(2345));
	}

	@Test
	public void obj0MessageCanAccessObj0Slot() {
		List<OpCode> customOpCodes = new ArrayList<OpCode>();
		Collections.addAll(customOpCodes,
				new OpCode.SetObject(Symbol.RESULT),
				new OpCode.FunctionCall(obj0message)
			);
		updateOpcodeList(opCodeShellList, END_OF_SHELL, customOpCodes);
		
		ObjectDOS result = executeOpCodes();
		assertThat(((ValueObject)result.getSlot(Symbol.RESULT)).getValue(), is(6789));
	}

	@Test
	public void obj1CanAccessSlot0() {
		updateShellToCallCustomMethod();
		
		List<OpCode> customMessageOpCodes = new ArrayList<OpCode>();
		Collections.addAll(customMessageOpCodes, 
			new OpCode.PushSymbol(obj0slot),
			new OpCode.FunctionCall(Symbol.GET_SLOT_$)
		);
		
		setupCustomMessage(customMessageOpCodes);
		
		ObjectDOS result = executeOpCodes();
		assertThat(((ValueObject)result.getSlot(Symbol.RESULT)).getValue(), is(6789));
	}

	@Test
	public void obj1CanSetSlot0OnObj1() {
		updateShellToCallCustomMethod();
		
		List<OpCode> customMessageOpCodes = new ArrayList<OpCode>();
		Collections.addAll(customMessageOpCodes,
			new OpCode.CreateValueObject(8901),
			new OpCode.PushSymbol(obj0slot),
			new OpCode.Push(Symbol.RESULT),
			new OpCode.FunctionCall(Symbol.SET_SLOT_$_TO_$),
			new OpCode.PushSymbol(Symbol.THIS),
			new OpCode.FunctionCall(Symbol.GET_SLOT_$)
		);
		
		setupCustomMessage(customMessageOpCodes);
		
		ObjectDOS result = executeOpCodes();
		
		assertThat(((ValueObject)object0.getSlot(obj0slot)).getValue(), is(6789));
		assertThat(((ValueObject)result.getSlot(Symbol.RESULT).getSlot(obj0slot)).getValue(), is(8901));
	}

	@Test
	public void obj0CanSetSlot0OnObj1() {
		updateShellToCallCustomMethod();
		
		List<OpCode> customMessageOpCodes = new ArrayList<OpCode>();
		Collections.addAll(customMessageOpCodes,
			new OpCode.FunctionCall(obj0setter)
		);
		
		setupCustomMessage(customMessageOpCodes);
		
		ObjectDOS result = executeOpCodes();
		
		assertThat(((ValueObject)object0.getSlot(obj0slot)).getValue(), is(6789));
		assertThat(((ValueObject)result.getSlot(Symbol.RESULT).getSlot(obj0slot)).getValue(), is(9012));
	}

	@Test
	public void functionCanShadowExistingSlot() {
		updateShellToCallCustomMethod();
		
		List<OpCode> customMessageOpCodes = new ArrayList<OpCode>();
		Collections.addAll(customMessageOpCodes,
			new OpCode.CreateValueObject(3456),
			new OpCode.PushSymbol(obj0slot),
			new OpCode.Push(Symbol.RESULT),
			new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),
			new OpCode.PushSymbol(obj1slot),
			new OpCode.Push(obj0slot),
			new OpCode.FunctionCall(Symbol.SET_SLOT_$_TO_$)
		);
		
		setupCustomMessage(customMessageOpCodes);
		
		ObjectDOS result = executeOpCodes();
		
		assertThat(((ValueObject)object0.getSlot(obj0slot)).getValue(), is(6789));
		assertThat(((ValueObject)result.getSlot(Symbol.RESULT).getSlot(obj0slot)).getValue(), is(6789));
		assertThat(((ValueObject)result.getSlot(Symbol.RESULT).getSlot(obj1slot)).getValue(), is(3456));
	}

	@Test
	public void functionCannotSetObjectSlotDirectly() {
		updateShellToCallCustomMethod();
		
		List<OpCode> customMessageOpCodes = new ArrayList<OpCode>();
		Collections.addAll(customMessageOpCodes,
			new OpCode.CreateValueObject(4567),
			new OpCode.PushSymbol(obj1slot),
			new OpCode.Push(Symbol.RESULT),
			new OpCode.SetObject(Symbol.CURRENT_CONTEXT),
			new OpCode.FunctionCall(Symbol.SET_SLOT_$_TO_$)
		);
		
		setupCustomMessage(customMessageOpCodes);
		
		try {
			ObjectDOS result = executeOpCodes();
			
			assertThat(((ValueObject)result.getSlot(Symbol.RESULT).getSlot(obj1slot)).getValue(), is(1234));

			fail("Shouldn't be allowed to assign slots on other objects");
		} catch (Exception e) {
			System.out.println(e);
			// pass
		}
		
	}

	@Test
	public void functionCannotSetObjectLocalSlotDirectly() {
		updateShellToCallCustomMethod();
		
		List<OpCode> customMessageOpCodes = new ArrayList<OpCode>();
		Collections.addAll(customMessageOpCodes,
			new OpCode.CreateValueObject(3456),
			new OpCode.PushSymbol(obj1slot),
			new OpCode.Push(Symbol.RESULT),
			new OpCode.SetObject(Symbol.THIS),
			new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$)
		);
		
		setupCustomMessage(customMessageOpCodes);
		
		try {
			executeOpCodes();
			fail("Shouldn't be allowed to assign slots on other objects");
		} catch (Exception e) {
			// pass
		}
		
	}

	private void setupCustomMessage(List<OpCode> customMessageOpCodes) {
		List<OpCode> customOpCodes = new ArrayList<OpCode>();
		Collections.addAll(customOpCodes,
				// set up obj1slot accessor
	        	new OpCode.PushSymbol(emptyList), // empty symbol list
	        	new OpCode.FunctionCall(Symbol.GET_SLOT_$),
	        	new OpCode.PushSymbol(argumentList),
	        	new OpCode.Push(Symbol.RESULT),
	        	new OpCode.FunctionCall(Symbol.SET_SLOT_$_TO_$),
	        	
	        	new OpCode.StartOpCodeList());
		customOpCodes.addAll(customMessageOpCodes);
       	Collections.addAll(customOpCodes,
       			new OpCode.EndOpCodeList(),
	        	
	        	new OpCode.Push(argumentList),
	        	new OpCode.Push(Symbol.RESULT),
	        	new OpCode.FunctionCall(Symbol.CREATE_FUNCTION_WITH_ARGUMENTS_$_OPCODES_$),
	            
	            new OpCode.PushSymbol(obj1custom),
	            new OpCode.Push(Symbol.RESULT),
	            new OpCode.FunctionCall(Symbol.SET_LOCAL_FUNCTION_$_TO_$)
			);
		System.out.println("before append " + opCodeList.size());
		updateOpcodeList(opCodeList, END_OF_OBJ1_CONSTRUCTOR, customOpCodes);
		System.out.println("after " + opCodeList.size() + "\n" + opCodeList);
	}

	private void updateShellToCallCustomMethod() {
		List<OpCode> customOpCodes = new ArrayList<OpCode>();
		Collections.addAll(customOpCodes,
				new OpCode.SetObject(Symbol.RESULT),
				new OpCode.FunctionCall(obj1custom)
			);
		updateOpcodeList(opCodeShellList, END_OF_SHELL, customOpCodes);
	}


	private void updateOpcodeList(List<OpCode> opcodes, int debugIndex, List<OpCode> customOpCodes) {
		int i, debugsFound = 0;
		for(i=0; i<opcodes.size() && debugIndex > debugsFound; i++) {
			if(opcodes.get(i) instanceof OpCode.Debug) {
				debugsFound++;
			}
		}
		
		opcodes.addAll(i-1, customOpCodes);
	}
	
	private ObjectDOS executeOpCodes() {
		ExecutableDOS obj1Constructor = createObj1Constructor(opCodeList);
		object0 = createSuperObject0();

		Activation shell = interpreter.newActivation();
		shell.setSlot(obj0, object0);
		shell.setSlot(listFactory, interpreter.getEnvironment().getEmptyList());
		shell.setFunction(obj1ConstructorSymbol, obj1Constructor);

		OpCode[] shellOpCodes = opCodeShellList.toArray(new OpCode[0]);
		interpreter.interpret(shell, shellOpCodes);
		
		return shell;
	}

	private List<OpCode> setupBasicShellOpCodes() {
		List<OpCode> opCodes = new ArrayList<OpCode>();
		Collections.addAll(opCodes,
	        	new OpCode.Push(obj0),
	        	new OpCode.Push(listFactory),
	        	new OpCode.FunctionCall(obj1ConstructorSymbol),
	        	
				new OpCode.Debug("returning from shell", Symbol.RESULT)
	        );
		return opCodes;
	}

	private ObjectDOS createSuperObject0() {
		ObjectDOS newObject0 = interpreter.newObject();
		newObject0.setSlot(obj0slot, new ValueObject(6789));
		newObject0.setFunction(obj0message, interpreter.getEnvironment().createFunction(
				new Symbol[] {}, 
				new OpCode[] {
					new OpCode.PushSymbol(obj0slot),
					new OpCode.FunctionCall(Symbol.GET_SLOT_$)
				}));
		newObject0.setFunction(obj0setter, interpreter.getEnvironment().createFunction(
				new Symbol[] {}, 
				new OpCode[] {
					new OpCode.CreateValueObject(9012),
					new OpCode.PushSymbol(obj0slot),
					new OpCode.Push(Symbol.RESULT),
					new OpCode.FunctionCall(Symbol.SET_SLOT_$_TO_$),
					new OpCode.Debug("what's this?", Symbol.THIS)
				}));
		return newObject0;
	}

	private ExecutableDOS createObj1Constructor(List<OpCode> opCodesList) {
		OpCode[] opCodes = opCodesList.toArray(new OpCode[0]);
		return interpreter.getEnvironment().createConstructor(new Symbol[] {obj0, listFactory}, opCodes);
	}

	private List<OpCode> setupBasicObj1ConstructorOpCodes() {
		List<OpCode> opCodes = new ArrayList<OpCode>();
		Collections.addAll(opCodes,
			// set parent
			new OpCode.Push(obj0),
			new OpCode.FunctionCall(Symbol.PARENT_$),
				
			// set up obj1slot
			new OpCode.CreateValueObject(1234),
			new OpCode.PushSymbol(obj1slot),
			new OpCode.Push(Symbol.RESULT),
			new OpCode.FunctionCall(Symbol.SET_SLOT_$_TO_$),
			
			// set up obj1message
        	new OpCode.PushSymbol(emptyList), // empty symbol list
        	new OpCode.FunctionCall(Symbol.GET_SLOT_$),
        	new OpCode.PushSymbol(argumentList),
        	new OpCode.Push(Symbol.RESULT),
        	new OpCode.FunctionCall(Symbol.SET_SLOT_$_TO_$),
        	
        	new OpCode.StartOpCodeList(),
        		new OpCode.CreateValueObject(2345),
				new OpCode.PushSymbol(obj1slot),
				new OpCode.Push(Symbol.RESULT),
				new OpCode.FunctionCall(Symbol.SET_SLOT_$_TO_$),
        	new OpCode.EndOpCodeList(),
        	
        	new OpCode.Push(argumentList),
        	new OpCode.Push(Symbol.RESULT),
        	new OpCode.FunctionCall(Symbol.CREATE_FUNCTION_WITH_ARGUMENTS_$_OPCODES_$),
            
            new OpCode.PushSymbol(obj1message),
            new OpCode.Push(Symbol.RESULT),
            new OpCode.FunctionCall(Symbol.SET_LOCAL_FUNCTION_$_TO_$),
            
			// set up obj1slot accessor
        	new OpCode.PushSymbol(emptyList), // empty symbol list
        	new OpCode.FunctionCall(Symbol.GET_SLOT_$),
        	new OpCode.PushSymbol(argumentList),
        	new OpCode.Push(Symbol.RESULT),
        	new OpCode.FunctionCall(Symbol.SET_SLOT_$_TO_$),
        	
        	new OpCode.StartOpCodeList(),
				new OpCode.PushSymbol(obj1slot),
				new OpCode.FunctionCall(Symbol.GET_SLOT_$),
        	new OpCode.EndOpCodeList(),
        	
        	new OpCode.Push(argumentList),
        	new OpCode.Push(Symbol.RESULT),
        	new OpCode.FunctionCall(Symbol.CREATE_FUNCTION_WITH_ARGUMENTS_$_OPCODES_$),
            
            new OpCode.PushSymbol(obj1slot),
            new OpCode.Push(Symbol.RESULT),
            new OpCode.FunctionCall(Symbol.SET_LOCAL_FUNCTION_$_TO_$),
            
			new OpCode.Debug("returning from obj1 constructor", Symbol.RESULT)
        	);
		return opCodes;
	}
}
