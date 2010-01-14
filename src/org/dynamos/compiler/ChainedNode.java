package org.dynamos.compiler;


public abstract class ChainedNode extends NamedNode {

	protected FunctionCallNode chain;

	public void setChain(FunctionCallNode chain) {
		this.chain = chain;
	}

	public FunctionCallNode getChain() {
		return chain;
	}
}
