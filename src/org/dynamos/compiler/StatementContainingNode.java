package org.dynamos.compiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StatementContainingNode extends ASTNode {
	List<ASTNode> statements = new ArrayList<ASTNode>();
	
	public void addStatement(ASTNode node) {
		statements.add(node);
	}

	public List<ASTNode> getStatements() {
		return Collections.unmodifiableList(statements);
	}
}
