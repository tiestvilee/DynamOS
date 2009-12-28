package org.dynamos.compiler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransformStringToAST {
	
	static abstract class State {

		public abstract void process(ASTNode root, Stream stream);
		
		public boolean isEOF() {
			return false;
		}

	}
	
	static class FunctionDefinition extends State {
		
		/*
		 * FunctionDefinition => Identifier(':' Identifier (FunctionDefinitionChain | ))
		 * FunctionDefinitionChain => Identifier ':' Identifier (FunctionDefinitionChain | ))
		 */
		@Override
		public void process(ASTNode node, Stream stream) {
			
			stream.consumeFunctionStart();
			
			FunctionNode fnode = (FunctionNode) node;
			
			new MessageDefinition().process(fnode, stream);
			
			new FunctionBody().process(fnode, stream);
			
			stream.consumeRightBracket();
		}
	}
	
	static class MessageDefinition extends State {

		@Override
		public void process(ASTNode root, Stream stream) {
			MessageNode fnode = (MessageNode) root;
			
			String id = stream.consumeIdentifier();
			fnode.appendToName(id);
			
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
		
	}
	
	static class ContextFunctionCall extends State {
		@Override
		public void process(ASTNode node, Stream stream) {
			StatementContainingNode fnode = (StatementContainingNode) node;
			
			FunctionCallNode call = new FunctionCallNode();
			new FunctionCall().process(call, stream);
			
			fnode.addStatement(call);
		}
	}
	
	static class ParameterFunctionCall extends State {
		@Override
		public void process(ASTNode node, Stream stream) {
			FunctionCallNode fnode = (FunctionCallNode) node;
			
			FunctionCallNode call = new FunctionCallNode();
			fnode.addParameter(call);
			
			new FunctionCall().process(call, stream);
		}
	}
	
	static class ChainedFunctionCall extends State {
		@Override
		public void process(ASTNode node, Stream stream) {
			FunctionCallNode fnode = (FunctionCallNode) node;
			
			FunctionCallNode call = new FunctionCallNode();
			fnode.setChain(call);
			
			new FunctionCall().process(call, stream);
		}
	}
	
	static class FunctionCall extends State {
		/*
		 * 
		 *  FunctionCall => Identifier( ':' ( '(' FunctionCall ')' | Identifier ) | FunctionCall | ) 
		 * 
		 */
		@Override
		public void process(ASTNode node, Stream stream) {
			FunctionCallNode call = (FunctionCallNode) node;

			String id = stream.consumeIdentifier();
			System.out.println("function call " + id);
			call.appendToName(id);
			
			if(stream.matchesColon()) {
				do {
					call.appendToName(stream.consumeColon());
					
					consumeParameter(stream, call);
					
					if(stream.matchesIdentifier()) {
						call.appendToName(stream.consumeIdentifier());
					} else if (stream.matchesRightBracket()) {
						return;
					} else {
						stream.consumeNewLine();
						return;
					}
				} while (true);
			} else if (stream.matchesIdentifier()) {
				new ChainedFunctionCall().process(call, stream);
			}
		}

		private void consumeParameter(Stream stream, FunctionCallNode call) {
			if (stream.matchesLeftBracket()) {
				stream.consumeLeftBracket();
				new ParameterFunctionCall().process(call, stream);
				stream.consumeRightBracket();
			} else if(stream.matchesIdentifier()) {
				call.addParameter(new SymbolNode(stream.consumeIdentifier()));
			} else if(stream.matchesHash()) {
				stream.consumeHash();
				call.addParameter(new NumberNode(stream.consumeDigits()));
			} else if(stream.matchesString()) {
				call.addParameter(new StringNode(stream.consumeString()));
			} else if(stream.matchesLeftBrace()) {
				new Closure().process(call, stream);
			}
		}
	}
	
	static class Closure extends State {

		@Override
		public void process(ASTNode node, Stream stream) {
			ClosureNode closure = new ClosureNode();
			((FunctionCallNode) node).addParameter(closure);
			
			stream.consumeLeftBrace();
			consumeParameters(stream, closure);
			stream.consumePipe();
			new FunctionBody().process(closure, stream);
			stream.consumeRightBrace();
		}

		private void consumeParameters(Stream stream, ClosureNode closure) {
			if(stream.matchesIdentifier()) {
				closure.addParameter(stream.consumeIdentifier());
				while(stream.matchesComma()) {
					stream.consumeComma();
					closure.addParameter(stream.consumeIdentifier());
				}
			}
		}
		
	}
	
	static class SlotDefinition extends State {
		@Override
		public void process(ASTNode node, Stream stream) {
			StatementContainingNode fnode = (StatementContainingNode) node;

			stream.consumeLocalStart();
			fnode.addSlot(new SymbolNode(stream.consumeIdentifier()));
			stream.consumeRightBracket();
		}
	}
	
	static class ObjectDefinition extends State {
		@Override
		public void process(ASTNode node, Stream stream) {
			ConstructorNode objectConstructor = new ConstructorNode();
			
			StatementContainingNode fnode = (StatementContainingNode) node;
			fnode.addStatement(objectConstructor);

			stream.consumeObjectConstructorStart();
			
			new MessageDefinition().process(objectConstructor, stream);
			
			new FunctionBody().process(objectConstructor, stream);
			
			stream.consumeRightBracket();
		}
	}
	
	static class FunctionBody extends State {
		@Override
		public void process(ASTNode node, Stream stream) {
			while(true) {
				if(stream.matchesIdentifier()) {
					new ContextFunctionCall().process(node, stream);
				}
				else if(stream.matchesSlotStart()) {
					new SlotDefinition().process(node, stream);
					stream.consumeNewLine();
				}
				else if(stream.matchesFunctionStart()) {
					// yuck, should do this elsewhere
					ASTNode function = new FunctionNode();
					((StatementContainingNode) node).addStatement(function);
					new FunctionDefinition().process(function, stream);
					stream.consumeNewLine();
				} else if(stream.matchesObjectConstructorStart()) {
					new ObjectDefinition().process(node, stream);
				} else if(stream.matchesNewLine()) {
					stream.consumeNewLine();
				} else if(stream.matchesRightBracket() || stream.matchesRightBrace()) {
					return;
				} else {
					throw new RuntimeException("don't understand from [" + stream.getRemainder());
				}
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
		StatementContainingNode root = new StatementContainingNode();
		
		new ObjectDefinition().process(root, stream);
		
		return root.getStatements().get(0);
	}

}
