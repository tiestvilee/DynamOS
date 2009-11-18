package org.dynamos.compiler;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class TransformStringToASTTest {

	private TransformStringToAST transformer;

	@Before
	public void setUp() {
		transformer = new TransformStringToAST();
	}
	
	@Test
	public void shouldCreateFunctionNode() {
		ASTNode root = transformer.transform(
			"(function function-WithParam: param1 andParam?: param2 \n " +
			")"
		);
		
		assertThat(((FunctionNode) root).getName(), is("function-WithParam:andParam?:"));
		assertThat(((FunctionNode) root).getArguments().get(0), is("param1"));
		assertThat(((FunctionNode) root).getArguments().get(1), is("param2"));
	}
	
	@Test
	public void shouldCreateFunctionCall() {
		ASTNode root = transformer.transform(
			"(function test\n" +
			"  function-WithParam: param1 andParam?: param2\n" +
			")"
		);
		
		FunctionCallNode call = ((FunctionCallNode) ((StatementContainingNode) root).getStatements().get(0));

		assertThat(call.getName(), is("function-WithParam:andParam?:"));
		assertThat(((SymbolNode) call.getArguments().get(0)).getName(), is("param1"));
		assertThat(((SymbolNode) call.getArguments().get(1)).getName(), is("param2"));
	}
	
	@Test
	public void shouldCreateNestedFunctionCall() {
		ASTNode root = transformer.transform(
			"(function test\n" +
			"  function-WithParam: (function1) andParam?: (function2: param1)\n" +
			")"
		);
		
		FunctionCallNode call = ((FunctionCallNode) ((StatementContainingNode) root).getStatements().get(0));

		assertThat(call.getName(), is("function-WithParam:andParam?:"));
		assertThat(((FunctionCallNode) call.getArguments().get(0)).getName(), is("function1"));
		assertThat(((FunctionCallNode) call.getArguments().get(1)).getName(), is("function2:"));
		assertThat( ((SymbolNode) ((FunctionCallNode) call.getArguments().get(1)).getArguments().get(0)).getName(), is("param1"));
	}
	
	@Test
	public void shouldChainParameterlessCalls() {
		ASTNode root = transformer.transform(
			"(function test\n" +
			"  accessor1 accessor2 function1: (function2: param1)\n" +
			")"
		);
		
		FunctionCallNode call = ((FunctionCallNode) ((StatementContainingNode) root).getStatements().get(0));

		assertThat(call.getName(), is("accessor1"));
		assertThat(call.getChain().getName(), is("accessor2"));
		assertThat(call.getChain().getChain().getName(), is("function1:"));
		assertThat( ((FunctionCallNode) call.getChain().getChain().getArguments().get(0)).getName(), is("function2:"));
		assertThat( ((SymbolNode) ((FunctionCallNode) call.getChain().getChain().getArguments().get(0)).getArguments().get(0)).getName(), is("param1"));
	}
}
