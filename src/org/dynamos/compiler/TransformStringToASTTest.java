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
			"(function function-WithParam: param1 andParam?: param2\n" +
			")"
		);
		
		assertThat(((FunctionNode) root).getName(), is("function-WithParam:andParam?:"));
		assertThat(((FunctionNode) root).getArguments().get(0), is("param1"));
		assertThat(((FunctionNode) root).getArguments().get(1), is("param2"));
	}
}
