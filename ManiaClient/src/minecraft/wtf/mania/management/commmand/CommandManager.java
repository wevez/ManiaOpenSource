package wtf.mania.management.commmand;

import java.util.LinkedList;

import wtf.mania.management.Manager;
import wtf.mania.management.commmand.impl.*;
import wtf.mania.util.ChatUtils;

public class CommandManager extends Manager<Command> {
	
	public static char prefix = '.';
	
	public CommandManager() {
		super(new LinkedList<>());
		array.add(new CommandToggle());
		array.add(new CommandToggle2());
		array.add(new CommandBind());
		array.add(new CommandConfig());
		array.add(new CommandFriend());
		array.add(new CommandTP());
		array.add(new CommandFriend());
	}
	
	/*
	 * return whether message is included prefix
	 */
	public boolean onSendMessage(String message) {
		if(message.charAt(0) == prefix) {
			String[] spilited = message.split(" ");
			if(spilited.length == 0) return true;
			String lower = spilited[0].toLowerCase().substring(1);
			for(Command c : array) {
				if(c.lowerName.equals(lower)) {
					c.onCalled(spilited);
					break;
				}
			}
			return true;
		}else return false;
	}

}
