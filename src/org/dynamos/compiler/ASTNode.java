package org.dynamos.compiler;

import java.util.List;

import org.dynamos.structures.OpCode;

public abstract class ASTNode {
	public abstract String toString(String indent);
	public abstract void compile(List<OpCode> opCodes, int tempNumber);

}
