package wtf.mania.gui.music;

public class Song {
	
	private final String name, id;
	private final int length;
	
	public Song(String name, String id, int length) {
		this.name = name;
		this.id = id;
		this.length = length;
	}
	
	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	public int getLength() {
		return length;
	}

}
