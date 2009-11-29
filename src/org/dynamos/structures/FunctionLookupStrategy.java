package org.dynamos.structures;

public interface FunctionLookupStrategy {
	public ExecutableDOS lookupFunction(ObjectDOS object, Symbol symbol);
}
