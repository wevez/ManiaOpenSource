package wtf.mania.management.commmand.impl;

import wtf.mania.management.commmand.Command;

public class CommandHelp extends Command {

	public CommandHelp() {
		super("help", ".help - display cpmmand list", false);
	}

	@Override
	public void onCalled(String[] args) {
		// TODO Auto-generated method stub
		
	}
}
