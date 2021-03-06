package org.dynamos.compiler;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class TransformStringToASTTest {

	private TransformStringToAST transformer;

	@Before
	public void setUp() {
		transformer = new TransformStringToAST();
	}

	@Test
	public void shouldCreateConstructorNode() {
		StatementContainingNode root = transformer.transform(
			"(constructor object-WithParam: param1 andParam?: param2 \n " +
			") \n"
		);

		ConstructorNode node = (ConstructorNode) root.getStatements().get(0);

		assertThat(node.getName(), is("object-WithParam:andParam?:"));
		assertThat(node.getArguments().get(0), is("param1"));
		assertThat(node.getArguments().get(1), is("param2"));
	}

	@Test
	public void shouldCreatePrivateConstructorNode() {
		StatementContainingNode root = transformer.transform(
			"(private-constructor object\n " +
			") \n"
		);

		PrivateConstructorNode node = (PrivateConstructorNode) root.getStatements().get(0);
	}

	@Test
	public void shouldIgnoreComments() {
		StatementContainingNode root = transformer.transform(
			"(constructor object-WithParam: param1 andParam?: param2 // comment 1\n" +
			"  // comment 2\n" +
			") \n "
		);

		ConstructorNode node = (ConstructorNode) root.getStatements().get(0);

		assertThat(node.getStatements().size(), is(0));
	}

	@Test
	public void shouldCreateFunctionCall() {
		StatementContainingNode root = transformer.transform(
			"functionCall\n"
		);

		ChainedNode call = ((ChainedNode) root.getStatements().get(0));

		assertThat(call.getName(), is("functionCall"));
	}

	@Test
	public void shouldCreateFunctionCallWithSlotParameters() {
		StatementContainingNode root = transformer.transform(
			"function-WithParam: $param1 andParam?: $param2\n"
		);

		FunctionCallNode call = ((FunctionCallNode) root.getStatements().get(0));

		assertThat(call.getName(), is("function-WithParam:andParam?:"));

		SymbolGetterNode param1Symbol = (SymbolGetterNode) call.getArguments().get(0);
		assertThat(param1Symbol.getName(), is("param1"));

		SymbolGetterNode param2Symbol = (SymbolGetterNode) call.getArguments().get(1);
		assertThat(param2Symbol.getName(), is("param2"));
	}

	@Test
	public void shouldCreateNestedFunctionCall() {
		StatementContainingNode root = transformer.transform(
			"function-WithParam: function1 andParam?: (function2: $param1)\n"
		);

		FunctionCallNode call = ((FunctionCallNode) root.getStatements().get(0));

		assertThat(call.getName(), is("function-WithParam:andParam?:"));
		assertThat(((ChainedNode) call.getArguments().get(0)).getName(), is("function1"));
		assertThat(((ChainedNode) call.getArguments().get(1)).getName(), is("function2:"));
		SymbolGetterNode param1Symbol = (SymbolGetterNode) ((FunctionCallNode) call.getArguments().get(1)).getArguments().get(0);
		assertThat( param1Symbol.getName(), is("param1"));
	}

	@Test
	public void shouldChainParameterlessCalls() {
		StatementContainingNode root = transformer.transform(
			"accessor1 accessor2 function1: (function2: $param1) // a comment, just to see\n"
		);

		ChainedNode call = ((ChainedNode) root.getStatements().get(0));

		assertThat(call.getName(), is("accessor1"));
		assertThat(call.getChain().getName(), is("accessor2"));
		assertThat(call.getChain().getChain().getName(), is("function1:"));
		assertThat( ((ChainedNode) call.getChain().getChain().getArguments().get(0)).getName(), is("function2:"));
		assertThat( ((SymbolGetterNode) ((FunctionCallNode) call.getChain().getChain().getArguments().get(0)).getArguments().get(0)).getName(), is("param1"));
	}

	@Test
	public void shouldChainSlotGetter() {
		StatementContainingNode root = transformer.transform(
			"$slotName functionName\n"
		);

		SymbolGetterNode call = ((SymbolGetterNode) root.getStatements().get(0));

		assertThat(call.getName(), is("slotName"));
		assertThat(call.getChain().getName(), is("functionName"));
	}

	@Test
	public void shouldChainSlotGetterInSubCall() {
		StatementContainingNode root = transformer.transform(
			"doSomething: ($slotName functionName)\n"
		);

		FunctionCallNode call = (FunctionCallNode) root.getStatements().get(0);
		SymbolGetterNode slotName = (SymbolGetterNode) call.getArguments().get(0);

		assertThat(slotName.getName(), is("slotName"));
		assertThat(slotName.getChain().getName(), is("functionName"));
	}

	@Test
	public void shouldSetSlot() {
		StatementContainingNode root = transformer.transform(
			"$slotName: (functionName: param1 and: $param2)\n"
		);

		SymbolSetterNode call = ((SymbolSetterNode) root.getStatements().get(0));

		assertThat(call.getName(), is("slotName"));
		assertThat(((FunctionCallNode) call.getArguments().get(0)).getName(), is("functionName:and:"));
	}

	@Test
	public void shouldCreateClosure() {
		StatementContainingNode root = transformer.transform(
			"functionWithClosure: [param1, param2| ]\n"
		);

		FunctionCallNode call = ((FunctionCallNode) root.getStatements().get(0));
		AnonymousFunctionNode closure = ((AnonymousFunctionNode) call.getArguments().get(0));

		assertThat( closure.getArguments().get(0), is("param1"));
		assertThat( closure.getArguments().get(1), is("param2"));
	}

	@Test
	public void shouldCreateClosureWithSimpleBody() {
		StatementContainingNode root = transformer.transform(
			"functionWithClosure: [| dosomething: param ]\n"
		);

		FunctionCallNode call = ((FunctionCallNode) root.getStatements().get(0));
		AnonymousFunctionNode closure = ((AnonymousFunctionNode) call.getArguments().get(0));

		FunctionCallNode firstStatement = (FunctionCallNode) closure.getStatements().get(0);
		assertThat( firstStatement.getName(), is("dosomething:"));
		assertThat( ((FunctionCallNode) firstStatement.getArguments().get(0)).getName(), is("param"));
	}

	@Test
	public void shouldCreateClosureWithStatement() {
		StatementContainingNode root = transformer.transform(
			"(constructor test\n" +
			"  functionWithClosure: [param1, param2| functioncall1: param1\n" +
			"    functionCall2\n" +
			"    functionCall3: param1 anotherParam: param2\n" +
			"  ]\n" +
			")"
		);

		ConstructorNode node = (ConstructorNode) root.getStatements().get(0);

		FunctionCallNode call = ((FunctionCallNode) node.getStatements().get(0));
		AnonymousFunctionNode closure = ((AnonymousFunctionNode) call.getArguments().get(0));

		assertThat( closure.getStatements().size(), is(3));
	}

	@Test
	public void shouldDefineValueObject() {
		StatementContainingNode root = transformer.transform(
			"function-WithParam: .2345\n"
		);

		FunctionCallNode call = ((FunctionCallNode) root.getStatements().get(0));

		assertThat(((ValueNode) call.getArguments().get(0)).getValue(), is(2345));
	}

	@Test
	public void shouldDefineNumberConstant() {
		StatementContainingNode root = transformer.transform(
			"function-WithParam: #1234\n"
		);

		FunctionCallNode call = ((FunctionCallNode) root.getStatements().get(0));

		assertThat(((NumberNode) call.getArguments().get(0)).getValue(), is(1234));
	}

	@Test
	public void shouldDefineStringConstant() {
		StatementContainingNode root = transformer.transform(
			"function-WithParam: 'a string'\n"
		);

		FunctionCallNode call = ((FunctionCallNode) root.getStatements().get(0));

		assertThat(((StringNode) call.getArguments().get(0)).getValue(), is("a string"));
	}

	@Test
	public void shouldAddLocalToContext() {
		StatementContainingNode root = transformer.transform(
			"(constructor test\n" +
			"  (slot aLocal)\n" +
			")"
		);

		ConstructorNode node = (ConstructorNode) root.getStatements().get(0);

		List<SymbolGetterNode> slots = node.getSlots();

		assertThat(slots.get(0).getName(), is("aLocal"));
	}

	@Test
	public void shouldCreateNestedFunction() {
		StatementContainingNode root = transformer.transform(
			"(constructor test\n" +
			"  (method nestedFunction: param1\n" +
			"    result: $param1\n" +
			"  )\n" +
			")"
		);

		ConstructorNode node = (ConstructorNode) root.getStatements().get(0);
		NamedMethodNode nested = ((NamedMethodNode) node.getStatements().get(0));

		assertThat(nested.getName(), is("nestedFunction:"));
		assertThat(nested.getArguments().get(0), is("param1"));

		FunctionCallNode call = (FunctionCallNode) nested.getStatements().get(0);
		assertThat(call.getName(), is("result:"));
		assertThat( ((SymbolGetterNode) call.getArguments().get(0)).getName(), is("param1"));
	}

	@Test
	public void shouldCreateNestedPrivateFunction() {
		StatementContainingNode root = transformer.transform(
			"(constructor test\n" +
			"  (function nestedFunction\n" +
			"  )\n" +
			")"
		);

		ConstructorNode node = (ConstructorNode) root.getStatements().get(0);
		NamedFunctionNode nested = ((NamedFunctionNode) node.getStatements().get(0));
	}

	@Test
	public void shouldDefineObjectConstructorWithinAnotherConstructor() {
		StatementContainingNode root = transformer.transform(
				"(constructor test\n" +
				"  (constructor aLocal\n" +
				"    (method afunc: theParam\n" +
				"      result: $theParam\n" +
				"    )\n" +
				"  )\n" +
				")"
			);

		ConstructorNode node = (ConstructorNode) root.getStatements().get(0);

		ConstructorNode open = (ConstructorNode) node.getStatements().get(0);
		assertThat(open.getName(), is("aLocal"));

		NamedMethodNode fnode = (NamedMethodNode) open.getStatements().get(0);
		assertThat(fnode.getName(), is("afunc:"));
		assertThat(fnode.getArguments().get(0), is("theParam"));

		FunctionCallNode call = (FunctionCallNode) fnode.getStatements().get(0);
		assertThat(call.getName(), is("result:"));
		assertThat(((SymbolGetterNode) call.getArguments().get(0)).getName(), is("theParam"));
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
		StatementContainingNode node = transformer.transform(
				"(constructor numberFactoryConstructor: vm\n" +
				"  \n" +
				"  (private-constructor numberPrototypeConstructor\n" +
				"    (method plus: number\n" +
				"      result: (vm add: number to: this)\n" +
				"    )\n" +
				"    \n" +
				"    (method minus: number\n" +
				"      result: (vm subtract: number from: this)\n" +
				"    )\n" +
				"    \n" +
				"    (method isLessThan: number\n" +
				"      result: (vm value: this isLessThan: number)\n" +
				"    )\n" +
				"  )\n" +
				"  numberPrototype: (numberPrototypeConstructor execute)\n" +
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
