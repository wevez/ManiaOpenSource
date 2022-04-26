package nazo.command.commands;

import nazo.Nazo;
import nazo.command.Command;
import nazo.module.Module;
import nazo.utils.ChatUtils;

public class Toggle extends Command{
	
	public Toggle() {
		super(new String[]{"toggle", "t"}, ".toggle(t) <Module> - toggles module", 2);
	}
	
	@Override
	public void onEnable(String[] str) {
		for(Module m : Nazo.moduleManager.modules) {
			if(str[1].equalsIgnoreCase(m.name)) {
				m.toggle();
				return;
			}
		}
	}

}
