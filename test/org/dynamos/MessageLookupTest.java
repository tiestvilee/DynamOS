package org.dynamos;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dynamos.structures.ExecutableDOS;
import org.dynamos.structures.FunctionWithContext;
import org.dynamos.structures.FunctionDOS;
import org.dynamos.structures.ObjectDOS;
import org.dynamos.structures.OpCode;
import org.dynamos.structures.StandardObjects;
import org.dynamos.structures.Symbol;
import org.dynamos.structures.StandardObjects.ValueObject;
import org.junit.Before;
import org.junit.Test;


public class MessageLookupTest {

	private static final int END_OF_OBJ1_CONSTRUCTOR = 1;
	private static final int END_OF_FUNC0_DEFINITION = 2;
	
	
	Symbol func0 = Symbol.get("func0");
	Symbol func0slot = Symbol.get("func0slot");
	Symbol func0message = Symbol.get("func0message");

	Symbol obj0 = Symbol.get("obj0");
	Symbol obj0slot = Symbol.get("obj0slot");
	Symbol obj0message = Symbol.get("obj0message");

	Symbol obj1 = Symbol.get("obj1");
	Symbol obj1slot = Symbol.get("obj1slot");
	Symbol obj1message = Symbol.get("obj1message");
	
	Symbol listFactory = Symbol.get("listFactory");
	Symbol newList = Symbol.get("newList");
	Symbol argumentList = Symbol.get("argumentList");

	Symbol libraryDefn = Symbol.get("libraryDefn:listFactory:");
	
	
	
	private OpCodeInterpreter interpreter;
	private ExecutableDOS obj0messageMock;
	private List<OpCode> opCodeList;
	private List<OpCode> opCodeShellList;
	


