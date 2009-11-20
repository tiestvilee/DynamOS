package org.dynamos.compiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FunctionNode extends StatementContainingNode {

	private List<String> arguments = new ArrayList<String>();
	
	public List<String> getArguments() {
		return Collections.unmodifiableList(arguments);
	}

	public void addParameter(String string) {
		arguments.add(string);
	}
}
