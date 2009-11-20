package org.dynamos.compiler;

public class SymbolNode extends ASTNode {
	public SymbolNode(String name) {
		appendToName(name);
	}
}
