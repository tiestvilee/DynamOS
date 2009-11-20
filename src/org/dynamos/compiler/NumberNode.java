package org.dynamos.compiler;

public class NumberNode extends ASTNode {
	private final int value;

	public NumberNode(String value) {
		this.value = Integer.parseInt(value);
	}
	
	public int getValue() {
		return value;
	}
}
