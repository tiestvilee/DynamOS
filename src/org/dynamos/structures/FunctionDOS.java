/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dynamos.structures;

import java.util.List;

/**
 *
 * @author tiestvilee
 */
public class FunctionDOS extends ObjectDOS {
    private List<OpCode> opCodes;

    public FunctionDOS(List<OpCode> opCodes) {
        super();
        this.opCodes = opCodes;
    }

    public void execute(Context context) {
        
    }

    public static class ContextualFunctionDOS extends ObjectDOS {
        private FunctionDOS function;
        private Context context;

        public ContextualFunctionDOS(FunctionDOS function, Context context) {
            this.function = function;
            this.context = context;
        }

        public void execute(List<Object> arguments) {

        }

        public void execute(ObjectDOS theObject, List<Object> arguments) {

        }
    }

    /* is this needed? */
    public static class MethodDOS extends ObjectDOS {
        private ContextualFunctionDOS contextualFunction;
        private ObjectDOS object;

        public MethodDOS(ContextualFunctionDOS contextualFunction, ObjectDOS object) {
            this.contextualFunction = contextualFunction;
            this.object = object;
        }
        
        public void execute(List<Object> arguments) {

        }
    }

}
