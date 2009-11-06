/**
 * 
 */
package org.dynamos.structures;

public class FunctionDOS extends ExecutableDOS {
    protected FunctionDefinitionDOS function;
    protected Context context;

    public FunctionDOS(FunctionDefinitionDOS function, Context context) {
        this.function = function;
        this.context = context;
    }

    public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
        Context newContext = function.newContext(context, arguments, theObject);
        function.execute(newContext);
        return newContext.getSlot(Symbol.RESULT);
    }
}