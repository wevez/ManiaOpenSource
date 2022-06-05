package wtf.mania.gui.screen.alt;

import java.util.Date;
	
public class Server {
		
	public final String ip;
	public Date unbanDate;
	public boolean banned;
	private final int color;
	
	public Server(String ip, int color) {
		this.ip = ip;
		banned = false;
		this.color = color;
	}
	
	public int getColor() {
		return this.color;
	}

}
