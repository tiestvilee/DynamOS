package org.dynamos.compiler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransformStringToAST {
	
	private abstract class State {

		public abstract void process(ASTNode root, Stream stream);
		
		public boolean isEOF() {
			return false;
		}

	}
	
	private class FunctionDefinition extends State {
		
		/*
		 * FunctionDefinition => Identifier(':' Identifier (FunctionDefinitionChain | ))
		 * FunctionDefinitionChain => Identifier ':' Identifier (FunctionDefinitionChain | ))
		 */
		@Override
		public void process(ASTNode node, Stream stream) {
			
			stream.consumeFunctionStart();
			
			FunctionNode fnode = (FunctionNode) node;
			
			String id = stream.consumeIdentifier();
			fnode.appendToFunctionName(id);
			
			if(stream.matchesColon()) {
				do {
					fnode.appendToFunctionName(stream.consumeColon());
					fnode.addParameter(stream.consumeIdentifier());
					if(!stream.matchesIdentifier()) {
						break;
					}
					fnode.appendToFunctionName(stream.consumeIdentifier());
				} while (true);
			}
			
			stream.consumeNewLine();
			
			new FunctionBody().process(fnode, stream);
			
			stream.consumeRightBracket();
		}
	}
	
	private class ContextFunctionCall extends State {
		@Override
		public void process(ASTNode node, Stream stream) {
			StatementContainingNode fnode = (StatementContainingNode) node;
			
			FunctionCallNode call = new FunctionCallNode();
			fnode.addStatement(call);
			
			new FunctionCall().process(call, stream);
		}
	}
	
	private class ParameterFunctionCall extends State {
		@Override
		public void process(ASTNode node, Stream stream) {
			FunctionCallNode fnode = (FunctionCallNode) node;
			
			FunctionCallNode call = new FunctionCallNode();
			fnode.addParameter(call);
			
			new FunctionCall().process(call, stream);
		}
	}
	
	private class ChainedFunctionCall extends State {
		@Override
		public void process(ASTNode node, Stream stream) {
			FunctionCallNode fnode = (FunctionCallNode) node;
			
			FunctionCallNode call = new FunctionCallNode();
			fnode.setChain(call);
			
			new FunctionCall().process(call, stream);
		}
	}
	
	private class FunctionCall extends State {
		/*
		 * 
		 *  FunctionCall => Identifier( ':' ( '(' FunctionCall ')' | Identifier ) | FunctionCall | ) 
		 * 
		 */
		@Override
		public void process(ASTNode node, Stream stream) {
			System.out.println("function call " + stream.getRemainder());
			FunctionCallNode call = (FunctionCallNode) node;

			String id = stream.consumeIdentifier();
			call.appendToFunctionName(id);
			
			if(stream.matchesColon()) {
				do {
					call.appendToFunctionName(stream.consumeColon());
					
					if (stream.matchesLeftBracket()) {
						stream.consumeLeftBracket();
						new ParameterFunctionCall().process(call, stream);
						stream.consumeRightBracket();
					} else if(stream.matchesIdentifier()) {
						call.addParameter(new SymbolNode(stream.consumeIdentifier()));
					}
					
					if(stream.matchesIdentifier()) {
						call.appendToFunctionName(stream.consumeIdentifier());
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
	}
	
	private class LocalDefinition extends State {
		@Override
		public void process(ASTNode node, Stream stream) {
			StatementContainingNode fnode = (StatementContainingNode) node;

			stream.consumeLocalStart();
			fnode.addLocal(new SymbolNode(stream.consumeIdentifier()));
			stream.consumeRightBracket();
			stream.consumeNewLine();
		}
	}
	
	private class FunctionBody extends State {
		@Override
		public void process(ASTNode node, Stream stream) {
			while(!stream.matchesRightBracket()) {
				if(stream.matchesIdentifier()) {
					new ContextFunctionCall().process(node, stream);
				}
				if(stream.matchesLocalStart()) {
					new LocalDefinition().process(node, stream);
				}
			}
		}
	}

	
	private class Stream {
		private final String program;
		private int index;
		private static final String WHITESPACE = "[^\\S\n]";

		Stream(String program, int index) {
			this.program = program;
			this.index = index;
		}
		
		public void consumeLocalStart() {
			consumeMatchWithPreceedingWhitespace("local start", "\\(local ");
		}
		
		public boolean matchesLocalStart() {
			return testMatch("\\(local ");
		}
		
		public void consumeFunctionStart() {
			consumeMatchWithPreceedingWhitespace("Function start", "\\(function ");
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

		public void consumeNewLine() {
			consumeMatchWithPreceedingWhitespace("Newline", "\n");
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
		ASTNode root = new FunctionNode();
		
		State state = new FunctionDefinition();
		
		state.process(root, stream);
		
		return root;
	}

}
