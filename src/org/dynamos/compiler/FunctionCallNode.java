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


}
