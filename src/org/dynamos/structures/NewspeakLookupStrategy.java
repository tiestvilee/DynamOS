package org.dynamos.structures;

public class NewspeakLookupStrategy implements FunctionLookupStrategy {

	@Override
	public ExecutableDOS lookupFunction(ObjectDOS object, Symbol symbol) {
        return getContextFunction(object, false, symbol);
	}
	
    private ExecutableDOS getContextFunction(ObjectDOS object, boolean foundEnclosingObject, Symbol symbol) {
        ExecutableDOS function = object.getFunctions().get(symbol);
        if(function == null) {
        	boolean isEnclosingObject = !(foundEnclosingObject || object instanceof Context);
        	
        	if(object.getContext() != null) {
            	System.out.println("  searching context " + isEnclosingObject + " " + object.getContext());
        		function = getContextFunction(object.getContext(), isEnclosingObject, symbol);
        	}
        	if(function == null && isEnclosingObject && object.getParent() != null) {
            	System.out.println("  searching parent " + object.getParent());
	            function = getParentFunction(object.getParent(), symbol);
        	}
        } else {
        	System.out.println("  found.");
        }
        return function;
    }
    
    private ExecutableDOS getParentFunction(ObjectDOS object, Symbol symbol) {
        ExecutableDOS function = object.getFunctions().get(symbol);
        if(function == null && object.getParent() != object) {
        	System.out.println("  searching parent " + object.getParent());
        	function = getParentFunction(object.getParent(), symbol);
        }
        return function;
    }

}
