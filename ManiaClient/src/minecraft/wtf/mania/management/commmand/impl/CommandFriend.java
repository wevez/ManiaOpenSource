package wtf.mania.management.commmand.impl;

import wtf.mania.Mania;
import wtf.mania.management.commmand.Command;
import wtf.mania.util.ChatUtils;

public class CommandFriend extends Command {

	public CommandFriend() {
		super("friend", ".friend  <mcid>", false);
	}

	@Override
	public void onCalled(String[] args) {
		if (args.length == 2) {
			Mania.instance.friendManager.addFriend(args[1]);
			ChatUtils.printClient(String.format("Added friend (MCID : %s)", args[1]));
		} else {
			ChatUtils.printClient(desc);
		}
	}

}
