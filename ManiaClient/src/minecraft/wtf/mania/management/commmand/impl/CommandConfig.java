package wtf.mania.management.commmand.impl;

import wtf.mania.Mania;
import wtf.mania.management.commmand.Command;
import wtf.mania.module.Module;
import wtf.mania.util.ChatUtils;

public class CommandConfig extends Command {

	public CommandConfig() {
		super("config", ".config <save / load> - load or save config", false);
	}

	@Override
	public void onCalled(String[] args) {
		if(args.length == 3) {
			switch(args[1].toLowerCase()) {
			case "save":
				Mania.instance.configManager.deleteConfig(args[2]);
				Mania.instance.configManager.saveConfig(args[2]);
				ChatUtils.printClient(String.format("Config '%s' has been saved", args[2]));
				return;
			case "load":
				if(Mania.instance.configManager.loadConfig(args[2])) {
					ChatUtils.printClient(String.format("Config '%s' has been loaded", args[2]));
					for(Module m : Mania.instance.moduleManager.array) {
			        	m.onSetting();
			        }
				}
				else
					ChatUtils.printClient(String.format("Failed to load config '%s'", args[2]));
				return;
			}
			return;
		}else
			return;
	}

}
