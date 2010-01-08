package org.dynamos.compiler;

import org.dynamos.structures.Symbol;

public abstract class NamedNode extends ASTNode {

	private String name;

	public String getName() {
		return name;
	}

	public Symbol getSymbol() {
		return Symbol.get(name);
	}

	public void appendToName(String string) {
		if(name == null) {
			name = string;
		} else {
			name += string;
		}
	}

	@Override
	public String toString(String indent) {
		return name;
	}

}
