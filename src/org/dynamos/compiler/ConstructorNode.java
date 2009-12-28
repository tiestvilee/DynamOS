package org.dynamos.compiler;



public class ConstructorNode extends MessageNode {
	// a cosntructor node is a message node
	
	public String type() {
		return "object";
	}
}
