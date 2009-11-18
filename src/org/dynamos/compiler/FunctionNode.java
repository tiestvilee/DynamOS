package org.dynamos.compiler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FunctionNode extends StatementContainingNode {

	private String name;
	private List<String> arguments = new ArrayList<String>();
	
	public String getName() {
		return name;
	}
	
	public List<String> getArguments() {
		return Collections.unmodifiableList(arguments);
	}

	public void appendToFunctionName(String string) {
		if(name == null) {
			name = string;
		} else {
			name += string;
		}
	}

	public void addParameter(String string) {
		arguments.add(string);
	}
}
