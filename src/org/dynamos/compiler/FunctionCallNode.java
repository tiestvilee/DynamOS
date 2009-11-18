package org.dynamos.compiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FunctionCallNode extends ASTNode {
	
	private String name;
	private List<ASTNode> arguments = new ArrayList<ASTNode>();
	private FunctionCallNode chain;
	
	public String getName() {
		return name;
	}
	
	public List<ASTNode> getArguments() {
		return Collections.unmodifiableList(arguments);
	}

	public void appendToFunctionName(String string) {
		if(name == null) {
			name = string;
		} else {
			name += string;
		}
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


}
