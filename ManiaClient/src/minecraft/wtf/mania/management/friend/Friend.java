package wtf.mania.management.friend;

public class Friend {
	
	private final String mcid;

	public Friend( String mcid) {
		this.mcid = mcid;
	}
	
	public boolean isFriend(String name) {
		if (name.equals(mcid)) return true;
		return false;
	}

}
