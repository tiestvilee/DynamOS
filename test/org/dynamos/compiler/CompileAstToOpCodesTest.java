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
				"function-WithParam: param1 andParam?: param2\n"
			);
		
		FunctionDOS function = compiler.compile(root);
		assertThat(function.getOpCodes(), is(new OpCode[] {
				new OpCode.Push(Symbol.get("param1")),
				new OpCode.Push(Symbol.get("param2")),
				new OpCode.FunctionCall(Symbol.get("function-WithParam:andParam?:"))				
		}));
	}
	
	@Test
	public void shouldCreateChainedFunctionCall() {
		StatementContainingNode root = transformer.transform(
				"object1 object2 function-WithParam: param1 andParam?: param2\n"
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
				
				new OpCode.Push(Symbol.get("param1")),
				new OpCode.Push(Symbol.get("param2")),
				new OpCode.SetObject(Symbol.get("__temp2")),
				new OpCode.FunctionCall(Symbol.get("function-WithParam:andParam?:"))				
		}));
	}
	
	@Test
	public void shouldCreateFunctionCallWithSubFunctionCalls() {
		StatementContainingNode root = transformer.transform(
				"function-WithParam: (anotherFunction: param1) andParam?: (object1 aThirdFunction: (subsubfunction: param2)) finalParam: param3\n"
			);
		
		FunctionDOS function = compiler.compile(root);
		assertThat(function.getOpCodes(), is(new OpCode[] {
				new OpCode.Push(Symbol.get("param1")),
				new OpCode.FunctionCall(Symbol.get("anotherFunction:")),
				new OpCode.PushSymbol(Symbol.get("__temp1")),
				new OpCode.Push(Symbol.RESULT),
				new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),

				new OpCode.FunctionCall(Symbol.get("object1")),
				new OpCode.PushSymbol(Symbol.get("__temp3")),
				new OpCode.Push(Symbol.RESULT),
				new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),

				new OpCode.Push(Symbol.get("param2")),
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
				
				new OpCode.Push(Symbol.get("__temp1")),
				new OpCode.Push(Symbol.get("__temp2")),
				new OpCode.Push(Symbol.get("param3")),
				new OpCode.FunctionCall(Symbol.get("function-WithParam:andParam?:finalParam:")),				
		}));
	}
	
	// subsub function calls
}
