package org.dynamos.compiler;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.dynamos.structures.FunctionDOS;
import org.dynamos.structures.OpCode;
import org.dynamos.structures.Symbol;
import org.junit.Before;
import org.junit.Test;

public class CompileAstToOpCodesTest {

	private TransformStringToAST transformer;
	private CompileASTToOpCodes compiler;

	@Before
	public void setUp() {
		transformer = new TransformStringToAST();
		compiler = new CompileASTToOpCodes();
	}
	
	@Test
	public void shouldCreateFunctionCall() {
		StatementContainingNode root = transformer.transform(
				"function-WithParam: $param1 andParam?: $param2\n"
			);
		
		FunctionDOS function = compiler.compile(root);
		assertThat(function.getOpCodes(), is(new OpCode[] {
				new OpCode.PushSymbol(Symbol.get("param1")),
				new OpCode.FunctionCall(Symbol.GET_SLOT_$),
				
				new OpCode.PushSymbol(Symbol.get("__temp1")),
				new OpCode.Push(Symbol.RESULT),
				new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),
				
				new OpCode.PushSymbol(Symbol.get("param2")),
				new OpCode.FunctionCall(Symbol.GET_SLOT_$),
				
				new OpCode.PushSymbol(Symbol.get("__temp2")),
				new OpCode.Push(Symbol.RESULT),
				new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),
				
				new OpCode.Push(Symbol.get("__temp1")),
				new OpCode.Push(Symbol.get("__temp2")),
				new OpCode.FunctionCall(Symbol.get("function-WithParam:andParam?:"))				
		}));
	}
	
	@Test
	public void shouldCreateChainedFunctionCall() {
		StatementContainingNode root = transformer.transform(
				"object1 object2 function-WithParam: $param1 andParam?: $param2\n"
			);
		
		FunctionDOS function = compiler.compile(root);
		assertThat(function.getOpCodes(), is(new OpCode[] {
				new OpCode.FunctionCall(Symbol.get("object1")),
				new OpCode.PushSymbol(Symbol.get("__temp1")),
				new OpCode.Push(Symbol.RESULT),
				new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),
				
				new OpCode.SetObject(Symbol.get("__temp1")),
				new OpCode.FunctionCall(Symbol.get("object2")),
				new OpCode.PushSymbol(Symbol.get("__temp2")),
				new OpCode.Push(Symbol.RESULT),
				new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),
				
				new OpCode.PushSymbol(Symbol.get("param1")),
				new OpCode.FunctionCall(Symbol.GET_SLOT_$),
				
				new OpCode.PushSymbol(Symbol.get("__temp3")),
				new OpCode.Push(Symbol.RESULT),
				new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),
				
				new OpCode.PushSymbol(Symbol.get("param2")),
				new OpCode.FunctionCall(Symbol.GET_SLOT_$),
				
				new OpCode.PushSymbol(Symbol.get("__temp4")),
				new OpCode.Push(Symbol.RESULT),
				new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),
				
				new OpCode.Push(Symbol.get("__temp3")),
				new OpCode.Push(Symbol.get("__temp4")),
				new OpCode.SetObject(Symbol.get("__temp2")),
				new OpCode.FunctionCall(Symbol.get("function-WithParam:andParam?:"))				
		}));
	}
	
	@Test
	public void shouldCreateChainedFunctionCallStartingWithSlot() {
		StatementContainingNode root = transformer.transform(
				"$slot object1 function-WithParam: $param1 andParam?: $param2\n"
			);
		
		FunctionDOS function = compiler.compile(root);
		assertThat(function.getOpCodes(), is(new OpCode[] {
				new OpCode.PushSymbol(Symbol.get("slot")), 
				new OpCode.FunctionCall(Symbol.GET_SLOT_$),
				
				new OpCode.PushSymbol(Symbol.get("__temp1")),
				new OpCode.Push(Symbol.RESULT),
				new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),
				
				new OpCode.SetObject(Symbol.get("__temp1")),
				new OpCode.FunctionCall(Symbol.get("object1")),
				
				new OpCode.PushSymbol(Symbol.get("__temp2")),
				new OpCode.Push(Symbol.RESULT),
				new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),
				
				new OpCode.PushSymbol(Symbol.get("param1")),  // param1
				new OpCode.FunctionCall(Symbol.GET_SLOT_$),
				
				new OpCode.PushSymbol(Symbol.get("__temp3")),
				new OpCode.Push(Symbol.RESULT),
				new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),
				
				new OpCode.PushSymbol(Symbol.get("param2")), // param2
				new OpCode.FunctionCall(Symbol.GET_SLOT_$),
				
				new OpCode.PushSymbol(Symbol.get("__temp4")),
				new OpCode.Push(Symbol.RESULT),
				new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),
				
				new OpCode.Push(Symbol.get("__temp3")),
				new OpCode.Push(Symbol.get("__temp4")),
				new OpCode.SetObject(Symbol.get("__temp2")),
				new OpCode.FunctionCall(Symbol.get("function-WithParam:andParam?:"))				
		}));
	}
	
	@Test
	public void shouldSetFunctionResultToSlot() {
		StatementContainingNode root = transformer.transform(
				"$slot: (function-WithParam: $param1)\n"
			);
		
		FunctionDOS function = compiler.compile(root);
		assertThat(function.getOpCodes(), is(new OpCode[] {
				new OpCode.PushSymbol(Symbol.get("param1")),  // param1
				new OpCode.FunctionCall(Symbol.GET_SLOT_$),
				
				new OpCode.PushSymbol(Symbol.get("__temp1")),
				new OpCode.Push(Symbol.RESULT),
				new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),

				new OpCode.Push(Symbol.get("__temp1")),
				new OpCode.FunctionCall(Symbol.get("function-WithParam:")),
				
				new OpCode.PushSymbol(Symbol.get("slot")),
				new OpCode.Push(Symbol.RESULT),
				new OpCode.FunctionCall(Symbol.SET_SLOT_$_TO_$),
		}));
	}
	
	@Test
	public void shouldSetResultToSlot() {
		StatementContainingNode root = transformer.transform(
				"$slot\n"
			);
		
		FunctionDOS function = compiler.compile(root);
		assertThat(function.getOpCodes(), is(new OpCode[] {
				new OpCode.PushSymbol(Symbol.get("slot")),
				new OpCode.FunctionCall(Symbol.GET_SLOT_$)
		}));
	}
	
	@Test
	public void shouldChainSlotInSubCall() {
		StatementContainingNode root = transformer.transform(
				"$slot1 function: ($slot2 function2)\n"
			);
		
		FunctionDOS function = compiler.compile(root);
		assertThat(function.getOpCodes(), is(new OpCode[] {
				new OpCode.PushSymbol(Symbol.get("slot1")), 
				new OpCode.FunctionCall(Symbol.GET_SLOT_$),
				
				new OpCode.PushSymbol(Symbol.get("__temp1")),
				new OpCode.Push(Symbol.RESULT),
				new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),
				
				new OpCode.PushSymbol(Symbol.get("slot2")), 
				new OpCode.FunctionCall(Symbol.GET_SLOT_$),
				
				new OpCode.PushSymbol(Symbol.get("__temp3")),
				new OpCode.Push(Symbol.RESULT),
				new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),

				new OpCode.SetObject(Symbol.get("__temp3")),
				new OpCode.FunctionCall(Symbol.get("function2")),

				new OpCode.PushSymbol(Symbol.get("__temp2")),
				new OpCode.Push(Symbol.RESULT),
				new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),

				new OpCode.Push(Symbol.get("__temp2")),
				new OpCode.SetObject(Symbol.get("__temp1")),
				new OpCode.FunctionCall(Symbol.get("function:")),				
		}));
	}
	
	@Test
	public void shouldCreateFunctionCallWithSubFunctionCalls() {
		StatementContainingNode root = transformer.transform(
				"function-WithParam: (anotherFunction: $param1) andParam?: (object1 aThirdFunction: (subsubfunction: $param2)) finalParam: $param3\n"
			);
		
		FunctionDOS function = compiler.compile(root);
		assertThat(function.getOpCodes(), is(new OpCode[] {
				new OpCode.PushSymbol(Symbol.get("param1")),
				new OpCode.FunctionCall(Symbol.GET_SLOT_$),
				
				new OpCode.PushSymbol(Symbol.get("__temp2")),
				new OpCode.Push(Symbol.RESULT),
				new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),
				
				new OpCode.Push(Symbol.get("__temp2")),
				new OpCode.FunctionCall(Symbol.get("anotherFunction:")),
				new OpCode.PushSymbol(Symbol.get("__temp1")),
				new OpCode.Push(Symbol.RESULT),
				new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),

				new OpCode.FunctionCall(Symbol.get("object1")),
				new OpCode.PushSymbol(Symbol.get("__temp3")),
				new OpCode.Push(Symbol.RESULT),
				new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),

				new OpCode.PushSymbol(Symbol.get("param2")),
				new OpCode.FunctionCall(Symbol.GET_SLOT_$),
				
				new OpCode.PushSymbol(Symbol.get("__temp5")),
				new OpCode.Push(Symbol.RESULT),
				new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),
				
				new OpCode.Push(Symbol.get("__temp5")),
				new OpCode.FunctionCall(Symbol.get("subsubfunction:")),
				new OpCode.PushSymbol(Symbol.get("__temp4")),
				new OpCode.Push(Symbol.RESULT),
				new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),

				new OpCode.Push(Symbol.get("__temp4")),
				new OpCode.SetObject(Symbol.get("__temp3")),
				new OpCode.FunctionCall(Symbol.get("aThirdFunction:")),
				new OpCode.PushSymbol(Symbol.get("__temp2")),
				new OpCode.Push(Symbol.RESULT),
				new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),
				
				new OpCode.PushSymbol(Symbol.get("param3")),
				new OpCode.FunctionCall(Symbol.GET_SLOT_$),
				
				new OpCode.PushSymbol(Symbol.get("__temp3")),
				new OpCode.Push(Symbol.RESULT),
				new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),
				
				new OpCode.Push(Symbol.get("__temp1")),
				new OpCode.Push(Symbol.get("__temp2")),
				new OpCode.Push(Symbol.get("__temp3")),
				new OpCode.FunctionCall(Symbol.get("function-WithParam:andParam?:finalParam:")),				
		}));
	}
	
	@Test
	public void shouldCreateFunctionCallWithValueObject() {
		StatementContainingNode root = transformer.transform(
				"function-WithParam: .3456\n"
			);
		
		FunctionDOS function = compiler.compile(root);
		assertThat(function.getOpCodes(), is(new OpCode[] {
				new OpCode.CreateValueObject(3456),
				
				new OpCode.PushSymbol(Symbol.get("__temp1")),
				new OpCode.Push(Symbol.RESULT),
				new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),
				
				new OpCode.Push(Symbol.get("__temp1")),
				new OpCode.FunctionCall(Symbol.get("function-WithParam:"))				
		}));
	}
	
	@Test
	public void shouldCreateEmptyPublicFunction() {
		StatementContainingNode root = transformer.transform(
				"(function functionName\n" +
				")\n"
			);
		
		FunctionDOS function = compiler.compile(root);
		assertThat(function.getOpCodes(), is(new OpCode[] {
            	new OpCode.FunctionCall(Symbol.get("newList")),
            	new OpCode.PushSymbol(Symbol.get("__argument_list")),
            	new OpCode.Push(Symbol.RESULT),
            	new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),
            	
            	new OpCode.StartOpCodeList(), // empty function body
            	new OpCode.EndOpCodeList(),
            	
            	new OpCode.Push(Symbol.get("__argument_list")), // create function
            	new OpCode.Push(Symbol.RESULT),
            	new OpCode.FunctionCall(Symbol.CREATE_FUNCTION_WITH_ARGUMENTS_$_OPCODES_$),
	            
            	new OpCode.PushSymbol(Symbol.get("functionName")), // save into functions
	            new OpCode.Push(Symbol.RESULT),
	        	new OpCode.FunctionCall(Symbol.SET_LOCAL_FUNCTION_$_TO_$),
		}));
	}
	
	@Test
	public void shouldCreateEmptyPrivateFunction() {
		StatementContainingNode root = transformer.transform(
				"(private-function functionName\n" +
				")\n"
			);
		
		FunctionDOS function = compiler.compile(root);
		assertThat(function.getOpCodes(), is(new OpCode[] {
            	new OpCode.FunctionCall(Symbol.get("newList")),
            	new OpCode.PushSymbol(Symbol.get("__argument_list")),
            	new OpCode.Push(Symbol.RESULT),
            	new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),
            	
            	new OpCode.StartOpCodeList(), // empty function body
            	new OpCode.EndOpCodeList(),
            	
            	new OpCode.Push(Symbol.get("__argument_list")), // create function
            	new OpCode.Push(Symbol.RESULT),
            	new OpCode.FunctionCall(Symbol.CREATE_FUNCTION_WITH_ARGUMENTS_$_OPCODES_$),
	            
            	new OpCode.PushSymbol(Symbol.get("functionName")), // save into slots
	            new OpCode.Push(Symbol.RESULT),
	        	new OpCode.FunctionCall(Symbol.SET_SLOT_$_TO_$),
		}));
	}
	
	@Test
	public void shouldCreateSimpleFunctionWithNoArguments() {
		StatementContainingNode root = transformer.transform(
				"(function functionName\n" +
				"  function-WithParam: $param1 andParam?: $param2\n" +
				")\n"
			);
		
		FunctionDOS function = compiler.compile(root);
		assertThat(function.getOpCodes(), is(new OpCode[] {

            	new OpCode.FunctionCall(Symbol.get("newList")),
            	new OpCode.PushSymbol(Symbol.get("__argument_list")),
            	new OpCode.Push(Symbol.RESULT),
            	new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),
            	
            	new OpCode.StartOpCodeList(), // function body
					new OpCode.PushSymbol(Symbol.get("param1")),
					new OpCode.FunctionCall(Symbol.GET_SLOT_$),
					
					new OpCode.PushSymbol(Symbol.get("__temp1")),
					new OpCode.Push(Symbol.RESULT),
					new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),

					new OpCode.PushSymbol(Symbol.get("param2")),
					new OpCode.FunctionCall(Symbol.GET_SLOT_$),
					
					new OpCode.PushSymbol(Symbol.get("__temp2")),
					new OpCode.Push(Symbol.RESULT),
					new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),
					
					new OpCode.Push(Symbol.get("__temp1")),
					new OpCode.Push(Symbol.get("__temp2")),
					new OpCode.FunctionCall(Symbol.get("function-WithParam:andParam?:")),		
            	new OpCode.EndOpCodeList(),
            	
            	new OpCode.Push(Symbol.get("__argument_list")), // create function
            	new OpCode.Push(Symbol.RESULT),
            	new OpCode.FunctionCall(Symbol.CREATE_FUNCTION_WITH_ARGUMENTS_$_OPCODES_$),
	            
            	new OpCode.PushSymbol(Symbol.get("functionName")), // save into functions
	            new OpCode.Push(Symbol.RESULT),
	        	new OpCode.FunctionCall(Symbol.SET_LOCAL_FUNCTION_$_TO_$),
		}));
	}
		
	@Test
	public void shouldCreateEmptyFunctionWithArguments() {
		StatementContainingNode root = transformer.transform(
				"(function functionName: param1 requires: param2\n" +
				")\n"
			);
		
		FunctionDOS function = compiler.compile(root);
		assertThat(function.getOpCodes(), is(new OpCode[] {

            	new OpCode.FunctionCall(Symbol.get("newList")),
            	new OpCode.PushSymbol(Symbol.get("__argument_list")),
            	new OpCode.Push(Symbol.RESULT),
            	new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),
            	
            	new OpCode.PushSymbol(Symbol.get("param1")),
            	new OpCode.SetObject(Symbol.get("__argument_list")),
            	new OpCode.FunctionCall(Symbol.get("add:")),
            	
            	new OpCode.PushSymbol(Symbol.get("param2")),
            	new OpCode.SetObject(Symbol.get("__argument_list")),
            	new OpCode.FunctionCall(Symbol.get("add:")),
            	
            	new OpCode.StartOpCodeList(), // empty function body
            	new OpCode.EndOpCodeList(),
            	
            	new OpCode.Push(Symbol.get("__argument_list")), // create function
            	new OpCode.Push(Symbol.RESULT),
            	new OpCode.FunctionCall(Symbol.CREATE_FUNCTION_WITH_ARGUMENTS_$_OPCODES_$),
	            
            	new OpCode.PushSymbol(Symbol.get("functionName:requires:")), // save into functions
	            new OpCode.Push(Symbol.RESULT),
	        	new OpCode.FunctionCall(Symbol.SET_LOCAL_FUNCTION_$_TO_$),
		}));
	}
	
	@Test
	public void shouldCreatePublicConstructorWithArguments() {
		StatementContainingNode root = transformer.transform(
				"(constructor objectName: param1 requires: param2\n" +
				"  function-WithParam: $param1 andParam?: $param2\n" +
				")\n"
			);
		
		FunctionDOS function = compiler.compile(root);
		assertThat(function.getOpCodes(), is(new OpCode[] {

            	new OpCode.FunctionCall(Symbol.get("newList")),
            	new OpCode.PushSymbol(Symbol.get("__argument_list")),
            	new OpCode.Push(Symbol.RESULT),
            	new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),
            	
            	new OpCode.PushSymbol(Symbol.get("param1")),
            	new OpCode.SetObject(Symbol.get("__argument_list")),
            	new OpCode.FunctionCall(Symbol.get("add:")),
            	
            	new OpCode.PushSymbol(Symbol.get("param2")),
            	new OpCode.SetObject(Symbol.get("__argument_list")),
            	new OpCode.FunctionCall(Symbol.get("add:")),
            	
            	new OpCode.StartOpCodeList(), // constructor body
					new OpCode.PushSymbol(Symbol.get("param1")),
					new OpCode.FunctionCall(Symbol.GET_SLOT_$),
					
					new OpCode.PushSymbol(Symbol.get("__temp1")),
					new OpCode.Push(Symbol.RESULT),
					new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),
					
					new OpCode.PushSymbol(Symbol.get("param2")),
					new OpCode.FunctionCall(Symbol.GET_SLOT_$),
					
					new OpCode.PushSymbol(Symbol.get("__temp2")),
					new OpCode.Push(Symbol.RESULT),
					new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),
					
					new OpCode.Push(Symbol.get("__temp1")),
					new OpCode.Push(Symbol.get("__temp2")),
					new OpCode.FunctionCall(Symbol.get("function-WithParam:andParam?:")),		
            	new OpCode.EndOpCodeList(),
            	
            	new OpCode.Push(Symbol.get("__argument_list")), // create constructor
            	new OpCode.Push(Symbol.RESULT),
            	new OpCode.FunctionCall(Symbol.CREATE_CONSTRUCTOR_WITH_ARGUMENTS_$_OPCODES_$),
	            
            	new OpCode.PushSymbol(Symbol.get("objectName:requires:")), // save into functions
	            new OpCode.Push(Symbol.RESULT),
	        	new OpCode.FunctionCall(Symbol.SET_LOCAL_FUNCTION_$_TO_$)
		}));
	}
	
	@Test
	public void shouldCreatePrivateConstructor() {
		StatementContainingNode root = transformer.transform(
				"(private-constructor objectName\n" +
				")\n"
			);
		
		FunctionDOS function = compiler.compile(root);
		assertThat(function.getOpCodes(), is(new OpCode[] {

            	new OpCode.FunctionCall(Symbol.get("newList")),
            	new OpCode.PushSymbol(Symbol.get("__argument_list")),
            	new OpCode.Push(Symbol.RESULT),
            	new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),
            	
            	new OpCode.StartOpCodeList(), // constructor body	
            	new OpCode.EndOpCodeList(),
            	
            	new OpCode.Push(Symbol.get("__argument_list")), // create constructor
            	new OpCode.Push(Symbol.RESULT),
            	new OpCode.FunctionCall(Symbol.CREATE_CONSTRUCTOR_WITH_ARGUMENTS_$_OPCODES_$),
	            
            	new OpCode.PushSymbol(Symbol.get("objectName")), // save into slots
	            new OpCode.Push(Symbol.RESULT),
	        	new OpCode.FunctionCall(Symbol.SET_SLOT_$_TO_$)
		}));
	}

	@Test
	public void shouldCompileNumberLibrary() {
		StatementContainingNode root = transformer.transform(
				"(constructor numberFactoryConstructor: vm and: listFactory\n" + 
				"  \n" + 
				"  (constructor numberPrototypeConstructor: listFactory\n" + 
				"    (function plus: number\n" + 
				"      $result: ($vm add: $number to: $this)\n" + 
				"    )\n" + 
				"    \n" + 
				"    (function minus: number\n" + 
				"      $result: ($vm subtract: $number from: $this)\n" + 
				"    )\n" + 
				"    \n" + 
				"    (function isLessThan: number\n" + 
				"      $result: ($vm value: $this isLessThan: $number)\n" + 
				"    )\n" + 
				"  )\n" +
				"  $numberPrototype: (numberPrototypeConstructor: $listFactory)\n" + 
				"  \n" + 
				"  (function numberFrom: value\n" + 
				"     $value parent: $numberPrototype\n" + 
				"  )\n" + 
				"  \n" +
				") \n "
		);
		
		FunctionDOS function = compiler.compile(root);
		
		OpCode[] opCodes = function.getOpCodes();
		OpCode.printOpCodes(opCodes);
	}

}
