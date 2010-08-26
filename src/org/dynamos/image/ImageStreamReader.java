package org.dynamos.image;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.dynamos.image.analysis.DepthFirstAdapter;
import org.dynamos.image.lexer.Lexer;
import org.dynamos.image.lexer.LexerException;
import org.dynamos.image.node.AFunctionDefinition;
import org.dynamos.image.node.AObjectDefinition;
import org.dynamos.image.node.AOpcode;
import org.dynamos.image.node.ARequiredParameter;
import org.dynamos.image.node.ASymbolOpcodeParameter;
import org.dynamos.image.node.AValueOpcodeParameter;
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
		
		List<OpCode> opCodes = new ArrayList<OpCode>();
		List<Symbol> requiredArgs = new ArrayList<Symbol>();
		Symbol latestSymbol = null;
		int latestValue = 0;
		
		@Override
		public void inARequiredParameter(ARequiredParameter node) {
			requiredArgs.add(Symbol.get(node.getId().getText()));
		}
		
		@Override
		public void inASymbolOpcodeParameter(ASymbolOpcodeParameter node) {
			latestSymbol = Symbol.get(node.getId().getText());
		}
		
		@Override
		public void inAValueOpcodeParameter(AValueOpcodeParameter node) {
			latestValue = Integer.parseInt(node.getNumber().getText());
		}

		@Override
		public void outAOpcode(AOpcode node) {
			OpCode result = null;
			
			switch(Integer.parseInt(node.getNumber().getText().toString())) {
				case 1:
					result = new OpCode.FunctionCall(latestSymbol);
					break;
				case 2:
					result = new OpCode.SetObject(latestSymbol);
					break;
				case 3:
					result = new OpCode.Push(latestSymbol);
					break;
				case 4:
					result = new OpCode.PushSymbol(latestSymbol);
					break;
				case 5:
					result = new OpCode.CreateValueObject(latestValue);
					break;
				case 6:
					result = new OpCode.StartOpCodeList();
					break;
				case 7:
					result = new OpCode.EndOpCodeList();
					break;
			}
			latestSymbol = null;
			
			opCodes.add(result);
		}
		
		@Override
		public void outAFunctionDefinition(AFunctionDefinition node) {
			FunctionDOS function = new FunctionDOS(requiredArgs.toArray(new Symbol[0]), opCodes.toArray(new OpCode[0]));
			currentContext.peek().setFunction(Symbol.get(node.getId().getText()), function );
			opCodes = new ArrayList<OpCode>();
			requiredArgs = new ArrayList<Symbol>();
		}
	}

}
