package org.dynamos.compiler;

public class ASTNode {

	private String name;

	public String getName() {
		return name;
	}

	public void appendToName(String string) {
		if(name == null) {
			name = string;
		} else {
			name += string;
		}
	}

}
