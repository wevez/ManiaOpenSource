package wtf.mania.management.friend;

import java.util.LinkedList;
import java.util.List;

public class FriendManager {
	
	private final List<Friend> friendList;
	
	public void addFriend(String mcid) {
		friendList.add(new Friend(mcid));
	}
	
	public FriendManager() {
		friendList = new LinkedList<>();
	}
	
	public boolean isFreidn(String name) {
		for (Friend f : friendList) {
			if (f.isFriend(name)) return true;
		}
		return false;
	}

}
