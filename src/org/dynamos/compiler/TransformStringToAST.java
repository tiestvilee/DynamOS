package org.dynamos.compiler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransformStringToAST {
	
	private abstract class State {

		public abstract State process(ASTNode root, Stream stream);
		
		public boolean isEOF() {
			return false;
		}

	}

	private class FunctionDefinition extends State {
		@Override
		public State process(ASTNode node, Stream stream) {
			
			stream.mustMatchAndConsume("(function ");
			
			FunctionNode fnode = (FunctionNode) node;
			
			Matcher match = stream.matchAndConsume("([a-zA-Z_\\-0-9?]+)(: ([a-zA-Z_\\-0-9?]+) ?)?");
			do {
				String partialFunctionName = match.group(1);
				if(match.group(3) != null) {
					fnode.addParameter(match.group(3));
					partialFunctionName += ":";
				}
				fnode.appendToFunctionName(partialFunctionName);
			} while ( (match = stream.matchAndConsume("([a-zA-Z_\\-0-9?]+): (([a-zA-Z_\\-0-9?]+) ?)")) != null);
			
			stream.matchAndConsume("\\n");
			
			return new FunctionBody();
		}
	}
	
	private class FunctionBody extends State {
		@Override
		public State process(ASTNode root, Stream stream) {
			if(stream.doesMatch(")")) {
				return new EOF();
			}
			throw new RuntimeException("did not understand from [" + stream.getRemainder() + "]");
		}
	}
	
	private class EOF extends State {
		@Override
		public State process(ASTNode root, Stream stream) {
			throw new RuntimeException("sholdnt' be processed");
		}
		
		public boolean isEOF() {
			return true;
		}
	}
	

	
	private class Stream {
		private final String program;
		private int index;

		Stream(String program, int index) {
			this.program = program;
			this.index = index;
		}
		
		public boolean doesMatch(String match) {
			Pattern pattern = Pattern.compile("\\s*" + Pattern.quote(match));
			Matcher matcher = pattern.matcher(program);
			return matcher.find(index) && matcher.start() == index;
		}

		public String getRemainder() {
			return program.substring(index);
		}

		public Matcher matchAndConsume(String match) {
			Pattern pattern = Pattern.compile(match);
			Matcher matcher = pattern.matcher(program);
			if(matcher.find(index) && matcher.start() == index) {
				index = matcher.end();
				return matcher;
			}
			return null;
		}

		public void mustMatchAndConsume(String match) {
			Pattern pattern = Pattern.compile("\\s*" + Pattern.quote(match));
			Matcher matcher = pattern.matcher(program);
			if(matcher.find(index) && matcher.start() == index) {
				index = matcher.end();
			} else {
				throw new RuntimeException("didn't match [" + match + "] to [" + program.substring(index) + "]");
			}
		}
	}

	public ASTNode transform(String program) {
		Stream stream = new Stream(program, 0);
		ASTNode root = new FunctionNode();
		
		State state = new FunctionDefinition();
		
		while( ! state.isEOF() ) {
			state = state.process(root, stream);
		}
		
		return root;
	}

}
