/**
 * 
 */
package org.dynamos.types;

import org.dynamos.structures.ObjectDOS;

public class ValueObject extends ObjectDOS {
	private int value;

	public ValueObject(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

}