package org.dynamos.compiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class MessageNode extends StatementContainingNode {

	private List<String> arguments = new ArrayList<String>();
	
	public List<String> getArguments() {
		return Collections.unmodifiableList(arguments);
	}

	public void addParameter(String string) {
		arguments.add(string);
	}
	
	public abstract String type();
	
	public String toString() {		
		return toString("");
	}

	public String toString(String indent) {		
		String statementsAsString = "";
		for(ASTNode statement: statements) {
			statementsAsString += statement.toString(indent + "  ") + " ";
		}
		return indent + "(" + type() + " " + getName() + " {\n" + statementsAsString + "\n" + indent + "})\n";
	}
}
