package org.dynamos.structures;

public class OpCodeWrapper extends ObjectDOS {

	private final OpCode opCode;

	public OpCodeWrapper(OpCode opCode) {
		this.opCode = opCode;
	}
	
	public OpCode getOpCode() {
		return opCode;
	}
	
	public String toString() {
		return opCode.toString();
	}
}
