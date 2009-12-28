package org.dynamos.compiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FunctionCallNode extends NamedNode {
	
	private List<ASTNode> arguments = new ArrayList<ASTNode>();
	private FunctionCallNode chain;
	
	public List<ASTNode> getArguments() {
		return Collections.unmodifiableList(arguments);
	}

	public void addParameter(ASTNode node) {
		arguments.add(node);
	}
	
	public void setChain(FunctionCallNode chain) {
		this.chain = chain;
	}

	public FunctionCallNode getChain() {
		return chain;
	}
	
	public String toString() {
		return toString("");
	}
	
	public String toString(String indent) {
		String args = "";
		for(ASTNode argument : arguments) {
			args += argument.toString(indent + "  ");
		}
		String result = "(" + getName() + args + ")";
		
		if(chain != null) {
			return chain.toString(indent, result);
		}
		return result;
	}

	private String toString(String indent, String chaincalculation) {
		return "(" + chaincalculation + " " + toString(indent) + ")";
	}

}
