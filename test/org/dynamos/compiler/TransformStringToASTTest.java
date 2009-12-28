package org.dynamos.compiler;

import java.util.List;

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
			"(object object-WithParam: param1 andParam?: param2 \n " +
			") \n "
		);
		
		assertThat(((ConstructorNode) root).getName(), is("object-WithParam:andParam?:"));
		assertThat(((ConstructorNode) root).getArguments().get(0), is("param1"));
		assertThat(((ConstructorNode) root).getArguments().get(1), is("param2"));
	}
	
	@Test
	public void shouldIgnoreComments() {
		ASTNode root = transformer.transform(
			"(object object-WithParam: param1 andParam?: param2 // comment 1\n" +
			"  // comment 2\n" +
			") \n "
		);
		
		assertThat(((ConstructorNode) root).getStatements().size(), is(0));
	}
	
	@Test
	public void shouldCreateFunctionCall() {
		ASTNode root = transformer.transform(
			"(object test\n" +
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
			"(object test\n" +
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
			"(object test\n" +
			"  accessor1 accessor2 function1: (function2: param1) // a comment, just to see\n" +
			")"
		);
		
		FunctionCallNode call = ((FunctionCallNode) ((StatementContainingNode) root).getStatements().get(0));

		assertThat(call.getName(), is("accessor1"));
		assertThat(call.getChain().getName(), is("accessor2"));
		assertThat(call.getChain().getChain().getName(), is("function1:"));
		assertThat( ((FunctionCallNode) call.getChain().getChain().getArguments().get(0)).getName(), is("function2:"));
		assertThat( ((SymbolNode) ((FunctionCallNode) call.getChain().getChain().getArguments().get(0)).getArguments().get(0)).getName(), is("param1"));
	}
	
	@Test
	public void shouldCreateClosure() {
		ASTNode root = transformer.transform(
			"(object test\n" +
			"  functionWithClosure: [param1, param2| ]\n" +
			")"
		);
		
		FunctionCallNode call = ((FunctionCallNode) ((StatementContainingNode) root).getStatements().get(0));
		ClosureNode closure = ((ClosureNode) call.getArguments().get(0));

		assertThat( closure.getArguments().get(0), is("param1"));
		assertThat( closure.getArguments().get(1), is("param2"));
	}
	
	@Test
	public void shouldCreateClosureWithStatement() {
		ASTNode root = transformer.transform(
			"(object test\n" +
			"  functionWithClosure: [param1, param2| functioncall1: param1\n" +
			"    functionCall2\n" +
			"    functionCall3: param1 anotherParam: param2\n" +
			"  ]\n" +
			")"
		);
		
		FunctionCallNode call = ((FunctionCallNode) ((StatementContainingNode) root).getStatements().get(0));
		ClosureNode closure = ((ClosureNode) call.getArguments().get(0));

		assertThat( closure.getStatements().size(), is(3));
	}
	
	@Test
	public void shouldDefineNumberConstant() {
		ASTNode root = transformer.transform(
			"(object test\n" +
			"  function-WithParam: #1234\n" +
			")"
		);
		
		FunctionCallNode call = ((FunctionCallNode) ((StatementContainingNode) root).getStatements().get(0));

		assertThat(((NumberNode) call.getArguments().get(0)).getValue(), is(1234));
	}
	
	@Test
	public void shouldDefineStringConstant() {
		ASTNode root = transformer.transform(
			"(object test\n" +
			"  function-WithParam: 'a string'\n" +
			")"
		);
		
		FunctionCallNode call = ((FunctionCallNode) ((StatementContainingNode) root).getStatements().get(0));

		assertThat(((StringNode) call.getArguments().get(0)).getValue(), is("a string"));
	}
	
	@Test
	public void shouldAddLocalToContext() {
		ASTNode root = transformer.transform(
			"(object test\n" +
			"  (slot aLocal)\n" +
			")"
		);
		
		List<SymbolNode> slots = ((ConstructorNode) root).getSlots();
		
		assertThat(slots.get(0).getName(), is("aLocal"));
	}
	
	@Test
	public void shouldCreateNestedFunction() {
		ASTNode root = transformer.transform(
			"(object test\n" +
			"  (function nestedFunction: param1\n" +
			"    result: param1\n" +
			"  )\n" +
			")"
		);
		
		FunctionNode nested = ((FunctionNode) ((StatementContainingNode) root).getStatements().get(0));

		assertThat(nested.getName(), is("nestedFunction:"));
		assertThat(nested.getArguments().get(0), is("param1"));
		
		FunctionCallNode call = (FunctionCallNode) nested.getStatements().get(0);
		assertThat(call.getName(), is("result:"));
		assertThat( ((SymbolNode) call.getArguments().get(0)).getName(), is("param1"));
	}
	
	@Test
	public void shouldOpenObjectForModifying() {
		ASTNode root = transformer.transform(
				"(object test\n" +
				"  (object aLocal\n" +
				"    (function afunc: theParam\n" +
				"      result: theParam\n" +
				"    )\n" +
				"  )\n" +
				")"
			);
			
		ConstructorNode open = (ConstructorNode) ((ConstructorNode) root).getStatements().get(0);
		assertThat(open.getName(), is("aLocal"));
		
		FunctionNode fnode = (FunctionNode) open.getStatements().get(0);
		assertThat(fnode.getName(), is("afunc:"));
		assertThat(fnode.getArguments().get(0), is("theParam"));
		
		FunctionCallNode call = (FunctionCallNode) fnode.getStatements().get(0);
		assertThat(call.getName(), is("result:"));
		assertThat(((SymbolNode) call.getArguments().get(0)).getName(), is("theParam"));
	}
//	
//	
////	public void shouldTranslateFibonacciSuccessfully() {
////		transformer.transform(
////				"(function mathFactory: vm\n"
////		);
////	}

	@Test
	public void shouldTranslateNumberLibrarySuccessfully() {
		ASTNode node = transformer.transform(
				"(object numberFactory: vm\n" + 
				"  \n" + 
				"  (object numberPrototype\n" + 
				"    (function plus: number\n" + 
				"      result: (vm add: number to: this)\n" + 
				"    )\n" + 
				"    \n" + 
				"    (function minus: number\n" + 
				"      result: (vm subtract: number from: this)\n" + 
				"    )\n" + 
				"    \n" + 
				"    (function isLessThan: number\n" + 
				"      result: (vm value: this isLessThan: number)\n" + 
				"    )\n" + 
				"  )\n" + 
				"  \n" + 
				"  (function numberFrom: value\n" + 
				"     value parent: numberPrototype\n" + 
				"  )\n" + 
				"  \n" +
				") \n "
			);
		System.out.println(node);
	}
}
