package org.dynamos.compiler;

import java.util.List;

import org.dynamos.structures.OpCode;

public class NumberNode extends ASTNode {
	private final int value;

	public NumberNode(String value) {
		this.value = Integer.parseInt(value);
	}
	
	public int getValue() {
		return value;
	}
	
	@Override
	public String toString(String indent) {
		return "" + value;
	}

	@Override
	public void compile(List<OpCode> opCodes, int tempNumber) {
		// TODO Auto-generated method stub
		
	}

}
