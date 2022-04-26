package nazo.command;

import nazo.utils.ChatUtils;

public class Command {
	
	public String disc;
	public int length;
	public String[] name;
	
	public Command(String[] name, String disc, int length) {
		this.name = name;
		this.disc = disc;
		this.length = length;
	}
	
	public void a(String[] msg) {
		if(msg.length == this.length)
			this.onEnable(msg);
		else
			ChatUtils.printChatprefix(this.disc);
	}
	
	public void onEnable(String[] msg) {}

}
