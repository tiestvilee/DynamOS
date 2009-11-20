package org.dynamos.compiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatementContainingNode extends NamedNode {
	
	List<ASTNode> statements = new ArrayList<ASTNode>();
	private List<SymbolNode> locals = new ArrayList<SymbolNode>();
	
	public void addStatement(ASTNode node) {
		statements.add(node);
	}

	public List<ASTNode> getStatements() {
		return Collections.unmodifiableList(statements);
	}
	
	public void addLocal(SymbolNode symbol) {
		locals.add(symbol);
	}
	
	public List<SymbolNode> getLocals() {
		return Collections.unmodifiableList(locals);
	}

}
