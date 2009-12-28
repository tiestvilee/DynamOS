package org.dynamos.compiler;

public class StringNode extends ASTNode {
	private final String value;

	public StringNode(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
	@Override
	public String toString(String indent) {
		return value;
	}
}
