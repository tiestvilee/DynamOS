package org.dynamos.compiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dynamos.structures.OpCode;

public class StatementContainingNode extends NamedNode {
	
	List<ASTNode> statements = new ArrayList<ASTNode>();
	private List<SymbolGetterNode> slots = new ArrayList<SymbolGetterNode>();
	
	public void addStatement(ASTNode node) {
		statements.add(node);
	}

	public List<ASTNode> getStatements() {
		return Collections.unmodifiableList(statements);
	}
	
	public void addSlot(SymbolGetterNode symbol) {
		slots.add(symbol);
	}
	
	public List<SymbolGetterNode> getSlots() {
		return Collections.unmodifiableList(slots);
	}

	@Override
	public void compile(List<OpCode> opCodes, int tempNumber) {
		for(ASTNode node : statements) {
			node.compile(opCodes, tempNumber);
		}
	}

}
