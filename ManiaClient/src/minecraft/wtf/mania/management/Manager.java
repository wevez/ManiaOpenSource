package wtf.mania.management;

import java.util.List;

public abstract class Manager <T> {

	public final List<T> array;
	
	public Manager(List<T> array) {
		this.array = array;
	}

}
