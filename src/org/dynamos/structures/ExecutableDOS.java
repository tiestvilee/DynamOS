package org.dynamos.structures;

import java.util.List;

import org.dynamos.OpCodeInterpreter;

public abstract class ExecutableDOS extends ObjectDOS {
    public abstract ObjectDOS execute(OpCodeInterpreter interpreter, ObjectDOS theObject, List<ObjectDOS> arguments);
}
