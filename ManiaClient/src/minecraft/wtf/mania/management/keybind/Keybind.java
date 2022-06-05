package wtf.mania.management.keybind;

public class Keybind {
	
	public final Object object;
	public int keyInt;
	public final String name;
	
	public Keybind(String name, Object object, int keyInt) {
		this.object = object;
		this.keyInt = keyInt;
		this.name = name;
	}

}
