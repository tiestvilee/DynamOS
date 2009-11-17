package org.dynamos.compiler;

import org.junit.Before;
import org.junit.Test;
import org.dynamos.OpCodeInterpreter;
import org.dynamos.compiler.BootstrapCompiler;
import org.dynamos.structures.Context;
import org.dynamos.structures.FunctionDefinitionDOS;
import org.dynamos.structures.ObjectDOS;
import org.dynamos.structures.OpCode;
import org.dynamos.structures.Symbol;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class BootstrapCompilerTest {
	
	OpCodeInterpreter interpreter;
	private Symbol definitionSymbol = Symbol.get("definition");
	private Symbol expectedParameterSymbol = Symbol.get("expectedParameter");
	
	ObjectDOS expectedParameter;
	
	@Before
	public void setUp() {
		interpreter = new OpCodeInterpreter();
		expectedParameter = new ObjectDOS();
	}

	@Test
	public void shouldReturnParameter() {
		String program =
			"(function test: someParameter\n" +
			//"  result: someParameter\n" +
			")";
			
		FunctionDefinitionDOS definition = new BootstrapCompiler().compile(interpreter, program);
		Context context = interpreter.newContext();
		context.setSlot(definitionSymbol, definition);
		context.setSlot(expectedParameterSymbol, expectedParameter);
		OpCode[] opCodes = new OpCode[] {
        	new OpCode.Push(definitionSymbol),  // contextualise the newly created function
        	new OpCode.Push(Symbol.CURRENT_CONTEXT),
        	new OpCode.FunctionCall(Symbol.CONTEXTUALIZE_FUNCTION_$_IN_$),	  
            new OpCode.Debug("contextualized", Symbol.RESULT),
            
            new OpCode.Push(expectedParameterSymbol),
            new OpCode.SetObject(Symbol.RESULT),
            new OpCode.FunctionCall(Symbol.EXECUTE)
		};
		interpreter.interpret(context, opCodes);
		
		ObjectDOS result = context.getSlot(Symbol.RESULT);
		assertThat(result, is(expectedParameter));
	}
}
