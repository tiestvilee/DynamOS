package org.dynamos.compiler;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.dynamos.structures.FunctionDOS;
import org.dynamos.structures.MetaVM;
import org.dynamos.structures.Mirror;
import org.dynamos.structures.OpCode;
import org.dynamos.structures.Symbol;
import org.junit.Before;
import org.junit.Test;

public class CompileAstToOpCodes_MethodCreation_Test {

	private TransformStringToAST transformer;
	private CompileASTToOpCodes compiler;

	@Before
	public void setUp() {
		transformer = new TransformStringToAST();
		compiler = new CompileASTToOpCodes();
	}

	@Test
	public void shouldCreateEmptyPublicMethod() {
		StatementContainingNode root = transformer.transform(
				"(constructor anObject\n" +
                "    (method functionName\n" +
                "    )\n" +
				")\n"
			);

		FunctionDOS function = compiler.compile(root);
		assertThat(function.getOpCodes(), is(new OpCode[] {
            	new OpCode.PushSymbol(Symbol.get("emptyList")),
            	new OpCode.FunctionCall(Symbol.GET_SLOT_$),

            	new OpCode.PushSymbol(Symbol.get("__argument_list")),
            	new OpCode.Push(Symbol.RESULT),
            	new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),

            	new OpCode.StartOpCodeList(), // constructor body

                    new OpCode.FunctionCall(MetaVM.NEW_OBJECT), // create object

                    new OpCode.Push(Symbol.get("__object")), // save object
                    new OpCode.Push(Symbol.RESULT),
                    new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),

                    new OpCode.Push(Symbol.get("__object")), // get mirror for object
                    new OpCode.FunctionCall(MetaVM.MIRROR_FOR_$),

                    new OpCode.Push(Symbol.get("__mirror")), // save into mirror
                    new OpCode.Push(Symbol.RESULT),
                    new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),

                    // This is where work will be done.
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
                    new OpCode.SetObject(Symbol.get("__mirror")),
                    new OpCode.FunctionCall(Mirror.SET_FUNCTION_$_TO_$),

					new OpCode.PushSymbol(Symbol.get("__object")),  // return object
					new OpCode.FunctionCall(Symbol.GET_SLOT_$),
            	new OpCode.EndOpCodeList(),

            	new OpCode.Push(Symbol.get("__argument_list")), // create constructor
            	new OpCode.Push(Symbol.RESULT),
            	new OpCode.FunctionCall(MetaVM.CREATE_FUNCTION_WITH_ARGUMENTS_$_OPCODES_$),

            	new OpCode.PushSymbol(Symbol.get("anObject")), // save into functions
	            new OpCode.Push(Symbol.RESULT),
                new OpCode.SetObject(Symbol.get("__mirror")),
	        	new OpCode.FunctionCall(Mirror.SET_FUNCTION_$_TO_$)
		}));
	}

	@Test
	public void shouldCreateSimpleMethodWithNoArguments() {
		StatementContainingNode root = transformer.transform(
				"(constructor anObject\n" +
				"  (method functionName\n" +
				"    function-WithParam: $param1 andParam?: $param2\n" +
                "  )\n" +
				")\n"
			);

		FunctionDOS function = compiler.compile(root);
		assertThat(function.getOpCodes(), is(new OpCode[] {

            	new OpCode.PushSymbol(Symbol.get("emptyList")),
            	new OpCode.FunctionCall(Symbol.GET_SLOT_$),

            	new OpCode.PushSymbol(Symbol.get("__argument_list")),
            	new OpCode.Push(Symbol.RESULT),
            	new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),

            	new OpCode.StartOpCodeList(), // constructor body

                    new OpCode.FunctionCall(MetaVM.NEW_OBJECT), // create object

                    new OpCode.Push(Symbol.get("__object")), // save object
                    new OpCode.Push(Symbol.RESULT),
                    new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),

                    new OpCode.Push(Symbol.get("__object")), // get mirror for object
                    new OpCode.FunctionCall(MetaVM.MIRROR_FOR_$),

                    new OpCode.Push(Symbol.get("__mirror")), // save into mirror
                    new OpCode.Push(Symbol.RESULT),
                    new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),

                    // This is where work will be done.
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
                    new OpCode.SetObject(Symbol.get("__mirror")),
                    new OpCode.FunctionCall(Mirror.SET_FUNCTION_$_TO_$),

					new OpCode.PushSymbol(Symbol.get("__object")),  // return object
					new OpCode.FunctionCall(Symbol.GET_SLOT_$),
            	new OpCode.EndOpCodeList(),

            	new OpCode.Push(Symbol.get("__argument_list")), // create constructor
            	new OpCode.Push(Symbol.RESULT),
            	new OpCode.FunctionCall(MetaVM.CREATE_FUNCTION_WITH_ARGUMENTS_$_OPCODES_$),

            	new OpCode.PushSymbol(Symbol.get("anObject")), // save into functions
	            new OpCode.Push(Symbol.RESULT),
                new OpCode.SetObject(Symbol.get("__mirror")),
	        	new OpCode.FunctionCall(Mirror.SET_FUNCTION_$_TO_$)
		}));
	}

	@Test
	public void shouldCreateEmptyMethodWithArguments() {
		StatementContainingNode root = transformer.transform(
				"(constructor anObject\n" +
				"  (method functionName: param1 requires: param2\n" +
                "  )\n" +
				")\n"
			);

		FunctionDOS function = compiler.compile(root);
		assertThat(function.getOpCodes(), is(new OpCode[] {

            	new OpCode.PushSymbol(Symbol.get("emptyList")),
            	new OpCode.FunctionCall(Symbol.GET_SLOT_$),

            	new OpCode.PushSymbol(Symbol.get("__argument_list")),
            	new OpCode.Push(Symbol.RESULT),
            	new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),

            	new OpCode.StartOpCodeList(), // constructor body

                    new OpCode.FunctionCall(MetaVM.NEW_OBJECT), // create object

                    new OpCode.Push(Symbol.get("__object")), // save object
                    new OpCode.Push(Symbol.RESULT),
                    new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),

                    new OpCode.Push(Symbol.get("__object")), // get mirror for object
                    new OpCode.FunctionCall(MetaVM.MIRROR_FOR_$),

                    new OpCode.Push(Symbol.get("__mirror")), // save into mirror
                    new OpCode.Push(Symbol.RESULT),
                    new OpCode.FunctionCall(Symbol.SET_LOCAL_SLOT_$_TO_$),

                    // This is where work will be done.
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
                    new OpCode.SetObject(Symbol.get("__mirror")),
                    new OpCode.FunctionCall(Mirror.SET_FUNCTION_$_TO_$),

					new OpCode.PushSymbol(Symbol.get("__object")),  // return object
					new OpCode.FunctionCall(Symbol.GET_SLOT_$),
            	new OpCode.EndOpCodeList(),

            	new OpCode.Push(Symbol.get("__argument_list")), // create constructor
            	new OpCode.Push(Symbol.RESULT),
            	new OpCode.FunctionCall(MetaVM.CREATE_FUNCTION_WITH_ARGUMENTS_$_OPCODES_$),

            	new OpCode.PushSymbol(Symbol.get("anObject")), // save into functions
	            new OpCode.Push(Symbol.RESULT),
                new OpCode.SetObject(Symbol.get("__mirror")),
	        	new OpCode.FunctionCall(Mirror.SET_FUNCTION_$_TO_$)
		}));
	}

}