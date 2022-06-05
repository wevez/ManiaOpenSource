package wtf.mania.management.commmand.impl;

import wtf.mania.Mania;
import wtf.mania.management.commmand.Command;
import wtf.mania.module.Module;
import wtf.mania.util.ChatUtils;

public class CommandToggle2 extends Command {
	
	public CommandToggle2() {
		super("t", "Toggles module - t <module>", false);
	}
	
	@Override
	public void onCalled(String[] args) {
		String lowerName = args[1].toLowerCase();
		for(Module m : Mania.instance.moduleManager.array) {
			if(lowerName.equals(m.name.toLowerCase())) {
				m.toggle();
				ChatUtils.printClient(String.format("%s has been %s", m.name, m.toggled ? "Enabled" : "Disabled"));
				return;
			}
		}
		ChatUtils.printClient(String.format("Module <%s> was not found", args[1]));
	}

}
