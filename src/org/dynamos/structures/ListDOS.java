package org.dynamos.structures;

import java.util.ArrayList;
import java.util.List;

import org.dynamos.structures.StandardObjects.ValueObject;

public class ListDOS extends ObjectDOS {
	
	List<ObjectDOS> list = new ArrayList<ObjectDOS>();
	
	public ListDOS() {
		setFunction(Symbol.get("at:"), new AtFunction());  // is this bad?  can only define this here as it uses non-static class
		setFunction(Symbol.get("add:"), new AddFunction());  // is this bad?  can only define this here as it uses non-static class
	}

	public void add(ObjectDOS object) {
		list.add(object);
	}

	public int size() {
		return list.size();
	}

	public ObjectDOS at(int index) {
		return list.get(index);
	}

	public List<ObjectDOS> getRawList() {
		return list;
	}
	
	class AtFunction extends ExecutableDOS {
		@Override
		public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
			StandardObjects.ValueObject index = (ValueObject) arguments.getRawList().get(0);
			System.out.println("made it! " + index.getValue() +  " " + list.get(index.getValue()));
			return list.get(index.getValue());
		}
	}
	
	class AddFunction extends ExecutableDOS {
		@Override
		public ObjectDOS execute(ObjectDOS theObject, ListDOS arguments) {
			list.add(arguments.at(0));
			return ListDOS.this;
		}
	}

}
