package org.dynamos.compiler;

import org.dynamos.structures.FunctionDOS;
import org.dynamos.structures.MetaVM;
import org.dynamos.structures.OpCode;
import org.dynamos.structures.Symbol;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class CompileAstToOpCodes_FunctionCreation_Test {

	private TransformStringToAST transformer;
	private CompileASTToOpCodes compiler;

	@Before
	public void setUp() {
		transformer = new TransformStringToAST();
		compiler = new CompileASTToOpCodes();
	}

	@Test
	public void shouldCreateEmptyFunction() {
		StatementContainingNode root = transformer.transform(
				"(function functionName\n" +
				")\n"
			);

		FunctionDOS function = compiler.compile(root);
		assertThat(function.getOpCodes(), is(new OpCode[] {
            	new OpCode.PushSymbol(Symbol.get("emptyList")),
            	new OpCode.FunctionCall(Symbol.GET_SLOT_$),
            	new OpCode.PushSymbol(Symbol.get("__argument_list")),
            	new OpCode.Push(Symbol.RESULT),
            	new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),

            	new OpCode.StartOpCodeList(), // empty function body
            	new OpCode.EndOpCodeList(),

            	new OpCode.Push(Symbol.get("__argument_list")), // create function
            	new OpCode.Push(Symbol.RESULT),
            	new OpCode.FunctionCall(MetaVM.CREATE_FUNCTION_WITH_ARGUMENTS_$_OPCODES_$),

            	new OpCode.PushSymbol(Symbol.get("functionName")), // save into functions
	            new OpCode.Push(Symbol.RESULT),
	        	new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),
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

            	new OpCode.PushSymbol(Symbol.get("emptyList")),
            	new OpCode.FunctionCall(Symbol.GET_SLOT_$),
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
            	new OpCode.FunctionCall(MetaVM.CREATE_FUNCTION_WITH_ARGUMENTS_$_OPCODES_$),

            	new OpCode.PushSymbol(Symbol.get("functionName")), // save into functions
	            new OpCode.Push(Symbol.RESULT),
	        	new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),
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

            	new OpCode.PushSymbol(Symbol.get("emptyList")),
            	new OpCode.FunctionCall(Symbol.GET_SLOT_$),

            	new OpCode.PushSymbol(Symbol.get("param2")),
            	new OpCode.SetObject(Symbol.RESULT),
            	new OpCode.FunctionCall(Symbol.get("prepend:")),

            	new OpCode.PushSymbol(Symbol.get("param1")),
            	new OpCode.SetObject(Symbol.RESULT),
            	new OpCode.FunctionCall(Symbol.get("prepend:")),

            	new OpCode.PushSymbol(Symbol.get("__argument_list")),
            	new OpCode.Push(Symbol.RESULT),
            	new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),

            	new OpCode.StartOpCodeList(), // empty function body
            	new OpCode.EndOpCodeList(),

            	new OpCode.Push(Symbol.get("__argument_list")), // create function
            	new OpCode.Push(Symbol.RESULT),
            	new OpCode.FunctionCall(MetaVM.CREATE_FUNCTION_WITH_ARGUMENTS_$_OPCODES_$),

            	new OpCode.PushSymbol(Symbol.get("functionName:requires:")), // save into functions
	            new OpCode.Push(Symbol.RESULT),
	        	new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),
		}));
	}

}