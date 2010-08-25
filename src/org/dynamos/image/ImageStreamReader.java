package org.dynamos.image;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import java.util.Stack;

import org.dynamos.image.analysis.DepthFirstAdapter;
import org.dynamos.image.lexer.Lexer;
import org.dynamos.image.lexer.LexerException;
import org.dynamos.image.node.AFunctionDefinition;
import org.dynamos.image.node.AObjectDefinition;
import org.dynamos.image.node.Start;
import org.dynamos.image.node.TId;
import org.dynamos.image.parser.Parser;
import org.dynamos.image.parser.ParserException;
import org.dynamos.structures.ExecutableDOS;
import org.dynamos.structures.FunctionDOS;
import org.dynamos.structures.ObjectDOS;
import org.dynamos.structures.OpCode;
import org.dynamos.structures.Symbol;

public class ImageStreamReader {

	ParserBuilder imageParserBuilder;

	public ImageStreamReader(ParserBuilder imageParserBuilder) {
		this.imageParserBuilder = imageParserBuilder;
	}
	
	
	public ObjectDOS read(String string) {
		
		Parser imageParser = imageParserBuilder.build(string);
		
		try {
			ObjectDOS wrapper = new ObjectDOS();
			Start topNode = imageParser.parse();
			Interpreter interpreter = new Interpreter(wrapper);
			topNode.apply(interpreter);
			
			return wrapper;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	private static class Interpreter extends DepthFirstAdapter {
		
		Stack<ObjectDOS> currentContext = new Stack<ObjectDOS>();
		
		Interpreter(ObjectDOS item) {
			currentContext.push(item);
		}
		
		@Override
		public void inAObjectDefinition(AObjectDefinition node) {
			ObjectDOS newNode = new ObjectDOS();
			currentContext.peek().setSlot(Symbol.get(node.getId().getText()), newNode);
			currentContext.push(newNode);
		}
		
		@Override
		public void outAObjectDefinition(AObjectDefinition node) {
			currentContext.pop();
		}
		
		@Override
		public void inAFunctionDefinition(AFunctionDefinition node) {
			OpCode[] opCodes = new OpCode[0];
			Symbol[] requiredArgs = new Symbol[0];
			FunctionDOS function = new FunctionDOS(requiredArgs, opCodes);
			currentContext.peek().setFunction(Symbol.get(node.getId().getText()), function );
		}
	}

}
