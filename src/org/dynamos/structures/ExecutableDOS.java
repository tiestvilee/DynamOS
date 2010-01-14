package org.dynamos.structures;

import org.dynamos.OpCodeInterpreter;

public abstract class ExecutableDOS extends ObjectDOS {
    public abstract ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, ListDOS arguments);
}
