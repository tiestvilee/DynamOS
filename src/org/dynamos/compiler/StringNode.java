package org.dynamos.compiler;

import java.util.List;

import org.dynamos.structures.OpCode;

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

	@Override
	public void compile(List<OpCode> opCodes, int tempNumber) {
		// TODO Auto-generated method stub
		
	}
}
