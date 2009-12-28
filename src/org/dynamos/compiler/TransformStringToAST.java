package org.dynamos.compiler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransformStringToAST {
	
	/*
	 * FunctionDefinition => Identifier(':' Identifier (FunctionDefinitionChain | ))
	 * FunctionDefinitionChain => Identifier ':' Identifier (FunctionDefinitionChain | ))
	 */
	public ASTNode functionDefinition(Stream stream) {
		FunctionNode fnode = new FunctionNode();
		
		stream.consumeFunctionStart();
		messageDefinition(fnode, stream);
		functionBody(fnode, stream);
		stream.consumeRightBracket();
		stream.consumeNewLine();
		
		return fnode;
	}
	
	public void messageDefinition(MessageNode fnode, Stream stream) {
		
		fnode.appendToName(stream.consumeIdentifier());
		
		if(stream.matchesColon()) {
			do {
				fnode.appendToName(stream.consumeColon());
				fnode.addParameter(stream.consumeIdentifier());
				if(!stream.matchesIdentifier()) {
					break;
				}
				fnode.appendToName(stream.consumeIdentifier());
			} while (true);
		}
		
		stream.consumeNewLine();
	}
	
		/*
		 * 
		 *  FunctionCall => Identifier( ':' ( '(' FunctionCall ')' | Identifier ) | FunctionCall | ) 
		 * 
		 */
	public FunctionCallNode functionCall(Stream stream) {
		FunctionCallNode call = new FunctionCallNode();

		call.appendToName(stream.consumeIdentifier());
		
		if(stream.matchesColon()) {
			do {
				call.appendToName(stream.consumeColon());
				
				functionCallConsumeParameter(stream, call);
				
				if(stream.matchesIdentifier()) {
					call.appendToName(stream.consumeIdentifier());
				} else if (stream.matchesRightBracket()) {
					return call;
				} else {
					stream.consumeNewLine();
					return call;
				}
			} while (true);
		} else if (stream.matchesIdentifier()) {
			call.setChain(functionCall(stream));
		}
		return call;
	}

	private void functionCallConsumeParameter(Stream stream, FunctionCallNode call) {
		if (stream.matchesLeftBracket()) {
			stream.consumeLeftBracket();
			call.addParameter(functionCall(stream));
			stream.consumeRightBracket();
		} else if(stream.matchesIdentifier()) {
			call.addParameter(new SymbolNode(stream.consumeIdentifier()));
		} else if(stream.matchesHash()) {
			stream.consumeHash();
			call.addParameter(new NumberNode(stream.consumeDigits()));
		} else if(stream.matchesString()) {
			call.addParameter(new StringNode(stream.consumeString()));
		} else if(stream.matchesLeftBrace()) {
			closure(call, stream);
		}
	}
	
	public void closure(ASTNode node, Stream stream) {
		ClosureNode closure = new ClosureNode();
		((FunctionCallNode) node).addParameter(closure);
		
		stream.consumeLeftBrace();
		closureConsumeParameters(stream, closure);
		stream.consumePipe();
		functionBody(closure, stream);
		stream.consumeRightBrace();
	}

	private void closureConsumeParameters(Stream stream, ClosureNode closure) {
		if(stream.matchesIdentifier()) {
			closure.addParameter(stream.consumeIdentifier());
			while(stream.matchesComma()) {
				stream.consumeComma();
				closure.addParameter(stream.consumeIdentifier());
			}
		}
	}
	
	public void slotDefinition(ASTNode node, Stream stream) {
		StatementContainingNode fnode = (StatementContainingNode) node;

		stream.consumeLocalStart();
		fnode.addSlot(new SymbolNode(stream.consumeIdentifier()));
		stream.consumeRightBracket();
	}
	
	public ASTNode objectDefinition( Stream stream) {
		ConstructorNode objectConstructor = new ConstructorNode();

		stream.consumeObjectConstructorStart();
		messageDefinition(objectConstructor, stream);
		functionBody(objectConstructor, stream);
		stream.consumeRightBracket();
		
		return objectConstructor;
	}
	
	public void functionBody(StatementContainingNode node, Stream stream) {
		while(true) {
			if(stream.matchesIdentifier()) {
				node.addStatement(functionCall(stream));
			}
			else if(stream.matchesSlotStart()) {
				slotDefinition(node, stream);
				stream.consumeNewLine();
			}
			else if(stream.matchesFunctionStart()) {
				node.addStatement(functionDefinition(stream));
			} else if(stream.matchesObjectConstructorStart()) {
				node.addStatement(objectDefinition(stream));
			} else if(stream.matchesNewLine()) {
				stream.consumeNewLine();
			} else if(stream.matchesRightBracket() || stream.matchesRightBrace()) {
				return;
			} else {
				throw new RuntimeException("don't understand from [" + stream.getRemainder());
			}
		}
	}

	
	static class Stream {
		private final String program;
		private int index;
		private static final String WHITESPACE = "[^\\S\n]";

		Stream(String program, int index) {
			this.program = program;
			this.index = index;
		}
		
		public void consumeLocalStart() {
			consumeMatchWithPreceedingWhitespace("slot start", "\\(slot ");
		}
		
		public boolean matchesSlotStart() {
			return testMatch("\\(slot ");
		}
		
		public boolean matchesFunctionStart() {
			return testMatch("\\(function ");
		}

		public void consumeFunctionStart() {
			consumeMatchWithPreceedingWhitespace("Function start", "\\(function ");
		}

		public boolean matchesObjectConstructorStart() {
			return testMatch("\\(object ");
		}

		public void consumeObjectConstructorStart() {
			consumeMatchWithPreceedingWhitespace("Open object start", "\\(object ");
		}

		public boolean matchesHash() {
			return testMatch("#");
		}

		public void consumeHash() {
			consumeMatchWithPreceedingWhitespace("Hash", "#");
		}
		
		public boolean matchesPipe() {
			return testMatch("\\|");
		}

		public void consumePipe() {
			consumeMatchWithPreceedingWhitespace("Pipe", "\\|");
		}
		
		public boolean matchesComma() {
			return testMatch(",");
		}

		public void consumeComma() {
			consumeMatchWithPreceedingWhitespace("Comma", ",");
		}
		
		public boolean matchesLeftBrace() {
			return testMatch("\\[");
		}

		public void consumeLeftBrace() {
			consumeMatchWithPreceedingWhitespace("Brace", "\\[");
		}
		
		public boolean matchesRightBrace() {
			return testMatch("\\]");
		}

		public void consumeRightBrace() {
			consumeMatchWithPreceedingWhitespace("Brace", "\\]");
		}
		
		public boolean matchesLeftBracket() {
			return testMatch("\\(");
		}

		public void consumeLeftBracket() {
			consumeMatchWithPreceedingWhitespace("Bracket", "\\(");
		}
		
		public boolean matchesRightBracket() {
			return testMatch("\\)");
		}

		public void consumeRightBracket() {
			consumeMatchWithPreceedingWhitespace("Bracket", "\\)");
		}
		
		public boolean matchesNewLine() {
			return testMatch("(//.*)?\n");
		}

		public void consumeNewLine() {
			consumeMatchWithPreceedingWhitespace("Newline", "(//.*)?\n");
		}

		public String consumeWhiteSpace() {
			return consumeMatch("Whitespace", "(" + WHITESPACE + "+)");
		}

		public boolean matchesColon() {
			return program.charAt(index) == ':';
		}

		public String consumeColon() {
			if(program.charAt(index) == ':') {
				index++;
				return ":";
			}
			throw new RuntimeException("colon not found at [" + getRemainder());
		}

		public boolean matchesIdentifier() {
			return testMatch("[a-zA-Z_\\-0-9?]+");
		}

		public String consumeIdentifier() {
			return consumeMatchWithPreceedingWhitespace("Identifier", "[a-zA-Z_\\-0-9?]+");
		}
		
		public boolean matchesString() {
			return testMatch("'(.*)'");
		}

		public String consumeString() {
			return consumeMatch("String", WHITESPACE + "*'(.*)'");
		}

		public String consumeDigits() {
			return consumeMatch("Digits", "([0-9]+)");
		}

		private boolean testMatch(String match) {
			Pattern pattern = Pattern.compile(WHITESPACE + "*" + match);
			Matcher matcher = pattern.matcher(program);
			if(matcher.find(index) && matcher.start() == index) {
				return true;
			}
			return false;
		}
		
		private String consumeMatchWithPreceedingWhitespace(String nameOfMatch, String match) {
			return consumeMatch(nameOfMatch, WHITESPACE + "*(" + match + ")");
		}

		private String consumeMatch(String nameOfMatch, String fullMatch) {
			Pattern pattern = Pattern.compile(fullMatch);
			Matcher matcher = pattern.matcher(program);
			if(matcher.find(index) && matcher.start() == index) {
				index = matcher.end();
				return matcher.group(1);
			}
			throw new RuntimeException(nameOfMatch + " not found at [" + getRemainder());
		}
		
		public String getRemainder() {
			return program.substring(index);
		}

	}

	public ASTNode transform(String program) {
		Stream stream = new Stream(program, 0);
		
		return objectDefinition(stream);
	}

}
