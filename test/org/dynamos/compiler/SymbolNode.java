package org.dynamos.compiler;

public class SymbolNode extends ASTNode {
	private String name;
	public SymbolNode(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
