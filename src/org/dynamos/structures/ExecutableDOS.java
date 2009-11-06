package org.dynamos.structures;

public abstract class ExecutableDOS extends ObjectDOS {
    public abstract ObjectDOS execute(ObjectDOS theObject, ListDOS arguments);
}
