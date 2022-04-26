package nazo.command;

import java.util.ArrayList;

import nazo.command.commands.*;

public class CommandManager {
	
	public ArrayList<Command> commands = new ArrayList<Command>();
	
	public CommandManager() {
		this.commands.add(new Toggle());
		this.commands.add(new Bind());
		this.commands.add(new Value());
	}

}