	@Before
	public void setUp() {
		interpreter = new OpCodeInterpreter();
		obj0messageMock = mock(ExecutableDOS.class);
//		opCodeList = setupBasicLibraryOpCodes();
//		opCodeShellList = setupBasicShellOpCodes();
	}
//	
//	@Test
//	public void executes() {
//		ObjectDOS result = executeOpCodes(opCodeList, opCodeShellList);
//		assertThat(((ValueObject)result.getSlot(Symbol.RESULT)).getValue(), is(1234));
//	}
//
//	@Test
//	public void func0CanUpdateSlotInFunc0() {
//		List<OpCode> customOpCodes = new ArrayList<OpCode>();
//		Collections.addAll(customOpCodes,
//				new OpCode.Debug("about to update slot", func0slot),
//				new OpCode.CreateValueObject(interpreter, 9999),
//				new OpCode.PushSymbol(func0slot),
//				new OpCode.Push(Symbol.RESULT),
//				new OpCode.FunctionCall(Symbol.SET_SLOT_$_TO_$)
//		);
//		
//		updateOpCodes(customOpCodes, END_OF_FUNC0_DEFINITION);
//		
//		ObjectDOS result = executeOpCodes(opCodeList, opCodeShellList);
//		
//		assertThat(((ValueObject)result.getSlot(Symbol.RESULT)).getValue(), is(9999));
//	}
//
//	@Test
//	public void obj1CanUpdateSlotInFunc0() {
//		List<OpCode> customOpCodes = new ArrayList<OpCode>();
//		Collections.addAll(customOpCodes,
//				new OpCode.Debug("about to update containing slot", func0slot),
//				new OpCode.CreateValueObject(interpreter, 9999),
//				new OpCode.PushSymbol(func0slot),
//				new OpCode.Push(Symbol.RESULT),
//				new OpCode.FunctionCall(Symbol.SET_SLOT_$_TO_$)
//		);
//		
//		updateOpCodes(customOpCodes, END_OF_OBJ1_CONSTRUCTOR);
//		
//		ObjectDOS result = executeOpCodes(opCodeList, opCodeShellList);
//		
//		assertThat(((ValueObject)result.getSlot(Symbol.RESULT)).getValue(), is(9999));
//	}
//
//	@Test
//	public void func0CanCallOwnMessage() {
//		List<OpCode> customOpCodes = new ArrayList<OpCode>();
//		Collections.addAll(customOpCodes,
//				new OpCode.Debug("about to call func0message", func0slot),
//				new OpCode.FunctionCall(func0message)
//		);
//		
//		updateOpCodes(customOpCodes, END_OF_FUNC0_DEFINITION);
//		
//		ObjectDOS result = executeOpCodes(opCodeList, opCodeShellList);
//		
//		assertThat(((ValueObject)result.getSlot(Symbol.RESULT)).getValue(), is(2345));
//	}
//
//	@Test
//	public void obj1CanCallFunc0Message() {
//		List<OpCode> customOpCodes = new ArrayList<OpCode>();
//		Collections.addAll(customOpCodes,
//				new OpCode.Debug("about to call func0message", func0slot),
//				new OpCode.FunctionCall(func0message)
//		);
//		
//		updateOpCodes(customOpCodes, END_OF_OBJ1_CONSTRUCTOR);
//		
//		ObjectDOS result = executeOpCodes(opCodeList, opCodeShellList);
//		
//		assertThat(((ValueObject)result.getSlot(Symbol.RESULT)).getValue(), is(2345));
//	}
//
//	@Test
//	public void obj1CanCallObj1Message() {
//		List<OpCode> customOpCodes = new ArrayList<OpCode>();
//		Collections.addAll(customOpCodes,
//				new OpCode.Debug("about to call obj1message", func0slot),
//				new OpCode.FunctionCall(obj1message)
//		);
//		
//		updateOpCodes(customOpCodes, END_OF_OBJ1_CONSTRUCTOR);
//		
//		ObjectDOS result = executeOpCodes(opCodeList, opCodeShellList);
//		
//		assertThat(((ValueObject)result.getSlot(Symbol.RESULT)).getValue(), is(4567));
//	}
//
//	@Test
//	public void func0CanCallObj1Message() {
//		List<OpCode> customOpCodes = new ArrayList<OpCode>();
//		Collections.addAll(customOpCodes,
//				new OpCode.Debug("about to call obj1message", func0slot),
//				new OpCode.SetObject(Symbol.RESULT),
//				new OpCode.FunctionCall(obj1message)
//		);
//		
//		updateOpCodes(customOpCodes, END_OF_FUNC0_DEFINITION);
//		
//		ObjectDOS result = executeOpCodes(opCodeList, opCodeShellList);
//		
//		assertThat(((ValueObject)result.getSlot(Symbol.RESULT)).getValue(), is(4567));
//	}
//
//	@Test
//	public void func0CanCallObj0Message() {
//		List<OpCode> customOpCodes = new ArrayList<OpCode>();
//		Collections.addAll(customOpCodes,
//				new OpCode.Debug("about to call obj0message", Symbol.RESULT),
//				new OpCode.SetObject(Symbol.RESULT),
//				new OpCode.FunctionCall(obj0message)
//		);
//		
//		updateOpCodes(customOpCodes, END_OF_FUNC0_DEFINITION);
//		
//		ObjectDOS result = executeOpCodes(opCodeList, opCodeShellList);
//		
//		assertThat(((ValueObject)result.getSlot(Symbol.RESULT)).getValue(), is(6789));
//	}
//
//	private void updateOpCodes(List<OpCode> customOpCodes, int debugIndex) {
//		int i;
//		for(i=0; i<opCodeList.size() && debugIndex > 0; i++) {
//			if(opCodeList.get(i) instanceof OpCode.Debug) {
//				debugIndex--;
//			}
//		}
//		
//		opCodeList.addAll(i-1, customOpCodes);
//	}
//
//	private ObjectDOS executeOpCodes(List<OpCode> opCodeList, List<OpCode> opCodeShellList) {
//		FunctionDOS library = createLibrary(opCodeList);
//		ObjectDOS object0 = createSuperObject0();
//
//		ObjectDOS shell = interpreter.newContext();
//		shell.setSlot(obj0, object0);
//		shell.setSlot(listFactory, interpreter.getEnvironment().getListFactory());
//		shell.setFunction(libraryDefn, library);
//
//		OpCode[] shellOpCodes = opCodeShellList.toArray(new OpCode[0]);
//		interpreter.interpret(shell, shellOpCodes);
//		
//		return shell;
//	}
//
//	private List<OpCode> setupBasicShellOpCodes() {
//		List<OpCode> opCodeShellList = new ArrayList<OpCode>();
//		Collections.addAll(opCodeShellList,
//	        	new OpCode.Push(obj0),
//	        	new OpCode.Push(listFactory),
//	        	new OpCode.FunctionCall(libraryDefn),
//	        	
//				new OpCode.Debug("returning from shell", Symbol.RESULT)
//	        );
//		return opCodeShellList;
//	}
//
//	private ObjectDOS createSuperObject0() {
//		ObjectDOS object0 = new ObjectDOS();
//		object0.setSlot(obj0slot, new StandardObjects.ValueObject(6789));
//		object0.setFunction(obj0message, interpreter.getEnvironment().createFunction(
//				new Symbol[] {}, 
//				new OpCode[] {
//					new OpCode.PushSymbol(obj0slot),
//					new OpCode.FunctionCall(Symbol.GET_SLOT_$) 
//					// TODO shit.  this needs to access the object version if not already defined on child.
//					// a different kind of GETter?
//				}, 
//				object0));
//		return object0;
//	}
//
//	private FunctionDOS createLibrary(List<OpCode> opCodeList) {
//		ObjectDOS libraryContext = interpreter.newContext();
//		OpCode[] opCodes = opCodeList.toArray(new OpCode[0]);
//		return new FunctionDOS(new FunctionDefinitionDOS(interpreter, new Symbol[] {obj0, listFactory}, opCodes), libraryContext);
//	}
//
//	private List<OpCode> setupBasicLibraryOpCodes() {
//		List<OpCode> opCodeList = new ArrayList<OpCode>();
//		Collections.addAll(opCodeList,
//				
//			// set up func0slot
//			new OpCode.CreateValueObject(interpreter, 1234),
//			new OpCode.PushSymbol(func0slot),
//			new OpCode.Push(Symbol.RESULT),
//			new OpCode.FunctionCall(Symbol.SET_SLOT_$_TO_$),
//			
//			// set up func0message
//        	new OpCode.SetObject(listFactory), // empty symbol list
//        	new OpCode.FunctionCall(newList),
//        	new OpCode.PushSymbol(argumentList),
//        	new OpCode.Push(Symbol.RESULT),
//        	new OpCode.FunctionCall(Symbol.SET_SLOT_$_TO_$),
//        	
//        	new OpCode.StartOpCodeList(),
//        		new OpCode.CreateValueObject(interpreter, 2345),
//				new OpCode.PushSymbol(func0slot),
//				new OpCode.Push(Symbol.RESULT),
//				new OpCode.FunctionCall(Symbol.SET_SLOT_$_TO_$),
//        	new OpCode.EndOpCodeList(),
//        	
//        	new OpCode.Push(argumentList),
//        	new OpCode.Push(Symbol.RESULT),
//        	new OpCode.FunctionCall(Symbol.CREATE_FUNCTION_WITH_ARGUMENTS_$_OPCODES_$),
//        	
//            new OpCode.Push(Symbol.RESULT),
//            new OpCode.Push(Symbol.CURRENT_CONTEXT),
//            new OpCode.FunctionCall(Symbol.CONTEXTUALIZE_FUNCTION_$_IN_$),
//            
//            new OpCode.PushSymbol(func0message),
//            new OpCode.Push(Symbol.RESULT),
//            new OpCode.FunctionCall(Symbol.SET_FUNCTION_$_TO_$),
//            
//            
//			// set up obj1
//        	new OpCode.SetObject(listFactory), // empty symbol list
//        	new OpCode.FunctionCall(newList),
//        	new OpCode.PushSymbol(argumentList),
//        	new OpCode.Push(Symbol.RESULT),
//        	new OpCode.FunctionCall(Symbol.SET_SLOT_$_TO_$),
//
//           	new OpCode.StartOpCodeList(), // defining the constructor
//	           	// set up obj1slot
//	           	new OpCode.CreateValueObject(interpreter, 3456),
//	           	new OpCode.PushSymbol(obj1slot),
//	           	new OpCode.Push(Symbol.RESULT),
//	           	new OpCode.FunctionCall(Symbol.SET_SLOT_$_TO_$),
//	           	
//				// assign parent
//				new OpCode.Push(obj0),
//				new OpCode.FunctionCall(Symbol.SET_PARENT_$),
//           	
//				// set up obj1message
//            	new OpCode.SetObject(listFactory), // empty symbol list
//            	new OpCode.FunctionCall(newList),
//            	new OpCode.PushSymbol(argumentList),
//            	new OpCode.Push(Symbol.RESULT),
//            	new OpCode.FunctionCall(Symbol.SET_SLOT_$_TO_$),
//            	
//            	new OpCode.StartOpCodeList(),
//	        		new OpCode.CreateValueObject(interpreter, 4567),
//					new OpCode.PushSymbol(func0slot),
//					new OpCode.Push(Symbol.RESULT),
//					new OpCode.FunctionCall(Symbol.SET_SLOT_$_TO_$),
//            	new OpCode.EndOpCodeList(),
//            	
//            	new OpCode.Push(argumentList),
//            	new OpCode.Push(Symbol.RESULT),
//            	new OpCode.FunctionCall(Symbol.CREATE_FUNCTION_WITH_ARGUMENTS_$_OPCODES_$),
//            	
//	            new OpCode.Push(Symbol.RESULT),
//	            new OpCode.Push(Symbol.CURRENT_CONTEXT),
//	            new OpCode.FunctionCall(Symbol.CONTEXTUALIZE_FUNCTION_$_IN_$),
//	            
//	            new OpCode.PushSymbol(obj1message),
//	            new OpCode.Push(Symbol.RESULT),
//	            new OpCode.FunctionCall(Symbol.SET_FUNCTION_$_TO_$),
//	            
//				new OpCode.Debug("code that runs at end of obj1 constructor", Symbol.PARENT),
//	            
//            new OpCode.EndOpCodeList(),
//            	
//        	new OpCode.Push(argumentList),
//        	new OpCode.Push(Symbol.RESULT),
//            new OpCode.Push(Symbol.CURRENT_CONTEXT),
//        	new OpCode.FunctionCall(Symbol.CREATE_CONSTRUCTOR_WITH_ARGUMENTS_$_OPCODES_$_IN_$),
//        	
//        	new OpCode.SetObject(Symbol.RESULT),
//        	new OpCode.FunctionCall(Symbol.EXECUTE),
//        	
//			new OpCode.Debug("code that runs at end of outside function", Symbol.RESULT),
//		
//    		new OpCode.PushSymbol(func0slot),
//            new OpCode.FunctionCall(Symbol.GET_SLOT_$),
//        	
//			new OpCode.Debug("returning from lib def", Symbol.RESULT)
//        	);
//		return opCodeList;
//	}
}
