package org.dynamos.compiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dynamos.structures.OpCode;

public class StatementContainingNode extends NamedNode {
	
	List<ASTNode> statements = new ArrayList<ASTNode>();
	private List<SymbolNode> slots = new ArrayList<SymbolNode>();
	
	public void addStatement(ASTNode node) {
		statements.add(node);
	}

	public List<ASTNode> getStatements() {
		return Collections.unmodifiableList(statements);
	}
	
	public void addSlot(SymbolNode symbol) {
		slots.add(symbol);
	}
	
	public List<SymbolNode> getSlots() {
		return Collections.unmodifiableList(slots);
	}

	@Override
	public void compile(List<OpCode> opCodes, int tempNumber) {
		for(ASTNode node : statements) {
			node.compile(opCodes, tempNumber);
		}
	}

}
