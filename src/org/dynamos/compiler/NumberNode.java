package org.dynamos.compiler;

import java.util.List;

import org.dynamos.structures.OpCode;

public class NumberNode extends ValueNode {
	
	public NumberNode(String value) {
		super(value);
	}
	
	@Override
	public void compile(List<OpCode> opCodes, int tempNumber) {
		// TODO Auto-generated method stub
		
	}

}
